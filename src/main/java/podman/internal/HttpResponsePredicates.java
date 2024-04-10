package podman.internal;

import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.predicate.ErrorConverter;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.client.predicate.ResponsePredicateResult;
import podman.client.RequestException;

public interface HttpResponsePredicates {

    static ResponsePredicate statusCode(int statusCode) {
        ErrorConverter errorConverter = ErrorConverter.createFullBody(result -> {
            HttpResponse<Buffer> response = result.response();
            int status = response.statusCode();
            if (response.getHeader("content-type").equals("application/json")) {
                return new RequestException(status, response.bodyAsString());
            } else {
                return new RequestException(status, result.message());
            }
        });
        return ResponsePredicate.create(
                response -> {
                    if (response.statusCode() == statusCode) {
                        return ResponsePredicateResult.success();
                    } else {
                        return ResponsePredicateResult.failure(
                                "Status code " + response.statusCode() + " is not " + statusCode);
                    }
                },
                errorConverter);
    }
}
