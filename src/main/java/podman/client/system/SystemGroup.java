package podman.client.system;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.parsetools.JsonEvent;
import io.vertx.core.streams.ReadStream;

public interface SystemGroup {

    Future<JsonObject> version();

    Future<JsonObject> info();

    Future<JsonObject> df();

    Future<JsonObject> ping();

    Future<JsonObject> prune(SystemPruneOptions pruneOptions);

    Future<JsonObject> check(SystemCheckOptions checkOptions);

    ReadStream<JsonEvent> getEvents(SystemGetEventsOptions getEventsOptions);
}