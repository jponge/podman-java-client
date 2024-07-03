package podman.client.volumes;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import podman.client.JsonFilters;

public interface VolumesGroup {

    Future<JsonObject> create(VolumeCreateOptions options);

    Future<JsonObject> inspect(String name);

    Future<JsonArray> list(JsonFilters filters);

    Future<JsonArray> prune(JsonFilters filters);

    Future<Void> remove(String name, boolean force);

    Future<Boolean> exists(String name);
}
