package podman.client;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import podman.machine.PodmanMachineClient;

import static helpers.AsyncTestHelpers.awaitResult;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PodmanClientSystemTest {

    Vertx vertx;
    PodmanClient client;

    @BeforeEach
    void setup() throws Throwable {
        PodmanMachineClient machineClient = PodmanMachineClient.create(vertx);
        String socketPath = awaitResult(machineClient.findDefaultMachineConnectionSocketPath());
        PodmanClient.Options options = new PodmanClient.Options()
                .setSocketPath(socketPath);
        client = PodmanClient.create(vertx, options);
    }

    @AfterEach
    void cleanup() {
        client.close();
    }

    @BeforeAll
    void vertxSetup() {
        VertxOptions vertxOptions = new VertxOptions().setPreferNativeTransport(true);
        vertx = Vertx.vertx(vertxOptions);
    }

    @AfterAll
    void vertxCleanup() throws Throwable {
        awaitResult(vertx.close());
    }

    @Test
    void version() throws Throwable {
        JsonObject data = awaitResult(client.system().version());
        assertThat(data.size()).isPositive();
        assertThat(data.containsKey("Version")).isTrue();
    }

    @Test
    void info() throws Throwable {
        JsonObject data = awaitResult(client.system().info());
        assertThat(data.size()).isPositive();
        assertThat(data.containsKey("host")).isTrue();
    }
}