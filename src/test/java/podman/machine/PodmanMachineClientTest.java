package podman.machine;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import podman.machine.machine.PodmanMachineClient;
import podman.machine.machine.PodmanMachineInspectResult;

import static helpers.AsyncTestHelpers.awaitResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
        String target = "podman-machine-default";
        PodmanMachineClient client = PodmanMachineClient.create(vertx);
        PodmanMachineInspectResult result = awaitResult(client.inspect(target));

        JsonObject data = result.payload();
        assertThat(data.containsKey("ConfigDir")).isTrue();
        assertThat(data.containsKey("ConnectionInfo")).isTrue();
        assertThat(data.containsKey("SSHConfig")).isTrue();

        assertThat(result.payload()).isSameAs(data);
        assertThat(result.connectionSocketPath()).isNotBlank();
        assertThat(result.name()).isEqualTo(target);
        assertThat(result.state()).isNotBlank();
        assertDoesNotThrow(result::rootful);
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