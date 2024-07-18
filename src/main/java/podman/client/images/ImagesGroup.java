package podman.client.images;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import java.util.concurrent.Flow;

public interface ImagesGroup {

    Flow.Publisher<JsonObject> pull(String reference, ImagePullOptions imagePullOptions);

    Future<Boolean> exists(String name);
}
