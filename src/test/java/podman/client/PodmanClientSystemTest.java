package podman.client;

import static helpers.AsyncTestHelpers.awaitResult;
import static org.assertj.core.api.Assertions.assertThat;

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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PodmanClientSystemTest {

    Vertx vertx;
    PodmanClient client;

    @BeforeEach
    void setup() throws Throwable {
        PodmanMachineClient machineClient = PodmanMachineClient.create(vertx);
        String socketPath = awaitResult(machineClient.findDefaultMachineConnectionSocketPath());
        PodmanClient.Options options = new PodmanClient.Options().setSocketPath(socketPath);
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

    @Test
    void df() throws Throwable {
        JsonObject data = awaitResult(client.system().df());
        assertThat(data.size()).isPositive();
        assertThat(data.containsKey("ImagesSize")).isTrue();
        assertThat(data.containsKey("Images")).isTrue();
        assertThat(data.containsKey("Containers")).isTrue();
        assertThat(data.containsKey("Volumes")).isTrue();
    }

    @Test
    void ping() throws Throwable {
        JsonObject data = awaitResult(client.system().ping());
        assertThat(data.size()).isPositive();
        assertThat(data.containsKey("Api-Version")).isTrue();
        assertThat(data.containsKey("Libpod-Api-Version")).isTrue();
    }
}
