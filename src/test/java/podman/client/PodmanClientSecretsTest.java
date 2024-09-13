package podman.client;

import static helpers.AsyncTestHelpers.awaitResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import helpers.PodmanHelpers;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.pointer.JsonPointer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import podman.client.secrets.SecretCreateOptions;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PodmanClientSecretsTest {

    Vertx vertx;
    PodmanClient client;

    @BeforeAll
    void setup() throws Throwable {
        VertxOptions vertxOptions = new VertxOptions().setPreferNativeTransport(true);
        vertx = Vertx.vertx(vertxOptions);

        String socketPath = awaitResult(PodmanHelpers.podmanSocketPath(vertx));
        PodmanClient.Options options = new PodmanClient.Options().setSocketPath(socketPath);
        client = PodmanClient.create(vertx, options);
    }

    @AfterAll
    void cleanup() throws Throwable {
        awaitResult(client.close());
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
        await().untilAsserted(() -> awaitResult(client.secrets().exists("yolo")));
        assertThat(awaitResult(client.secrets().exists("foo"))).isFalse();
        awaitResult(client.secrets().remove("yolo"));
    }

    @Test
    @Order(4)
    void inspect() throws Throwable {
        awaitResult(client.secrets().create("yolo", "this", new SecretCreateOptions()));
        JsonObject data = awaitResult(client.secrets().inspect("yolo", true));
        System.out.println(data.encodePrettily());
        assertThat(data.containsKey("ID")).isTrue();
        assertThat(data.getString("SecretData")).isEqualTo("this");
        data = awaitResult(client.secrets().inspect("yolo", false));
        assertThat(data.containsKey("SecretData")).isFalse();
        awaitResult(client.secrets().remove("yolo"));
    }

    @Test
    @Order(5)
    void labels() throws Throwable {
        awaitResult(client.secrets()
                .create("foo", "bar", new SecretCreateOptions().label("a", "1").label("b", "2")));
        JsonObject data = awaitResult(client.secrets().inspect("foo", true));
        JsonObject labelsData = (JsonObject) JsonPointer.from("/Spec/Labels").queryJson(data);
        assertThat(labelsData.getString("a")).isEqualTo("1");
        assertThat(labelsData.getString("b")).isEqualTo("2");
        awaitResult(client.secrets().remove("foo"));
    }

    @Test
    @Order(6)
    void list() throws Throwable {
        awaitResult(client.secrets().create("foo", "bar", new SecretCreateOptions()));
        JsonArray secrets = awaitResult(client.secrets().list(new JsonFilters().put("name", "foo")));
        assertThat(secrets.size()).isEqualTo(1);
        secrets = awaitResult(client.secrets().list(new JsonFilters().put("name", "_foo_")));
        assertThat(secrets).isEmpty();
        secrets = awaitResult(client.secrets().list(new JsonFilters()));
        assertThat(secrets).hasSizeGreaterThanOrEqualTo(1);
        awaitResult(client.secrets().remove("foo"));
    }
}
