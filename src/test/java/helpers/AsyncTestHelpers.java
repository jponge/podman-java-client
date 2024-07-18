package helpers;

import static org.awaitility.Awaitility.await;

import io.vertx.core.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public interface AsyncTestHelpers {

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
}
