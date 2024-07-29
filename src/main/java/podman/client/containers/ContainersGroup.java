package podman.client.containers;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface ContainersGroup {

    Future<JsonObject> create(ContainerCreateOptions containerCreateOptions);
}
