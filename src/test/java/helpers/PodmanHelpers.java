package helpers;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.apache.commons.lang3.SystemUtils;
import podman.machine.PodmanMachineClient;

public interface PodmanHelpers {

    static Future<String> podmanSocketPath(Vertx vertx) {
        if (SystemUtils.IS_OS_LINUX) {
            return Future.succeededFuture("/var/run/podman/podman.sock");
        } else {
            return PodmanMachineClient.create(vertx).findDefaultMachineConnectionSocketPath();
        }
    }
}
