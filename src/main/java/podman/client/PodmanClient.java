package podman.client;

import io.vertx.core.Vertx;

public interface PodmanClient {

    class Options {

        private String socketPath = "";
        private String apiVersion = "v5.0.0";
        private String versionedBasePath = "/" + apiVersion + "/";

        public String getSocketPath() {
            return socketPath;
        }

        public Options setSocketPath(String socketPath) {
            this.socketPath = socketPath;
            return this;
        }

        public String getApiVersion() {
            return apiVersion;
        }

        public Options setApiVersion(String apiVersion) {
            this.apiVersion = apiVersion;
            this.versionedBasePath = "/" + apiVersion + "/";
            return this;
        }

        public String getVersionedBasePath() {
            return versionedBasePath;
        }
    }

    static PodmanClient create(Vertx vertx, Options options) {
        return new PodmanClientImpl(vertx, options);
    }

    void close();

    SystemGroup system();

    SecretsGroup secrets();
}
