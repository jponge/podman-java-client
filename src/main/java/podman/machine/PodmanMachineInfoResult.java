package podman.machine;

import io.vertx.core.json.JsonObject;

public class PodmanMachineInfoResult {

    private final JsonObject payload;

    public PodmanMachineInfoResult(JsonObject payload) {
        this.payload = payload;
    }

    public JsonObject payload() {
        return payload;
    }

    public Host host() {
        return new Host(payload.getJsonObject("Host"));
    }

    public static class Host {

        private final JsonObject payload;

        public Host(JsonObject payload) {
            this.payload = payload;
        }

        public String arch() {
            return payload.getString("Arch");
        }

        public String currentMachine() {
            return payload.getString("CurrentMachine");
        }

        public String defaultMachine() {
            return payload.getString("DefaultMachine");
        }

        public String eventsDir() {
            return payload.getString("EventsDir");
        }

        public String machineConfigDir() {
            return payload.getString("MachineConfigDir");
        }

        public String machineImageDir() {
            return payload.getString("MachineImageDir");
        }

        public String machineState() {
            return payload.getString("MachineState");
        }

        public int numberOfMachines() {
            return payload.getInteger("NumberOfMachines");
        }

        public String os() {
            return payload.getString("OS");
        }

        public String vmType() {
            return payload.getString("VMType");
        }
    }

    public Version version() {
        return new Version(payload.getJsonObject("Version"));
    }

    public static class Version {

        private final JsonObject payload;

        public Version(JsonObject payload) {
            this.payload = payload;
        }

        public String apiVersion() {
            return payload.getString("APIVersion");
        }

        public String version() {
            return payload.getString("Version");
        }

        public String goVersion() {
            return payload.getString("GoVersion");
        }

        public String gitCommit() {
            return payload.getString("GitCommit");
        }

        public String builtTime() {
            return payload.getString("BuiltTime");
        }

        public long built() {
            return payload.getLong("Built");
        }

        public String osArch() {
            return payload.getString("OsArch");
        }

        public String os() {
            return payload.getString("Os");
        }
    }
}
