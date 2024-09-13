package podman.client;

import static helpers.AsyncTestHelpers.awaitResult;
import static org.assertj.core.api.Assertions.assertThat;

import helpers.PodmanHelpers;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import podman.client.volumes.VolumeCreateOptions;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
public class PodmanClientVolumesTest {

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
    void createCheckInspectRemove() throws Throwable {
        JsonObject volume = awaitResult(client.volumes().create(new VolumeCreateOptions()));
        assertThat(volume.containsKey("Name")).isTrue();
        String name = volume.getString("Name");

        assertThat(awaitResult(client.volumes().exists(name))).isTrue();

        JsonObject inspection = awaitResult(client.volumes().inspect(name));
        assertThat(inspection.containsKey("Name")).isTrue();
        assertThat(inspection.getString("Name")).isEqualTo(name);

        awaitResult(client.volumes().remove(name, true));
        assertThat(awaitResult(client.volumes().exists(name))).isFalse();
    }

    @Test
    void listAndPrune() throws Throwable {
        JsonObject volume = awaitResult(client.volumes().create(new VolumeCreateOptions()));
        assertThat(volume.containsKey("Name")).isTrue();
        String name = volume.getString("Name");

        JsonArray volumes = awaitResult(client.volumes().list(new JsonFilters()));
        assertThat(volumes).isNotEmpty();
        assertThat(volumes).anyMatch(entry -> {
            JsonObject vol = (JsonObject) entry;
            return vol.getString("Name").equals(name);
        });

        JsonArray pruned = awaitResult(client.volumes().prune(new JsonFilters()));
        assertThat(pruned).isNotEmpty();

        volumes = awaitResult(client.volumes().list(new JsonFilters()));
        assertThat(volumes).noneMatch(entry -> {
            JsonObject vol = (JsonObject) entry;
            return vol.getString("Name").equals(name);
        });
    }
}
