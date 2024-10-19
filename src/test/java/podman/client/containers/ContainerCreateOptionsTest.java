package podman.client.containers;

import static org.assertj.core.api.Assertions.assertThat;

import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ContainerCreateOptionsTest {

    // Note: it's a (too) big class to test

    @Test
    void checkSomeEntries() {
        JsonObject json = new ContainerCreateOptions()
                .image("quay.io/podman/hello:latest")
                .imageArch("arm64")
                .imageOs("linux")
                .env(Map.of("FOO", "foo-bar", "BAR", "bar-baz"))
                .labels(Map.of("app", "demo"))
                .user("root")
                .command(List.of("/bin/sh", "-c", "echo hello"))
                .secrets(List.of(
                        new ContainerCreateOptions.Secret("secret1", "yolo"),
                        new ContainerCreateOptions.Secret("secret2", "1234")))
                .dnsServer(List.of("1.1.1.1", "8.8.8.8"))
                .remove(true)
                .json();

        assertThat(json.getString("image")).isEqualTo("quay.io/podman/hello:latest");
        assertThat(json.getString("image_arch")).isEqualTo("arm64");
        assertThat(json.getString("image_os")).isEqualTo("linux");
        assertThat(json.getJsonObject("env"))
                .isNotNull()
                .isNotEmpty()
                .contains(Map.entry("FOO", "foo-bar"), Map.entry("BAR", "bar-baz"));
        assertThat(json.getJsonObject("labels")).isNotNull().isNotEmpty().containsExactly(Map.entry("app", "demo"));
        assertThat(json.getString("user")).isEqualTo("root");
        assertThat(json.getJsonArray("command"))
                .isNotNull()
                .isNotEmpty()
                .containsExactly("/bin/sh", "-c", "echo hello");
        assertThat(json.getJsonArray("secrets"))
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .anyMatch(o -> {
                    JsonObject secret = (JsonObject) o;
                    return secret.getString("Key").equals("secret1")
                            && secret.getString("Secret").equals("yolo");
                });
        assertThat(json.getJsonArray("dns_server")).isNotNull().isNotEmpty().containsAll(List.of("1.1.1.1", "8.8.8.8"));
        assertThat(json.getBoolean("remove")).isTrue();
    }

    @Test
    void checkImageVolumes() {
        JsonObject json = new ContainerCreateOptions()
                .imageVolumes(List.of(
                        new ContainerCreateOptions.ImageVolume("dest1", true, "source1", ""),
                        new ContainerCreateOptions.ImageVolume("dest2", false, "source2", "sub")))
                .json();

        assertThat(json.getJsonArray("image_volumes"))
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .anyMatch(o -> {
                    JsonObject imageVolume = (JsonObject) o;
                    return imageVolume.getString("Destination").equals("dest1")
                            && imageVolume.getBoolean("ReadWrite").equals(true)
                            && imageVolume.getString("Source").equals("source1")
                            && imageVolume.getString("subPath").equals("");
                });
    }
}
