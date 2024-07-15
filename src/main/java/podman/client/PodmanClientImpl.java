package podman.client;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.web.client.WebClient;
import podman.client.secrets.SecretsGroup;
import podman.client.secrets.SecretsGroupImpl;
import podman.client.system.SystemGroup;
import podman.client.system.SystemGroupImpl;
import podman.client.volumes.VolumesGroup;
import podman.client.volumes.VolumesGroupImpl;
import podman.internal.ClientState;

class PodmanClientImpl implements PodmanClient {

    private final ClientState state;

    public PodmanClientImpl(Vertx vertx, Options options) {
        SocketAddress socketAddress = SocketAddress.domainSocketAddress(options.getSocketPath());
        HttpClient httpClient = vertx.createHttpClient(
                new HttpClientOptions().setDefaultHost("localhost").setDefaultPort(9999));
        WebClient webClient = WebClient.wrap(httpClient);
        this.state = new ClientState(vertx, options, socketAddress, httpClient, webClient);
    }

    @Override
    public void close() {
        state.webClient().close();
    }

    @Override
    public SystemGroup system() {
        return new SystemGroupImpl(state);
    }

    @Override
    public SecretsGroup secrets() {
        return new SecretsGroupImpl(state);
    }

    @Override
    public VolumesGroup volumes() {
        return new VolumesGroupImpl(state);
    }
}
