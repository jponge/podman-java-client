package podman.machine;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

class PodmanMachineClientImpl implements PodmanMachineClient {

    private final Vertx vertx;
    private final Options options;

    public PodmanMachineClientImpl(Vertx vertx, Options options) {
        this.vertx = vertx;
        this.options = options;
    }

    @Override
    public Future<PodmanMachineInfoResult> info() {
        return vertx.executeBlocking(() -> {
            JsonObject data = (JsonObject) runJson("podman", "machine", "info", "--format", "json");
            return new PodmanMachineInfoResult(data);
        });
    }

    @Override
    public Future<List<PodmanMachineListResult>> list() {
        return vertx.executeBlocking(() -> {
            JsonArray data = (JsonArray) runJson("podman", "machine", "list", "--format", "json");
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
            JsonArray data = (JsonArray) runJson("podman", "machine", "inspect", name);
            if (data.size() != 1) {
                throw new IllegalStateException("Expected an array of size 1 instead of " + data.size());
            }
            return new PodmanMachineInspectResult(data.getJsonObject(0));
        });
    }

    @Override
    public Future<String> findDefaultMachineConnectionSocketPath() {
        return inspect("podman-machine-default").map(PodmanMachineInspectResult::connectionSocketPath);
    }

    private Object runJson(String... command) throws IOException, InterruptedException, TimeoutException {
        ProcessResult processResult = new ProcessExecutor(command)
                .readOutput(true)
                .redirectErrorStream(true)
                .timeout(options.getTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .execute();
        String payload = processResult.outputUTF8();
        if (processResult.getExitValue() == 0) {
            return Json.decodeValue(payload);
        } else {
            throw new IOException("Failed with exit code " + processResult.getExitValue() + "\n" + payload);
        }
    }
}
