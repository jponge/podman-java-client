package podman.internal;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class JsonLabels {

    private final JsonObject labels = new JsonObject();

    public void put(String key, String value) {
        JsonArray values;
        if (!labels.containsKey(key)) {
            values = new JsonArray();
            labels.put(key, values);
        } else {
            values = labels.getJsonArray(key);
        }
        values.add(value);
    }

    public JsonObject labels() {
        return labels;
    }
}
