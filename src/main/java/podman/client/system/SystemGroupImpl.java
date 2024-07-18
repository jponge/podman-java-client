package podman.client.system;

import static io.vertx.core.Future.succeededFuture;
import static io.vertx.uritemplate.Variables.variables;
import static podman.internal.HttpClientHelpers.statusCode;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.parsetools.JsonEvent;
import io.vertx.core.parsetools.JsonParser;
import io.vertx.core.streams.ReadStream;
import io.vertx.uritemplate.UriTemplate;
import io.vertx.uritemplate.Variables;
import podman.internal.ClientContext;
import podman.internal.HttpClientHelpers;

public class SystemGroupImpl implements SystemGroup {

    private final ClientContext context;

    public SystemGroupImpl(ClientContext context) {
        this.context = context;
    }

    private static final UriTemplate VERSION_TPL = UriTemplate.of("/{base}/libpod/version");

    @Override
    public Future<JsonObject> version() {
        Variables vars = variables().set("base", context.options().getApiVersion());
        RequestOptions options = new RequestOptions()
                .setMethod(HttpMethod.GET)
                .setServer(context.socketAddress())
                .setURI(VERSION_TPL.expandToString(vars));
        return HttpClientHelpers.makeSimplifiedRequest(
                context.httpClient(), options, response -> statusCode(response, 200), response -> response.body()
                        .map(Buffer::toJsonObject));
    }

    private static final UriTemplate INFO_TPL = UriTemplate.of("/{base}/libpod/info");

    @Override
    public Future<JsonObject> info() {
        Variables vars = variables().set("base", context.options().getApiVersion());
        RequestOptions options = new RequestOptions()
                .setMethod(HttpMethod.GET)
                .setServer(context.socketAddress())
                .setURI(INFO_TPL.expandToString(vars));
        return HttpClientHelpers.makeSimplifiedRequest(
                context.httpClient(), options, response -> statusCode(response, 200), response -> response.body()
                        .map(Buffer::toJsonObject));
    }

    private static final UriTemplate DF_TPL = UriTemplate.of("/{base}/system/df");

    @Override
    public Future<JsonObject> df() {
        Variables vars = variables().set("base", context.options().getApiVersion());
        RequestOptions options = new RequestOptions()
                .setMethod(HttpMethod.GET)
                .setServer(context.socketAddress())
                .setURI(DF_TPL.expandToString(vars));
        return HttpClientHelpers.makeSimplifiedRequest(
                context.httpClient(), options, response -> statusCode(response, 200), response -> response.body()
                        .map(Buffer::toJsonObject));
    }

    private static final UriTemplate PING_TPL = UriTemplate.of("/{base}/libpod/_ping");

    @Override
    public Future<JsonObject> ping() {
        Variables vars = variables().set("base", context.options().getApiVersion());
        RequestOptions options = new RequestOptions()
                .setMethod(HttpMethod.HEAD)
                .setServer(context.socketAddress())
                .setURI(PING_TPL.expandToString(vars));
        return HttpClientHelpers.makeSimplifiedRequest(
                context.httpClient(), options, response -> statusCode(response, 200), response -> {
                    JsonObject result = new JsonObject();
                    response.headers().forEach(result::put);
                    return succeededFuture(result);
                });
    }

    private static final UriTemplate PRUNE_TPL =
            UriTemplate.of("/{base}/libpod/system/prune{?all,volumes,external,filters}");

    @Override
    public Future<JsonObject> prune(SystemPruneOptions pruneOptions) {
        Variables vars = variables().set("base", context.options().getApiVersion());
        String uri = PRUNE_TPL.expandToString(pruneOptions.fillQueryParams(vars));
        RequestOptions options = new RequestOptions()
                .setMethod(HttpMethod.POST)
                .setServer(context.socketAddress())
                .setURI(uri);
        return HttpClientHelpers.makeSimplifiedRequest(
                context.httpClient(), options, response -> statusCode(response, 200), response -> response.body()
                        .map(Buffer::toJsonObject));
    }

    private static final UriTemplate EVENTS_TPL = UriTemplate.of("/{base}/libpod/events{?filters,stream,since,until}");

    @Override
    public Future<ReadStream<JsonEvent>> getEvents(SystemGetEventsOptions getEventsOptions) {
        if (!context.isCalledFromEventLoop()) {
            return context.accessingStreamOutsideEventLoop();
        }
        Variables vars = variables().set("base", context.options().getApiVersion());
        String uri = EVENTS_TPL.expandToString(getEventsOptions.fillQueryParams(vars));
        RequestOptions options = new RequestOptions()
                .setMethod(HttpMethod.GET)
                .setServer(context.socketAddress())
                .setURI(uri);
        return HttpClientHelpers.makeSimplifiedRequest(
                context.httpClient(),
                options,
                response -> statusCode(response, 200),
                response -> succeededFuture(JsonParser.newParser(response).objectValueMode()));
    }
}
