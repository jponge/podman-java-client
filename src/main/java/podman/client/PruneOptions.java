package podman.client;

import io.vertx.core.json.JsonObject;
import podman.internal.JsonFilters;

public class PruneOptions {

    private boolean all;
    private boolean volumes;
    private boolean external;
    private final JsonFilters filters = new JsonFilters();

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
        return filters.filters();
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
        filters.filter(key, value);
        return this;
    }
}
