package podman.machine.machine;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import podman.machine.machine.PodmanMachineClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    public Future<JsonObject> info() {
        return vertx.executeBlocking(() -> (JsonObject) run("podman", "machine", "info", "--format", "json"));
    }

    @Override
    public Future<JsonArray> list() {
        return vertx.executeBlocking(() -> (JsonArray) run("podman", "machine", "list", "--format", "json"));
    }

    @Override
    public Future<JsonArray> inspect(String name) {
        return vertx.executeBlocking(() -> (JsonArray) run("podman", "machine", "inspect", name));
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
