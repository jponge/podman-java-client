package podman.client.system;

import io.vertx.core.json.JsonObject;
import io.vertx.uritemplate.Variables;
import podman.client.JsonFilters;

public class SystemPruneOptions {

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
        return filters.json();
    }

    public SystemPruneOptions setAll(boolean all) {
        this.all = all;
        return this;
    }

    public SystemPruneOptions setVolumes(boolean volumes) {
        this.volumes = volumes;
        return this;
    }

    public SystemPruneOptions setExternal(boolean external) {
        this.external = external;
        return this;
    }

    public SystemPruneOptions filter(String key, String value) {
        filters.put(key, value);
        return this;
    }

    public Variables fillQueryParams(Variables vars) {
        return vars.set("all", String.valueOf(all()))
                .set("volumes", String.valueOf(volumes()))
                .set("external", String.valueOf(external()))
                .set("filters", filters().encode());
    }
}
