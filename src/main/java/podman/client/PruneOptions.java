package podman.client;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class PruneOptions {

    private boolean all;
    private boolean volumes;
    private boolean external;
    private final JsonObject filters = new JsonObject();

    public boolean all() {
        return all;
    }

    public boolean volumes() {
        return volumes;
    }

    public boolean external() {
        return external;
    }

    public JsonObject filters() {
        return filters;
    }

    public PruneOptions setAll(boolean all) {
        this.all = all;
        return this;
    }

    public PruneOptions setVolumes(boolean volumes) {
        this.volumes = volumes;
        return this;
    }

    public PruneOptions setExternal(boolean external) {
        this.external = external;
        return this;
    }

    public PruneOptions filter(String key, String value) {
        JsonArray values;
        if (!filters.containsKey(key)) {
            values = new JsonArray();
            filters.put(key, values);
        } else {
            values = filters.getJsonArray(key);
        }
        values.add(value);
        return this;
    }
}
