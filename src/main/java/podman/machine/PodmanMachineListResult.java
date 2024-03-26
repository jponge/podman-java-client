package podman.machine;

import io.vertx.core.json.JsonObject;

public class PodmanMachineListResult {

    private final JsonObject payload;

    public PodmanMachineListResult(JsonObject payload) {
        this.payload = payload;
    }

    public JsonObject payload() {
        return payload;
    }

    public String name() {
        return payload.getString("Name");
    }

    public boolean isDefault() {
        return payload.getBoolean("Default");
    }

    public String created() {
        return payload.getString("Created");
    }

    public boolean isRunning() {
        return payload.getBoolean("Running");
    }

    public boolean isStarting() {
        return payload.getBoolean("Starting");
    }

    public String lastUp() {
        return payload.getString("LastUp");
    }

    public String stream() {
        return payload.getString("Stream");
    }

    public String vmType() {
        return payload.getString("VMType");
    }

    public int cpus() {
        return payload.getInteger("CPUs");
    }

    public String memory() {
        return payload.getString("Memory");
    }

    public String diskSize() {
        return payload.getString("DiskSize");
    }

    public int port() {
        return payload.getInteger("Port");
    }

    public String remoteUsername() {
        return payload.getString("RemoteUsername");
    }

    public String identityPath() {
        return payload.getString("IdentityPath");
    }

    public boolean userModeNetworking() {
        return payload.getBoolean("UserModeNetworking");
    }
}
