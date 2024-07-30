package podman.client;

import static helpers.AsyncTestHelpers.awaitResult;
import static org.assertj.core.api.Assertions.assertThat;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import podman.client.containers.ContainerCreateOptions;
import podman.client.containers.ContainerDeleteOptions;
import podman.machine.PodmanMachineClient;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PodmanClientContainersTest {

    static final String PODMAN_HELLO_REF = "quay.io/podman/hello:latest";
    static final String NOT_FOUND_REF = "quay.io/idonotexist/icannotbefound:idonotexist";
    static final String NOT_REACHABLE_REF = "sample.test/podman/hello:latest";

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

    @Disabled
    void lifecycleMethods() throws Throwable {
        // TODO use a long-running image
        JsonObject createResult = awaitResult(
                client.containers().create(new ContainerCreateOptions().image("quay.io/podman/hello:latest")));
        String id = createResult.getString("Id");

        awaitResult(client.containers().start(id));
        awaitResult(client.containers().pause(id));
        awaitResult(client.containers().start(id));
        awaitResult(client.containers().stop(id, false, 10));
    }
}
