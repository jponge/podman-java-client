package podman.machine.machine;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class ConcretePodmanMachineClient implements PodmanMachineClient {

    private final Vertx vertx;
    private final Options options;

    public ConcretePodmanMachineClient(Vertx vertx, Options options) {
        this.vertx = vertx;
        this.options = options;
    }

    @Override
    public Future<PodmanMachineInfoResult> info() {
        return vertx.executeBlocking(() -> {
            JsonObject data = (JsonObject) run("podman", "machine", "info", "--format", "json");
            return new PodmanMachineInfoResult(data);
        });
    }

    @Override
    public Future<List<PodmanMachineListResult>> list() {
        return vertx.executeBlocking(() -> {
            JsonArray data = (JsonArray) run("podman", "machine", "list", "--format", "json");
            int length = data.size();
            ArrayList<PodmanMachineListResult> items = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                items.add(new PodmanMachineListResult(data.getJsonObject(i)));
            }
            return items;
        });
    }

    @Override
    public Future<PodmanMachineInspectResult> inspect(String name) {
        return vertx.executeBlocking(() -> {
            JsonArray data = (JsonArray) run("podman", "machine", "inspect", name);
            if (data.size() != 1) {
                throw new IllegalStateException("Expected an array of size 1 instead of " + data.size());
            }
            return new PodmanMachineInspectResult(data.getJsonObject(0));
        });
    }

    private Object run(String... command) throws IOException, InterruptedException, TimeoutException {
        Path tempFile = Files.createTempFile("podman-machine", "config");
        Process process = new ProcessBuilder(command)
                .redirectOutput(tempFile.toFile())
                .redirectErrorStream(true)
                .start();
        if (!process.waitFor(options.getTimeout().toMillis(), TimeUnit.MILLISECONDS)) {
            tempFile.toFile().delete();
            throw new TimeoutException("Failed to complete operation within 30 seconds");
        }
        String payload = Files.readString(tempFile);
        tempFile.toFile().delete();
        int exitCode = process.exitValue();
        if (exitCode == 0) {
            return Json.decodeValue(payload);
        } else {
            throw new IOException("Failed with exit code " + exitCode + "\n" + payload);
        }
    }
}
