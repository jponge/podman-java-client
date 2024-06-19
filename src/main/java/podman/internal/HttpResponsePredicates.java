package podman.internal;

import io.vertx.core.Expectation;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import podman.client.RequestException;

public interface HttpResponsePredicates {

    static Expectation<? super HttpResponse<JsonObject>> statusCode(int statusCode) {
        return new Expectation<HttpResponse<JsonObject>>() {
            @Override
            public boolean test(HttpResponse<JsonObject> response) {
                return response.statusCode() == statusCode;
            }
        }.wrappingFailure((response, err) -> {
            int status = response.statusCode();
            if (response.getHeader("content-type").equals("application/json")) {
                return new RequestException(status, response.body().encode());
            } else {
                return new RequestException(status, "Status code " + response.statusCode() + " is not " + statusCode);
            }
        });
    }
}
