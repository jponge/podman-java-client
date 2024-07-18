package podman.client.images;

import io.vertx.core.json.JsonObject;
import java.util.concurrent.Flow;

public interface ImagesGroup {

    Flow.Publisher<JsonObject> pull(String reference, ImagePullOptions imagePullOptions);
}
