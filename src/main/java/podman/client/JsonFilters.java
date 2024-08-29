package podman.client;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class JsonFilters {

    private final JsonObject filters = new JsonObject();

    public JsonFilters put(String key, String value) {
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

    public JsonObject json() {
        return filters;
    }
}
