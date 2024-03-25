package podman.machine.machine;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.time.Duration;

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

    Future<JsonObject> info();

    Future<JsonArray> list();

    Future<PodmanMachineInspectResult> inspect(String name);
}
