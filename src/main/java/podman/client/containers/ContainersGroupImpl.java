package podman.client.containers;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.uritemplate.UriTemplate;
import io.vertx.uritemplate.Variables;
import podman.internal.ClientContext;

import static io.vertx.uritemplate.Variables.variables;
import static podman.internal.HttpClientHelpers.*;

public class ContainersGroupImpl implements ContainersGroup {

    private final ClientContext context;

    public ContainersGroupImpl(ClientContext context) {
        this.context = context;
    }

    private static final UriTemplate CREATE_TPL = UriTemplate.of("/{base}/libpod/containers/create");

    @Override
    public Future<JsonObject> create(ContainerCreateOptions containerCreateOptions) {
        Variables vars = variables().set("base", context.options().getApiVersion());
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.POST)
                .setServer(context.socketAddress())
                .setURI(CREATE_TPL.expandToString(vars));
        return makeSimplifiedRequestWithPayload(
                context.httpClient(),
                requestOptions,
                Buffer.buffer(containerCreateOptions.json().encode()),
                response -> statusCode(response, 200, 201),
                response -> response.body().map(Buffer::toJsonObject));
    }
}
