package podman.internal;

import static io.vertx.core.Future.failedFuture;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.RequestOptions;
import java.util.function.Function;
import java.util.function.Predicate;
import podman.client.RequestException;

public interface HttpClientHelpers {

    static <T> Future<T> makeRequest(
            HttpClient httpClient, RequestOptions options, Function<HttpClientResponse, Future<T>> handler) {
        return httpClient
                .request(options)
                .compose(HttpClientRequest::send)
                .compose(handler, HttpClientHelpers::wrapFailure);
    }

    static <T> Future<T> makeRequestWithPayload(
            HttpClient httpClient,
            RequestOptions options,
            Buffer payload,
            Function<HttpClientResponse, Future<T>> handler) {
        return httpClient
                .request(options)
                .compose(httpClientRequest -> httpClientRequest.send(payload))
                .compose(handler, HttpClientHelpers::wrapFailure);
    }

    static <T> Future<T> makeSimplifiedRequest(
            HttpClient httpClient,
            RequestOptions options,
            Predicate<HttpClientResponse> predicate,
            Function<HttpClientResponse, Future<T>> handler) {
        return makeRequest(httpClient, options, response -> {
            if (predicate.test(response)) {
                return handler.apply(response);
            } else {
                return consumeErrorResponseAndFail(response);
            }
        });
    }

    static <T> Future<T> makeSimplifiedRequestWithPayload(
            HttpClient httpClient,
            RequestOptions options,
            Buffer payload,
            Predicate<HttpClientResponse> predicate,
            Function<HttpClientResponse, Future<T>> handler) {
        return makeRequestWithPayload(httpClient, options, payload, response -> {
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
}
