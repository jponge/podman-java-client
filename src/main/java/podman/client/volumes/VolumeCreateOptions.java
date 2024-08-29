package podman.client.volumes;

import io.vertx.core.json.JsonObject;
import java.util.HashMap;

public class VolumeCreateOptions {

    private String name = "";
    private String driver = "local";
    private boolean ignoreIfExists;
    private final HashMap<String, String> labels = new HashMap<>();
    private final HashMap<String, String> options = new HashMap<>();

    public String name() {
        return name;
    }

    public String driver() {
        return driver;
    }

    public boolean ignoreIfExists() {
        return ignoreIfExists;
    }

    public HashMap<String, String> labels() {
        return labels;
    }

    public HashMap<String, String> options() {
        return options;
    }

    public VolumeCreateOptions setName(String name) {
        this.name = name;
        return this;
    }

    public VolumeCreateOptions setDriver(String driver) {
        this.driver = driver;
        return this;
    }

    public VolumeCreateOptions setIgnoreIfExists(boolean ignoreIfExists) {
        this.ignoreIfExists = ignoreIfExists;
        return this;
    }

    public VolumeCreateOptions label(String key, String value) {
        labels.put(key, value);
        return this;
    }

    public VolumeCreateOptions option(String key, String value) {
        labels.put(key, value);
        return this;
    }

    public JsonObject json() {
        return new JsonObject()
                .put("Name", name)
                .put("Driver", driver)
                .put("IgnoreIfExists", ignoreIfExists)
                .put("Labels", labels)
                .put("Options", options);
    }
}
