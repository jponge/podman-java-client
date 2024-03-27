package podman.client;

import io.vertx.core.Vertx;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

class PodmanClientImpl implements PodmanClient {

    private final ClientState state;

    public PodmanClientImpl(Vertx vertx, Options options) {
        SocketAddress socketAddress = SocketAddress.domainSocketAddress(options.getSocketPath());
        WebClientOptions clientOptions = new WebClientOptions()
                .setDefaultHost("localhost")
                .setDefaultPort(9999);
        WebClient webClient = WebClient.create(vertx, clientOptions);
        this.state = new ClientState(vertx, options, socketAddress, webClient);
    }

    @Override
    public void close() {
        state.webClient().close();
    }

    @Override
    public SystemGroup system() {
        return new SystemGroupImpl(state);
    }
}
