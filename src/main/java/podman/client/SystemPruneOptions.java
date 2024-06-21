package podman.client;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import podman.internal.JsonLabels;

public class SystemPruneOptions {

    private boolean all;
    private boolean volumes;
    private boolean external;
    private final JsonLabels filters = new JsonLabels();

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
        return filters.labels();
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

    public <T> HttpRequest<T> fillQueryParams(HttpRequest<T> request) {
        return request.addQueryParam("all", String.valueOf(all()))
                .addQueryParam("volumes", String.valueOf(volumes()))
                .addQueryParam("external", String.valueOf(external()))
                .addQueryParam("filters", filters().encode());
    }
}
