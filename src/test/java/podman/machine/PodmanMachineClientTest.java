package podman.machine;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import podman.machine.machine.PodmanMachineClient;

import static helpers.AsyncTestHelpers.awaitResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.TestInstance.*;

@TestInstance(Lifecycle.PER_CLASS)
class PodmanMachineClientTest {

    Vertx vertx;

    @BeforeAll
    void setup() {
        vertx = Vertx.vertx();
    }

    @AfterAll
    void cleanup() throws Throwable {
        awaitResult(vertx.close());
    }

    @Test
    void inspect() throws Throwable {
        PodmanMachineClient client = PodmanMachineClient.create(vertx);
        JsonArray data = awaitResult(client.inspect("podman-machine-default"));

        assertThat(data).hasSize(1);
        JsonObject entry = data.getJsonObject(0);
        assertThat(entry.containsKey("ConfigDir")).isTrue();
        assertThat(entry.containsKey("ConnectionInfo")).isTrue();
        assertThat(entry.containsKey("SSHConfig")).isTrue();
    }

    @Test
    void info() throws Throwable {
        PodmanMachineClient client = PodmanMachineClient.create(vertx);
        JsonObject data = awaitResult(client.info());

        assertThat(data.containsKey("Host")).isTrue();
        assertThat(data.containsKey("Version")).isTrue();
    }

    @Test
    void list() throws Throwable {
        PodmanMachineClient client = PodmanMachineClient.create(vertx);
        JsonArray data = awaitResult(client.list());

        assertThat(data).hasSizeGreaterThanOrEqualTo(1);
        JsonObject entry = data.getJsonObject(0);
        assertThat(entry.containsKey("Name")).isTrue();
        assertThat(entry.containsKey("Running")).isTrue();
    }
}