package podman.client.images;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.JsonEvent;
import io.vertx.core.streams.ReadStream;

public interface ImagesGroup {

    Future<ReadStream<JsonEvent>> pull(String reference, ImagePullOptions imagePullOptions);
}
