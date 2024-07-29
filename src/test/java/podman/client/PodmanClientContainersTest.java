package podman.client;

import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import podman.client.containers.ContainerCreateOptions;
import podman.client.images.ImagePullOptions;
import podman.machine.PodmanMachineClient;

import java.util.List;
import java.util.concurrent.Flow;

import static helpers.AsyncTestHelpers.awaitResult;
import static org.assertj.core.api.Assertions.assertThat;

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
        JsonObject result = awaitResult(client.containers().create(new ContainerCreateOptions()
                .image("quay.io/podman/hello:latest")
                .remove(true)));
        // TODO
        System.out.println(result.encodePrettily());
    }
}
