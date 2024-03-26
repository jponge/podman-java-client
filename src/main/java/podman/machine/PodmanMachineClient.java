package podman.machine;

import io.vertx.core.Future;
import io.vertx.core.Vertx;

import java.time.Duration;
import java.util.List;

public interface PodmanMachineClient {

    class Options {

        private Duration timeout = Duration.ofSeconds(30);

        public Duration getTimeout() {
            return timeout;
        }

        public Options setTimeout(Duration timeout) {
            if (timeout.isPositive()) {
                this.timeout = timeout;
            } else {
                throw new IllegalArgumentException("A timeout must be positive: " + timeout);
            }
            return this;
        }
    }

    static PodmanMachineClient create(Vertx vertx) {
        return new ConcretePodmanMachineClient(vertx, new Options());
    }

    static PodmanMachineClient create(Vertx vertx, Options options) {
        return new ConcretePodmanMachineClient(vertx, options);
    }

    Future<PodmanMachineInfoResult> info();

    Future<List<PodmanMachineListResult>> list();

    Future<PodmanMachineInspectResult> inspect(String name);

    Future<String> findDefaultMachineConnectionSocketPath();
}
