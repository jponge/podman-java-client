package podman.client;

import io.vertx.core.Vertx;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.web.client.WebClient;

record ClientState(Vertx vertx, PodmanClient.Options options, SocketAddress socketAddress, WebClient webClient) {}
