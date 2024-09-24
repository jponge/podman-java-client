package podman.machine;

import static helpers.TestHelpers.awaitResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

@TestInstance(Lifecycle.PER_CLASS)
@DisabledOnOs(OS.LINUX)
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
        assertDoesNotThrow(result::isRootful);
    }

    @Test
    void inspectMissing() {
        PodmanMachineClient client = PodmanMachineClient.create(vertx);
        IOException err = assertThrows(IOException.class, () -> awaitResult(client.inspect("yolo-abcdef-123456")));
        assertThat(err).hasMessageContaining("Failed with exit code");
    }

    @Test
    void findDefaultMachineConnectionSocketPath() throws Throwable {
        PodmanMachineClient client = PodmanMachineClient.create(vertx);
        String path = awaitResult(client.findDefaultMachineConnectionSocketPath());
        assertThat(path).isNotBlank();
    }

    @Test
    void info() throws Throwable {
        PodmanMachineClient client = PodmanMachineClient.create(vertx);
        PodmanMachineInfoResult data = awaitResult(client.info());

        assertThat(data.host().os()).isNotBlank();
        assertThat(data.version().gitCommit()).isNotBlank();
    }

    @Test
    void list() throws Throwable {
        PodmanMachineClient client = PodmanMachineClient.create(vertx);
        List<PodmanMachineListResult> data = awaitResult(client.list());

        assertThat(data).hasSizeGreaterThanOrEqualTo(1);
        PodmanMachineListResult first = data.get(0);
        assertThat(first.cpus()).isPositive();
        assertThat(first.name()).isNotBlank();
        assertThat(first.remoteUsername()).isNotBlank();
    }
}
