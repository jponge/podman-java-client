package podman.client.images;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.RequestOptions;
import io.vertx.core.parsetools.JsonEvent;
import io.vertx.core.parsetools.JsonParser;
import io.vertx.core.streams.ReadStream;
import io.vertx.uritemplate.UriTemplate;
import io.vertx.uritemplate.Variables;
import podman.internal.ClientContext;
import podman.internal.HttpClientHelpers;

import static io.vertx.core.Future.failedFuture;
import static io.vertx.core.Future.succeededFuture;
import static io.vertx.uritemplate.Variables.variables;
import static podman.internal.HttpClientHelpers.statusCode;

public class ImagesGroupImpl implements ImagesGroup {

    private final ClientContext context;

    public ImagesGroupImpl(ClientContext context) {
        this.context = context;
    }

    private static final UriTemplate PULL_TPL = UriTemplate.of(
            "/{base}/libpod/images/pull{?allTags,Arch,compatMode,OS,policy,quiet,reference,tlsVerify,Variant}");

    @Override
    public Future<ReadStream<JsonEvent>> pull(String reference, ImagePullOptions imagePullOptions) {
        if (!context.isCalledFromEventLoop()) {
            return context.accessingStreamOutsideEventLoop();
        }
        Variables vars = variables()
                .set("base", context.options().getApiVersion())
                .set("reference", reference);
        RequestOptions requestOptions = new RequestOptions()
                .setMethod(HttpMethod.POST)
                .setServer(context.socketAddress())
                .setURI(PULL_TPL.expandToString(imagePullOptions.fillQueryParams(vars)));
        if (imagePullOptions.registryAuth() != null) {
            requestOptions.addHeader("X-Registry-Auth", imagePullOptions.registryAuth());
        }
        return HttpClientHelpers.makeSimplifiedRequest(
                context.httpClient(),
                requestOptions,
                response -> statusCode(response, 200),
                response -> succeededFuture(JsonParser.newParser(response).objectValueMode()));
    }

//    @Override
//    public Future<ReadStream<Buffer>> rawPull(String reference, ImagePullOptions imagePullOptions) {
//        Variables vars = variables()
//                .set("base", state.options().getApiVersion())
//                .set("reference", reference);
//        RequestOptions requestOptions = new RequestOptions()
//                .setMethod(HttpMethod.POST)
//                .setServer(state.socketAddress())
//                .setURI(PULL_TPL.expandToString(imagePullOptions.fillQueryParams(vars)));
//        if (imagePullOptions.registryAuth() != null) {
//            requestOptions.addHeader("X-Registry-Auth", imagePullOptions.registryAuth());
//        }
//        return state.httpClient()
//                .request(requestOptions)
//                .compose(HttpClientRequest::send)
//                .map(response -> response);
////        return HttpClientHelpers.makeSimplifiedRequest(
////                state.httpClient(),
////                requestOptions,
////                response -> statusCode(response, 200),
////                Future::succeededFuture);
//    }
}
