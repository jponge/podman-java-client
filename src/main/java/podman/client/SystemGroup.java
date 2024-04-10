package podman.client;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface SystemGroup {

    Future<JsonObject> version();

    Future<JsonObject> info();

    Future<JsonObject> df();

    Future<JsonObject> ping();

    // TODO events (stream)

    // TODO prune
}
