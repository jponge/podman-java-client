package podman.client;

import static helpers.AsyncTestHelpers.awaitResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.parsetools.JsonEvent;
import io.vertx.core.streams.ReadStream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import podman.client.system.SystemGetEventsOptions;
import podman.client.system.SystemPruneOptions;
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

    @Test
    void prune() throws Throwable {
        SystemPruneOptions pruneOptions = new SystemPruneOptions()
                .setAll(true)
                .setVolumes(true)
                .filter("label", "foo")
                .filter("label", "bar");
        JsonObject data = awaitResult(client.system().prune(pruneOptions));
        assertThat(data.size()).isPositive();
        assertThat(data.containsKey("ReclaimedSpace")).isTrue();
        assertThat(data.containsKey("ImagePruneReports")).isTrue();
    }

    @Test
    void pruneBadFilter() {
        SystemPruneOptions pruneOptions = new SystemPruneOptions()
                .setAll(true)
                .setVolumes(true)
                .filter("foo", "bar")
                .filter("label", "bar");
        RequestException err = assertThrows(
                RequestException.class, () -> awaitResult(client.system().prune(pruneOptions)));
        assertThat(err.statusCode()).isEqualTo(500);
        assertThat(err.payload()).contains("\"message\":\"foo is an invalid filter\"");
    }

    @Test
    @Disabled
    void getEvents() throws Throwable {
        SystemGetEventsOptions getEventsOptions = new SystemGetEventsOptions();
        ReadStream<JsonEvent> stream = awaitResult(client.system().getEvents(getEventsOptions));
        stream.handler(event -> {
            System.out.println(event.type());
            System.out.println(event.objectValue().encodePrettily());
        });
        // TODO revisit to check for events when we have image creation / pull APIs
        //        awaitResult(client.system().prune(new SystemPruneOptions()));
        //        awaitResult(client.system().ping());
        //        awaitResult(client.system().df());
        //        Thread.sleep(10_000);
    }
}
