package podman.client.images;

import static io.vertx.core.Future.succeededFuture;
import static io.vertx.uritemplate.Variables.variables;
import static podman.internal.HttpClientHelpers.makeSimplifiedRequest;
import static podman.internal.HttpClientHelpers.statusCode;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.parsetools.JsonEvent;
import io.vertx.core.parsetools.JsonParser;
import io.vertx.uritemplate.UriTemplate;
import io.vertx.uritemplate.Variables;
import java.util.concurrent.Flow;
import mutiny.zero.operators.Transform;
import mutiny.zero.vertxpublishers.VertxPublisher;
import podman.internal.ClientContext;
import podman.internal.HttpClientHelpers;

public class ImagesGroupImpl implements ImagesGroup {

    private final ClientContext context;

    public ImagesGroupImpl(ClientContext context) {
        this.context = context;
    }

    private static final UriTemplate PULL_TPL = UriTemplate.of(
            "/{base}/libpod/images/pull{?allTags,Arch,compatMode,OS,policy,quiet,reference,tlsVerify,Variant}");

    @Override
    public Flow.Publisher<JsonObject> pull(String reference, ImagePullOptions imagePullOptions) {
        Variables vars =
                variables().set("base", context.options().getApiVersion()).set("reference", reference);
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.POST)
                .setServer(context.socketAddress())
                .setURI(PULL_TPL.expandToString(imagePullOptions.fillQueryParams(vars)));
        if (imagePullOptions.registryAuth() != null) {
            requestOptions.addHeader("X-Registry-Auth", imagePullOptions.registryAuth());
        }
        return new Transform<>(
                VertxPublisher.fromFuture(() -> HttpClientHelpers.makeSimplifiedRequest(
                        context.vertx(),
                        context.httpClient(),
                        requestOptions,
                        response -> statusCode(response, 200),
                        response -> {
                            response.pause();
                            return succeededFuture(
                                    JsonParser.newParser(response).objectValueMode());
                        })),
                JsonEvent::objectValue);
    }

    private static final UriTemplate EXISTS_TPL = UriTemplate.of("/{base}/libpod/images/{name}/exists");

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
