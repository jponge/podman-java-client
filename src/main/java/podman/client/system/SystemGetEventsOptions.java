package podman.client.system;

import io.vertx.core.json.JsonObject;
import io.vertx.uritemplate.Variables;
import podman.client.JsonFilters;

public class SystemGetEventsOptions {

    private final JsonFilters filters = new JsonFilters();
    private String since;
    private boolean stream = true;
    private String until;

    public String since() {
        return since;
    }

    public SystemGetEventsOptions setSince(String since) {
        this.since = since;
        return this;
    }

    public boolean stream() {
        return stream;
    }

    public SystemGetEventsOptions setStream(boolean stream) {
        this.stream = stream;
        return this;
    }

    public String until() {
        return until;
    }

    public SystemGetEventsOptions setUntil(String until) {
        this.until = until;
        return this;
    }

    public SystemGetEventsOptions filter(String key, String value) {
        filters.put(key, value);
        return this;
    }

    public JsonObject filters() {
        return filters.json();
    }

    public Variables fillQueryParams(Variables vars) {
        vars.set("filters", filters().encode()).set("stream", String.valueOf(stream));
        if (since != null) {
            vars.set("since", since);
        }
        if (until != null) {
            vars.set("until", until);
        }
        return vars;
    }
}
