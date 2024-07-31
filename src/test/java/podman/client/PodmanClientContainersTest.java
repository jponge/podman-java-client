package podman.client;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import podman.client.containers.ContainerCreateOptions;
import podman.client.containers.ContainerDeleteOptions;
import podman.client.containers.ContainerGetLogsOptions;
import podman.client.containers.MultiplexedStreamFrame;
import podman.machine.PodmanMachineClient;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import static helpers.AsyncTestHelpers.awaitResult;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PodmanClientContainersTest {

    static final String PODMAN_HELLO_REF = "quay.io/podman/hello:latest";
    static final String NOT_FOUND_REF = "quay.io/idonotexist/icannotbefound:idonotexist";
    static final String NOT_REACHABLE_REF = "sample.test/podman/hello:latest";
    private static final Logger log = LoggerFactory.getLogger(PodmanClientContainersTest.class);

    Vertx vertx;
    PodmanClient client;

    @BeforeAll
    void setup() throws Throwable {
        VertxOptions vertxOptions = new VertxOptions().setPreferNativeTransport(true);
        vertx = Vertx.vertx(vertxOptions);

        PodmanMachineClient machineClient = PodmanMachineClient.create(vertx);
        String socketPath = awaitResult(machineClient.findDefaultMachineConnectionSocketPath());
        PodmanClient.Options options = new PodmanClient.Options().setSocketPath(socketPath);
        client = PodmanClient.create(vertx, options);
    }

    @AfterAll
    void cleanup() throws Throwable {
        awaitResult(client.close());
        awaitResult(vertx.close());
    }

    @Test
    void createAndDeleteContainer() throws Throwable {
        JsonObject createResult = awaitResult(client.containers()
                .create(new ContainerCreateOptions()
                        .image("quay.io/podman/hello:latest")
                        .remove(true)));
        String id = createResult.getString("Id");

        assertThat(awaitResult(client.containers().exists(id))).isTrue();

        JsonArray deleteResult = awaitResult(client.containers()
                .delete(
                        id,
                        new ContainerDeleteOptions()
                                .setDeleteVolumes(true)
                                .setDepend(true)
                                .setIgnore(false)));
        assertThat(deleteResult).hasSizeGreaterThanOrEqualTo(1);
        assertThat(deleteResult.getJsonObject(0).getString("Id")).isEqualTo(id);

        assertThat(awaitResult(client.containers().exists(id))).isFalse();
    }

    @Test
    void getLogsPodmanHello() throws Throwable {
        JsonObject createResult = awaitResult(
                client.containers().create(new ContainerCreateOptions().image("quay.io/podman/hello:latest")));
        String id = createResult.getString("Id");

        awaitResult(client.containers().start(id));

        AssertSubscriber<MultiplexedStreamFrame> sub = AssertSubscriber.create(Long.MAX_VALUE);
        client.containers().logs(id, new ContainerGetLogsOptions()).subscribe(sub);

        sub.awaitCompletion().assertCompleted();
        List<MultiplexedStreamFrame> frames = sub.getItems();

        assertThat(frames)
                .isNotEmpty()
                .anyMatch(frame -> (frame.type() == MultiplexedStreamFrame.Type.STD_OUT)
                                   && frame.buffer().toString().equals("!... Hello Podman World ...!\n"))
                .anyMatch(frame -> (frame.type() == MultiplexedStreamFrame.Type.STD_OUT)
                                   && frame.buffer().toString().equals("Website:   https://podman.io\n"));

        awaitResult(client.containers().delete(id, new ContainerDeleteOptions().setIgnore(true)));
    }

    @Test
    void lifeOfLongRunningContainerWithPause() throws Throwable {
        JsonObject createResult = awaitResult(
                client.containers().create(new ContainerCreateOptions()
                        .image("registry.access.redhat.com/ubi8/ubi-minimal:8.10")
                        .command(List.of(
                                "/bin/sh",
                                "-c",
                                "i=0; while true; do echo $i; i=$((i+1)); sleep 0.1; done"
                        ))));
        String id = createResult.getString("Id");
        awaitResult(client.containers().start(id));

        AssertSubscriber<MultiplexedStreamFrame> logsSub = AssertSubscriber.create(Long.MAX_VALUE);
        client.containers().logs(id, new ContainerGetLogsOptions().setFollow(true)).subscribe(logsSub);

        Thread.sleep(250);
        awaitResult(client.containers().pause(id));

        logsSub.assertNotTerminated();
        int countAtPause = logsSub.getItems().size();

        awaitResult(client.containers().unpause(id));
        Thread.sleep(250);

        logsSub.assertNotTerminated();
        int countAfterPause = logsSub.getItems().size();

        awaitResult(client.containers().kill(id));
        awaitResult(client.containers().delete(id, new ContainerDeleteOptions().setIgnore(true)));

        logsSub.awaitCompletion();
        assertThat(countAtPause).isLessThan(countAfterPause);

        List<Integer> nums = logsSub.getItems().stream()
                .map(MultiplexedStreamFrame::buffer)
                .map(buffer -> Integer.valueOf(buffer.toString().trim()))
                .toList();
        for (int i = 0; i < nums.size(); i++) {
            assertThat(nums.get(i)).isEqualTo(i);
        }
    }

    @Test
    void lifeOfLongRunningContainerWithStartStop() throws Throwable {
        JsonObject createResult = awaitResult(
                client.containers().create(new ContainerCreateOptions()
                        .image("registry.access.redhat.com/ubi8/ubi-minimal:8.10")
                        .command(List.of(
                                "/bin/sh",
                                "-c",
                                "i=0; while true; do echo $i; i=$((i+1)); sleep 0.1; done"
                        ))));
        String id = createResult.getString("Id");
        awaitResult(client.containers().start(id));

        AssertSubscriber<MultiplexedStreamFrame> logsSub = AssertSubscriber.create(Long.MAX_VALUE);
        client.containers().logs(id, new ContainerGetLogsOptions().setFollow(true)).subscribe(logsSub);

        Thread.sleep(250);
        awaitResult(client.containers().stop(id, false, 1));
        logsSub.awaitCompletion();
        ArrayList<MultiplexedStreamFrame> itemsOnFirstRun = new ArrayList<>(logsSub.getItems());

        awaitResult(client.containers().start(id));
        logsSub = AssertSubscriber.create(Long.MAX_VALUE);
        client.containers().logs(id, new ContainerGetLogsOptions().setFollow(true)).subscribe(logsSub);
        Thread.sleep(250);

        awaitResult(client.containers().kill(id));
        awaitResult(client.containers().delete(id, new ContainerDeleteOptions().setIgnore(true)));

        logsSub.awaitCompletion();
        ArrayList<MultiplexedStreamFrame> itemsOnSecondRun = new ArrayList<>(logsSub.getItems());

        List<Integer> nums1 = itemsOnFirstRun.stream()
                .map(MultiplexedStreamFrame::buffer)
                .map(buffer -> Integer.valueOf(buffer.toString().trim()))
                .toList();
        List<Integer> nums2 = itemsOnSecondRun.stream()
                .map(MultiplexedStreamFrame::buffer)
                .map(buffer -> Integer.valueOf(buffer.toString().trim()))
                .toList();
        assertThat(nums2.size()).isGreaterThan(nums1.size() + 1);
        assertThat(nums2).startsWith(nums1.toArray(new Integer[0]));
        assertThat(nums2.get(nums1.size())).isEqualTo(0);
        assertThat(nums2.get(nums1.size() + 1)).isEqualTo(1);
    }
}
