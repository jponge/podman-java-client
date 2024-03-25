package podman.machine;

import io.vertx.core.Vertx;

public interface PodmanClient {

    static PodmanClient create(Vertx vertx) {
        throw new UnsupportedOperationException("To be implemented");
    }
}
