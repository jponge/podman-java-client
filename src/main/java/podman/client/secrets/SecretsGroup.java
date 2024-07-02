package podman.client.secrets;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import podman.client.JsonFilters;

public interface SecretsGroup {

    Future<JsonObject> create(String name, String data, SecretCreateOptions options);

    Future<Void> remove(String name);

    Future<Boolean> exists(String name);

    Future<JsonObject> inspect(String name, boolean showSecret);

    Future<JsonArray> list(JsonFilters filters);
}
