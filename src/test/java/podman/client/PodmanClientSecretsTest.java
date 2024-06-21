package podman.client;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import podman.machine.PodmanMachineClient;

import static helpers.AsyncTestHelpers.awaitResult;
import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PodmanClientSecretsTest {

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
        client.close();
        awaitResult(vertx.close());
    }

    @Test
    @Order(1)
    void createSecret() throws Throwable {
        JsonObject result = awaitResult(client.secrets().create("foo", "bar", new SecretCreateOptions()));
        assertThat(result.containsKey("ID")).isTrue();
    }

    @Test
    @Order(2)
    void removeSecret() throws Throwable {
        awaitResult(client.secrets().remove("foo"));
    }

    @Test
    @Order(3)
    void exists() throws Throwable {
        awaitResult(client.secrets().create("yolo", "this", new SecretCreateOptions()));
        assertThat(awaitResult(client.secrets().exists("yolo"))).isTrue();
        assertThat(awaitResult(client.secrets().exists("foo"))).isFalse();
        awaitResult(client.secrets().remove("yolo"));
    }

    @Test
    @Order(4)
    void inspect() throws Throwable {
        awaitResult(client.secrets().create("yolo", "this", new SecretCreateOptions()));
        JsonObject data = awaitResult(client.secrets().inspect("yolo", true));
        assertThat(data.containsKey("ID")).isTrue();
        assertThat(data.getString("SecretData")).isEqualTo("this");
        data = awaitResult(client.secrets().inspect("yolo", false));
        assertThat(data.containsKey("SecretData")).isFalse();
        awaitResult(client.secrets().remove("yolo"));
    }
}
