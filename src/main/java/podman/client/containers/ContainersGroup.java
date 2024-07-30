package podman.client.containers;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface ContainersGroup {

    Future<JsonObject> create(ContainerCreateOptions options);

    Future<JsonArray> delete(String name, ContainerDeleteOptions options);

    Future<Boolean> exists(String name);

    default Future<Void> start(String name) {
        return start(name, null);
    }

    Future<Void> start(String name, String detachKeys);

    Future<Void> pause(String name);

    Future<Void> kill(String name, String signal);

    Future<Void> stop(String name, boolean ignoreIfStopped, int timeout);
}
