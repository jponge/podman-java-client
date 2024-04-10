package podman.internal;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class JsonFilters {

    private final JsonObject filters = new JsonObject();

    public void filter(String key, String value) {
        JsonArray values;
        if (!filters.containsKey(key)) {
            values = new JsonArray();
            filters.put(key, values);
        } else {
            values = filters.getJsonArray(key);
        }
        values.add(value);
    }

    public JsonObject filters() {
        return filters;
    }
}
