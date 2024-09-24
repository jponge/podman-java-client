package helpers;

import static org.awaitility.Awaitility.await;

import io.vertx.core.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface TestHelpers {

    Logger LOGGER = Logger.getLogger(TestHelpers.class.getName());

    static <T> T awaitResult(Future<T> future) throws Throwable {
        AtomicBoolean done = new AtomicBoolean();
        AtomicReference<T> result = new AtomicReference<>();
        AtomicReference<Throwable> failure = new AtomicReference<>();
        future.onComplete(res -> {
            if (res.succeeded()) {
                result.set(res.result());
            } else {
                failure.set(res.cause());
            }
            done.set(true);
        });
        await().untilTrue(done);
        if (failure.get() != null) {
            throw failure.get();
        } else {
            return result.get();
        }
    }

    static String podmanSocketPath() {
        // TODO: it remains a bit brittle, perhaps we should assume podman-machine on Windows/macOS, and what is below
        String path = System.getenv("PODMAN_SOCKET_PATH");
        if (path == null) {
            path = "/var/run/docker.sock";
            LOGGER.log(
                    Level.WARNING,
                    "PODMAN_SOCKET_PATH not set, defaulting to /var/run/docker.sock to run tests but this might not be what you want...");
        }
        return path;
    }
}
