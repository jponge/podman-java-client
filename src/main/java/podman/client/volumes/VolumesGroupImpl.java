package podman.client.volumes;

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
import podman.client.JsonFilters;
import podman.internal.ClientContext;

public class VolumesGroupImpl implements VolumesGroup {

    private final ClientContext context;

    public VolumesGroupImpl(ClientContext context) {
        this.context = context;
    }

    private static final UriTemplate CREATE_TPL = UriTemplate.of("/{base}/libpod/volumes/create");

    @Override
    public Future<JsonObject> create(VolumeCreateOptions options) {
        Variables vars = variables().set("base", context.options().getApiVersion());
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.POST)
                .setServer(context.socketAddress())
                .addHeader("Content-Type", "application/json")
                .setURI(CREATE_TPL.expandToString(vars));
        return makeSimplifiedRequestWithPayload(
                context.vertx(),
                context.httpClient(),
                requestOptions,
                Buffer.buffer(options.json().encode()),
                response -> statusCode(response, 200, 201),
                response -> response.body().map(Buffer::toJsonObject));
    }

    private static final UriTemplate INSPECT_TPL = UriTemplate.of("/{base}/libpod/volumes/{name}/json");

    @Override
    public Future<JsonObject> inspect(String name) {
        Variables vars =
                variables().set("base", context.options().getApiVersion()).set("name", name);
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.GET)
                .setServer(context.socketAddress())
                .setURI(INSPECT_TPL.expandToString(vars));
        return makeSimplifiedRequest(
                context.vertx(),
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 200),
                response -> response.body().map(Buffer::toJsonObject));
    }

    private static final UriTemplate LIST_TPL = UriTemplate.of("/{base}/libpod/volumes/json{?filters}");

    @Override
    public Future<JsonArray> list(JsonFilters filters) {
        Variables vars = variables()
                .set("base", context.options().getApiVersion())
                .set("filters", filters.json().encode());
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.GET)
                .setServer(context.socketAddress())
                .setURI(LIST_TPL.expandToString(vars));
        return makeSimplifiedRequest(
                context.vertx(),
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 200),
                response -> response.body().map(Buffer::toJsonArray));
    }

    private static final UriTemplate PRUNE_TPL = UriTemplate.of("/{base}/libpod/volumes/prune{?filters}");

    @Override
    public Future<JsonArray> prune(JsonFilters filters) {
        Variables vars = variables()
                .set("base", context.options().getApiVersion())
                .set("filters", filters.json().encode());
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.POST)
                .setServer(context.socketAddress())
                .setURI(PRUNE_TPL.expandToString(vars));
        return makeSimplifiedRequest(
                context.vertx(),
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 200),
                response -> response.body().map(Buffer::toJsonArray));
    }

    private static final UriTemplate REMOVE_TPL = UriTemplate.of("/{base}/libpod/volumes/{name}{?force}");

    @Override
    public Future<Void> remove(String name, boolean force) {
        Variables vars = variables()
                .set("base", context.options().getApiVersion())
                .set("name", name)
                .set("force", String.valueOf(force));
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.DELETE)
                .setServer(context.socketAddress())
                .setURI(REMOVE_TPL.expandToString(vars));
        return makeSimplifiedRequest(
                context.vertx(),
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 200, 204),
                response -> response.body().mapEmpty());
    }

    private static final UriTemplate EXISTS_TPL = UriTemplate.of("/{base}/libpod/volumes/{name}/exists");

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
                response -> statusCode(response, 200, 204, 404),
                response -> succeededFuture(response.statusCode() != 404));
    }
}
