package podman.machine.machine;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.pointer.JsonPointer;

public class PodmanMachineInspectResult {

    private final JsonObject payload;

    public PodmanMachineInspectResult(JsonObject payload) {
        this.payload = payload;
    }

    public JsonObject payload() {
        return payload;
    }

    public String name() {
        return payload.getString("Name");
    }

    public String state() {
        return payload.getString("State");
    }

    public boolean rootful() {
        return payload.getBoolean("Rootful");
    }

    public String connectionSocketPath() {
        JsonPointer pointer = JsonPointer.from("/ConnectionInfo/PodmanSocket/Path");
        return (String) pointer.queryJson(payload);
    }
}
