package podman.client.system;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import java.util.concurrent.Flow;

public interface SystemGroup {

    Future<JsonObject> version();

    Future<JsonObject> info();

    Future<JsonObject> df();

    Future<JsonObject> ping();

    Future<JsonObject> prune(SystemPruneOptions pruneOptions);

    Flow.Publisher<JsonObject> getEvents(SystemGetEventsOptions getEventsOptions);
}
