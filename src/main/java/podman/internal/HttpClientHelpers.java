package podman.internal;

import static io.vertx.core.Future.failedFuture;

import io.smallrye.common.vertx.VertxContext;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.RequestOptions;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import podman.client.RequestException;

public interface HttpClientHelpers {

    static <T> Future<T> makeRequest(
            Vertx vertx,
            HttpClient httpClient,
            RequestOptions options,
            Function<HttpClientResponse, Future<T>> handler) {
        return executeOnVertxContext(vertx, () -> httpClient
                .request(options)
                .compose(HttpClientRequest::send)
                .compose(handler, HttpClientHelpers::wrapFailure));
    }

    static <T> Future<T> makeRequestWithPayload(
            Vertx vertx,
            HttpClient httpClient,
            RequestOptions options,
            Buffer payload,
            Function<HttpClientResponse, Future<T>> handler) {
        return executeOnVertxContext(vertx, () -> httpClient
                .request(options)
                .compose(httpClientRequest -> httpClientRequest.send(payload))
                .compose(handler, HttpClientHelpers::wrapFailure));
    }

    static <T> Future<T> makeSimplifiedRequest(
            Vertx vertx,
            HttpClient httpClient,
            RequestOptions options,
            Predicate<HttpClientResponse> predicate,
            Function<HttpClientResponse, Future<T>> handler) {
        return makeRequest(vertx, httpClient, options, response -> {
            if (predicate.test(response)) {
                return handler.apply(response);
            } else {
                return consumeErrorResponseAndFail(response);
            }
        });
    }

    static <T> Future<T> makeSimplifiedRequestWithPayload(
            Vertx vertx,
            HttpClient httpClient,
            RequestOptions options,
            Buffer payload,
            Predicate<HttpClientResponse> predicate,
            Function<HttpClientResponse, Future<T>> handler) {
        return makeRequestWithPayload(vertx, httpClient, options, payload, response -> {
            if (predicate.test(response)) {
                return handler.apply(response);
            } else {
                return consumeErrorResponseAndFail(response);
            }
        });
    }

    static <T> Future<T> wrapFailure(Throwable err) {
        return failedFuture(new RequestException(err));
    }

    static <T> Future<T> consumeErrorResponseAndFail(HttpClientResponse response) {
        return response.body().compose(buffer -> {
            String contentType = response.getHeader("Content-Type");
            return failedFuture(new RequestException(response.statusCode(), contentType, buffer.toString()));
        });
    }

    static boolean statusCode(HttpClientResponse response, int expected) {
        return response.statusCode() == expected;
    }

    static boolean statusCode(HttpClientResponse response, int... allowed) {
        for (int status : allowed) {
            if (response.statusCode() == status) {
                return true;
            }
        }
        return false;
    }

    static <T> Future<T> executeOnVertxContext(Vertx vertx, Supplier<Future<T>> action) {
        if (Context.isOnEventLoopThread()) {
            return action.get();
        } else {
            Context context = VertxContext.getOrCreateDuplicatedContext(vertx);
            Promise<T> promise = Promise.promise();
            context.runOnContext(v -> action.get().onSuccess(promise::complete).onFailure(promise::fail));
            return promise.future();
        }
    }
}
