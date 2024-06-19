package podman.internal;

import io.vertx.core.http.HttpResponseHead;
import io.vertx.ext.web.client.HttpResponse;
import java.util.function.BiFunction;
import podman.client.RequestException;

public interface HttpPredicates {

    static BiFunction<HttpResponseHead, Throwable, Throwable> requestException() {
        return (resp, err) -> {
            HttpResponse<?> response = (HttpResponse<?>) resp;
            return new RequestException(
                    response.statusCode(), response.getHeader("Content-Type"), response.bodyAsString());
        };
    }
}
