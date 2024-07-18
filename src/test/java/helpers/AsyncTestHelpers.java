package helpers;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.streams.ReadStream;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;

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

    static <T> void awaitStream(ReadStream<T> stream, Duration timeout, Handler<T> handler) throws Throwable {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> error = new AtomicReference<>();
        try {
            stream.exceptionHandler(err -> {
                error.set(err);
                latch.countDown();
            });
            stream.endHandler(done -> latch.countDown());
            stream.handler(handler);
            boolean earlyFinish = latch.await(timeout.toMillis(), TimeUnit.MILLISECONDS);
            Throwable throwable = error.get();
            if (throwable != null) {
                throw throwable;
            }
            if (!earlyFinish) {
                throw new TimeoutException("Timed out after " + timeout.toMillis() + " ms");
            }
        } catch (Throwable t) {
            throw t;
        }
    }
}
