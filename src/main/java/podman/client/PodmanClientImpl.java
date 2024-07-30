package podman.client;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.net.SocketAddress;
import podman.client.containers.ContainersGroup;
import podman.client.containers.ContainersGroupImpl;
import podman.client.images.ImagesGroup;
import podman.client.images.ImagesGroupImpl;
import podman.client.secrets.SecretsGroup;
import podman.client.secrets.SecretsGroupImpl;
import podman.client.system.SystemGroup;
import podman.client.system.SystemGroupImpl;
import podman.client.volumes.VolumesGroup;
import podman.client.volumes.VolumesGroupImpl;
import podman.internal.ClientContext;

class PodmanClientImpl implements PodmanClient {

    private final ClientContext context;

    private VolumesGroup volumesGroup;
    private ImagesGroup imagesGroup;
    private ContainersGroup containersGroup;
    private SystemGroup systemGroup;
    private SecretsGroup secretsGroup;

    public PodmanClientImpl(Vertx vertx, Options options) {
        SocketAddress socketAddress = SocketAddress.domainSocketAddress(options.getSocketPath());
        HttpClient httpClient = vertx.createHttpClient(
                new HttpClientOptions().setDefaultHost("localhost").setDefaultPort(9999));
        this.context = new ClientContext(vertx, options, socketAddress, httpClient);
    }

    @Override
    public Future<Void> close() {
        return context.httpClient().close();
    }

    @Override
    public SystemGroup system() {
        if (systemGroup == null) {
            systemGroup = new SystemGroupImpl(context);
        }
        return systemGroup;
    }

    @Override
    public SecretsGroup secrets() {
        if (secretsGroup == null) {
            secretsGroup = new SecretsGroupImpl(context);
        }
        return secretsGroup;
    }

    @Override
    public VolumesGroup volumes() {
        if (volumesGroup == null) {
            volumesGroup = new VolumesGroupImpl(context);
        }
        return volumesGroup;
    }

    @Override
    public ImagesGroup images() {
        if (imagesGroup == null) {
            imagesGroup = new ImagesGroupImpl(context);
        }
        return imagesGroup;
    }

    @Override
    public ContainersGroup containers() {
        if (containersGroup == null) {
            containersGroup = new ContainersGroupImpl(context);
        }
        return containersGroup;
    }
}
