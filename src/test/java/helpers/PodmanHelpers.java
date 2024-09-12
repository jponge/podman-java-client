package helpers;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.json.pointer.JsonPointer;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.SystemUtils;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import podman.machine.PodmanMachineClient;

public interface PodmanHelpers {

    static Future<String> podmanSocketPath(Vertx vertx) {
        if (SystemUtils.IS_OS_LINUX) {
            return vertx.executeBlocking(() -> {
                try {
                    ProcessResult processResult = new ProcessExecutor("podman", "system", "info", "--format", "json")
                            .readOutput(true)
                            .redirectErrorStream(true)
                            .timeout(5, TimeUnit.SECONDS)
                            .execute();
                    String payload = processResult.outputUTF8();
                    if (processResult.getExitValue() == 0) {
                        JsonObject data = (JsonObject) Json.decodeValue(payload);
                        String path = (String)
                                JsonPointer.from("/host/remoteSocket/path").queryJson(data);
                        return path;
                    } else {
                        throw new IOException("Failed with exit code " + processResult.getExitValue() + "\n" + payload);
                    }
                } catch (Throwable err) {
                    throw err;
                }
            });
        } else {
            return PodmanMachineClient.create(vertx).findDefaultMachineConnectionSocketPath();
        }
    }
}
