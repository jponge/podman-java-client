package podman.client.secrets;

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

public class SecretsGroupImpl implements SecretsGroup {

    private final ClientContext context;

    public SecretsGroupImpl(ClientContext context) {
        this.context = context;
    }

    private static final UriTemplate CREATE_TPL =
            UriTemplate.of("/{base}/libpod/secrets/create{?name,driver,driveropts,labels}");

    @Override
    public Future<JsonObject> create(String name, String data, SecretCreateOptions options) {
        Variables vars =
                variables().set("base", context.options().getApiVersion()).set("name", name);
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.POST)
                .setServer(context.socketAddress())
                .setURI(CREATE_TPL.expandToString(options.fillQueryParams(vars)));
        return makeSimplifiedRequestWithPayload(
                context.httpClient(),
                requestOptions,
                Buffer.buffer(data),
                response -> statusCode(response, 200, 201),
                response -> response.body().map(Buffer::toJsonObject));
    }

    private static final UriTemplate REMOVE_TPL = UriTemplate.of("/{base}/libpod/secrets/{name}");

    @Override
    public Future<Void> remove(String name) {
        Variables vars =
                variables().set("base", context.options().getApiVersion()).set("name", name);
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.DELETE)
                .setServer(context.socketAddress())
                .setURI(REMOVE_TPL.expandToString(vars));
        return makeSimplifiedRequest(
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 200, 204),
                response -> response.body().mapEmpty());
    }

    private static final UriTemplate EXISTS_TPL = UriTemplate.of("/{base}/libpod/secrets/{name}/exists");

    @Override
    public Future<Boolean> exists(String name) {
        Variables vars =
                variables().set("base", context.options().getApiVersion()).set("name", name);
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.GET)
                .setServer(context.socketAddress())
                .setURI(EXISTS_TPL.expandToString(vars));
        return makeSimplifiedRequest(
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 200, 204, 404),
                response -> succeededFuture(response.statusCode() != 404));
    }

    private static final UriTemplate INSPECT_TPL = UriTemplate.of("/{base}/libpod/secrets/{name}/json{?showsecret}");

    @Override
    public Future<JsonObject> inspect(String name, boolean showSecret) {
        Variables vars = variables()
                .set("base", context.options().getApiVersion())
                .set("name", name)
                .set("showsecret", String.valueOf(showSecret));
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.GET)
                .setServer(context.socketAddress())
                .setURI(INSPECT_TPL.expandToString(vars));
        return makeSimplifiedRequest(
                context.httpClient(), requestOptions, response -> statusCode(response, 200), response -> response.body()
                        .map(Buffer::toJsonObject));
    }

    private static final UriTemplate LIST_TPL = UriTemplate.of("/{base}/libpod/secrets/json{?filters}");

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
                context.httpClient(), requestOptions, response -> statusCode(response, 200), response -> response.body()
                        .map(Buffer::toJsonArray));
    }
}
