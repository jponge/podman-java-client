package podman.client.containers;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface ContainersGroup {

    Future<JsonObject> create(ContainerCreateOptions options);

    Future<JsonArray> delete(String name, ContainerDeleteOptions options);

    Future<Boolean> exists(String name);
}
