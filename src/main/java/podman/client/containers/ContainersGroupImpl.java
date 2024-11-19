package podman.client.containers;

import static io.vertx.core.Future.succeededFuture;
import static io.vertx.uritemplate.Variables.variables;
import static podman.internal.HttpClientHelpers.*;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.uritemplate.UriTemplate;
import io.vertx.uritemplate.Variables;
import java.util.concurrent.Flow;
import mutiny.zero.vertxpublishers.VertxPublisher;
import podman.internal.ClientContext;

public class ContainersGroupImpl implements ContainersGroup {

    private final ClientContext context;

    public ContainersGroupImpl(ClientContext context) {
        this.context = context;
    }

    private static final UriTemplate CREATE_TPL = UriTemplate.of("/{base}/libpod/containers/create");

    @Override
    public Future<JsonObject> create(ContainerCreateOptions options) {
        Variables vars = variables().set("base", context.options().getApiVersion());
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.POST)
                .setServer(context.socketAddress())
                .setURI(CREATE_TPL.expandToString(vars));
        return makeSimplifiedRequestWithPayload(
                context.vertx(),
                context.httpClient(),
                requestOptions,
                Buffer.buffer(options.json().encode()),
                response -> statusCode(response, 200, 201),
                response -> response.body().map(Buffer::toJsonObject));
    }

    private static final UriTemplate DELETE_TPL =
            UriTemplate.of("/{base}/libpod/containers/{name}{?depend,force,ignore,timeout,v}");

    @Override
    public Future<JsonArray> delete(String name, ContainerDeleteOptions options) {
        Variables vars =
                variables().set("base", context.options().getApiVersion()).set("name", name);
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.DELETE)
                .setServer(context.socketAddress())
                .setURI(DELETE_TPL.expandToString(options.fillQueryParams(vars)));
        return makeSimplifiedRequest(
                context.vertx(),
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 200),
                response -> response.body().map(Buffer::toJsonArray));
    }

    private static final UriTemplate EXISTS_TPL = UriTemplate.of("/{base}/libpod/containers/{name}/exists");

    @Override
    public Future<Boolean> exists(String name) {
        Variables vars =
                variables().set("base", context.options().getApiVersion()).set("name", name);
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.GET)
                .setServer(context.socketAddress())
                .setURI(EXISTS_TPL.expandToString(vars));
        return makeSimplifiedRequest(
                context.vertx(),
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 204, 404),
                response -> succeededFuture(response.statusCode() != 404));
    }

    private static final UriTemplate START_TPL = UriTemplate.of("/{base}/libpod/containers/{name}/start{?detachKeys}");

    @Override
    public Future<Void> start(String name, String detachKeys) {
        Variables vars =
                variables().set("base", context.options().getApiVersion()).set("name", name);
        if (detachKeys != null) {
            vars.set("detachKeys", detachKeys);
        }
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.POST)
                .setServer(context.socketAddress())
                .setURI(START_TPL.expandToString(vars));
        return makeSimplifiedRequest(
                context.vertx(),
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 204),
                response -> succeededFuture());
    }

    private static final UriTemplate PAUSE_TPL = UriTemplate.of("/{base}/libpod/containers/{name}/pause");

    @Override
    public Future<Void> pause(String name) {
        Variables vars =
                variables().set("base", context.options().getApiVersion()).set("name", name);
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.POST)
                .setServer(context.socketAddress())
                .setURI(PAUSE_TPL.expandToString(vars));
        return makeSimplifiedRequest(
                context.vertx(),
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 204),
                response -> succeededFuture());
    }

    private static final UriTemplate UNPAUSE_TPL = UriTemplate.of("/{base}/libpod/containers/{name}/unpause");

    @Override
    public Future<Void> unpause(String name) {
        Variables vars =
                variables().set("base", context.options().getApiVersion()).set("name", name);
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.POST)
                .setServer(context.socketAddress())
                .setURI(UNPAUSE_TPL.expandToString(vars));
        return makeSimplifiedRequest(
                context.vertx(),
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 204),
                response -> succeededFuture());
    }

    private static final UriTemplate KILL_TPL = UriTemplate.of("/{base}/libpod/containers/{name}/kill{?signal}");

    @Override
    public Future<Void> kill(String name, String signal) {
        Variables vars =
                variables().set("base", context.options().getApiVersion()).set("name", name);
        if (signal != null) {
            vars.set("signal", signal);
        }
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.POST)
                .setServer(context.socketAddress())
                .setURI(KILL_TPL.expandToString(vars));
        return makeSimplifiedRequest(
                context.vertx(),
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 204),
                response -> succeededFuture());
    }

    private static final UriTemplate STOP_TPL =
            UriTemplate.of("/{base}/libpod/containers/{name}/stop{?Ignore,timeout}");

    @Override
    public Future<Void> stop(String name, boolean ignoreIfStopped, int timeout) {
        Variables vars = variables()
                .set("base", context.options().getApiVersion())
                .set("name", name)
                .set("Ignore", String.valueOf(ignoreIfStopped))
                .set("timeout", String.valueOf(timeout));
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.POST)
                .setServer(context.socketAddress())
                .setURI(STOP_TPL.expandToString(vars));
        return makeSimplifiedRequest(
                context.vertx(),
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 204),
                response -> succeededFuture());
    }

    private static final UriTemplate RESTART_TPL = UriTemplate.of("/{base}/libpod/containers/{name}/restart{?t}");

    @Override
    public Future<Void> restart(String name, int timeout) {
        Variables vars = variables()
                .set("base", context.options().getApiVersion())
                .set("name", name)
                .set("t", String.valueOf(timeout));
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.POST)
                .setServer(context.socketAddress())
                .setURI(RESTART_TPL.expandToString(vars));
        return makeSimplifiedRequest(
                context.vertx(),
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 204),
                response -> succeededFuture());
    }

    private static final UriTemplate LOGS_TPL =
            UriTemplate.of("/{base}/libpod/containers/{name}/logs{?follow,since,stderr,stdout,tail,timestamps,until}");

    @Override
    public Flow.Publisher<MultiplexedStreamFrame> logs(String name, ContainerGetLogsOptions options) {
        Variables vars =
                variables().set("base", context.options().getApiVersion()).set("name", name);
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.GET)
                .setServer(context.socketAddress())
                .setURI(LOGS_TPL.expandToString(options.fillQueryParams(vars)));
        return VertxPublisher.fromFuture(() -> makeSimplifiedRequest(
                context.vertx(),
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 200),
                response -> {
                    response.pause();
                    return succeededFuture(new ContainerOutputReadStream(response));
                }));
    }

    private static final UriTemplate INSPECT_TPL = UriTemplate.of("/{base}/libpod/containers/{name}/json");

    @Override
    public Future<JsonObject> inspect(String name, ContainerInspectOptions options) {
        Variables vars =
                variables().set("base", context.options().getApiVersion()).set("name", name);
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.GET)
                .setServer(context.socketAddress())
                .setURI(INSPECT_TPL.expandToString(options.fillQueryParams(vars)));
        return makeSimplifiedRequest(
                context.vertx(),
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 200),
                response -> response.body().map(Buffer::toJsonObject));
    }

    private static final UriTemplate TOP_TPL = UriTemplate.of("/{base}/libpod/containers/{name}/top");

    @Override
    public Future<JsonObject> top(String name, ContainerTopOptions options) {
        Variables vars =
                variables().set("base", context.options().getApiVersion()).set("name", name);
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.GET)
                .setServer(context.socketAddress())
                .setURI(TOP_TPL.expandToString(options.fillQueryParams(vars)));
        return makeSimplifiedRequest(
                context.vertx(),
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 200),
                response -> response.body().map(Buffer::toJsonObject));
    }
}
