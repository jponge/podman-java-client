package podman.internal;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.net.SocketAddress;
import podman.client.PodmanClient;

public record ClientContext(
        Vertx vertx, PodmanClient.Options options, SocketAddress socketAddress, HttpClient httpClient) {}
