package podman.client;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.parsetools.JsonEvent;
import io.vertx.core.streams.ReadStream;

public interface SystemGroup {

    Future<JsonObject> version();

    Future<JsonObject> info();

    Future<JsonObject> df();

    Future<JsonObject> ping();

    Future<JsonObject> prune(PruneOptions pruneOptions);

    ReadStream<JsonEvent> getEvents(GetEventsOptions getEventsOptions);
}
