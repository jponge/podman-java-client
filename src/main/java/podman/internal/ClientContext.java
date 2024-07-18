package podman.internal;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.net.SocketAddress;
import podman.client.PodmanClient;

import static io.vertx.core.Future.failedFuture;

public record ClientContext(
        Vertx vertx, PodmanClient.Options options, SocketAddress socketAddress, HttpClient httpClient) {

    public boolean isCalledFromEventLoop() {
        Context context = Vertx.currentContext();
        return (context != null) && context.isEventLoopContext();
    }

    public <T> Future<T> accessingStreamOutsideEventLoop() {
        return failedFuture("Stream providing methods must be called from a Vert.x event-loop thread (was called from " + Thread.currentThread().getName() + ")");
    }
}
