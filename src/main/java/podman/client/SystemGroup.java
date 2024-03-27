package podman.client;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface SystemGroup {

    Future<JsonObject> version();

    Future<JsonObject> info();
}
