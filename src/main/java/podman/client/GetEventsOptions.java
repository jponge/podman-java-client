package podman.client;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import podman.internal.JsonFilters;

public class GetEventsOptions {

    private final JsonFilters filters = new JsonFilters();
    private String since;
    private boolean stream = true;
    private String until;

    public String since() {
        return since;
    }

    public GetEventsOptions setSince(String since) {
        this.since = since;
        return this;
    }

    public boolean stream() {
        return stream;
    }

    public GetEventsOptions setStream(boolean stream) {
        this.stream = stream;
        return this;
    }

    public String until() {
        return until;
    }

    public GetEventsOptions setUntil(String until) {
        this.until = until;
        return this;
    }

    public GetEventsOptions filter(String key, String value) {
        filters.filter(key, value);
        return this;
    }

    public JsonObject filters() {
        return filters.filters();
    }

    public <T> HttpRequest<T> fillQueryParams(HttpRequest<T> request) {
        request.addQueryParam("filters", filters().encode());
        request.addQueryParam("stream", String.valueOf(stream));
        if (since != null) {
            request.addQueryParam("since", since);
        }
        if (until != null) {
            request.addQueryParam("until", until);
        }
        return request;
    }
}
