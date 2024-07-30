package podman.client.containers;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.uritemplate.UriTemplate;
import io.vertx.uritemplate.Variables;
import podman.internal.ClientContext;

import static io.vertx.core.Future.succeededFuture;
import static io.vertx.uritemplate.Variables.variables;
import static podman.internal.HttpClientHelpers.*;

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
                context.httpClient(),
                requestOptions,
                Buffer.buffer(options.json().encode()),
                response -> statusCode(response, 200, 201),
                response -> response.body().map(Buffer::toJsonObject));
    }

    private static final UriTemplate DELETE_TPL = UriTemplate.of("/{base}/libpod/containers/{name}{?depend,force,ignore,timeout,v}");

    @Override
    public Future<JsonArray> delete(String name, ContainerDeleteOptions options) {
        Variables vars = variables()
                .set("base", context.options().getApiVersion())
                .set("name", name);
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.DELETE)
                .setServer(context.socketAddress())
                .setURI(DELETE_TPL.expandToString(options.fillQueryParams(vars)));
        return makeSimplifiedRequest(
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 200),
                response -> response.body().map(Buffer::toJsonArray));
    }

    private static final UriTemplate EXISTS_TPL = UriTemplate.of("/{base}/libpod/containers/{name}/exists");

    @Override
    public Future<Boolean> exists(String name) {
        Variables vars = variables()
                .set("base", context.options().getApiVersion())
                .set("name", name);
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.GET)
                .setServer(context.socketAddress())
                .setURI(EXISTS_TPL.expandToString(vars));
        return makeSimplifiedRequest(
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 204, 404),
                response -> succeededFuture(response.statusCode() != 404));
    }
}
