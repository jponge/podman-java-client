package podman.client;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.predicate.ResponsePredicate;
import io.vertx.ext.web.codec.BodyCodec;

class SystemGroupImpl implements SystemGroup {

    private final ClientState state;

    public SystemGroupImpl(ClientState state) {
        this.state = state;
    }

    @Override
    public Future<JsonObject> version() {
        String path = state.options().getVersionedBasePath() + "libpod/version";
        return state.webClient()
                .request(HttpMethod.GET, state.socketAddress(), path)
                .as(BodyCodec.jsonObject())
                .expect(ResponsePredicate.SC_OK)
                .send()
                .map(HttpResponse::body);
    }

    @Override
    public Future<JsonObject> info() {
        String path = state.options().getVersionedBasePath() + "libpod/info";
        return state.webClient()
                .request(HttpMethod.GET, state.socketAddress(), path)
                .as(BodyCodec.jsonObject())
                .expect(ResponsePredicate.SC_OK)
                .send()
                .map(HttpResponse::body);
    }

    @Override
    public Future<JsonObject> df() {
        String path = state.options().getVersionedBasePath() + "libpod/system/df";
        return state.webClient()
                .request(HttpMethod.GET, state.socketAddress(), path)
                .as(BodyCodec.jsonObject())
                .expect(ResponsePredicate.SC_OK)
                .send()
                .map(HttpResponse::body);
    }

    @Override
    public Future<JsonObject> ping() {
        String path = state.options().getVersionedBasePath() + "libpod/_ping";
        return state.webClient()
                .request(HttpMethod.HEAD, state.socketAddress(), path)
                .expect(ResponsePredicate.SC_OK)
                .send()
                .map(response -> {
                    JsonObject result = new JsonObject();
                    response.headers().forEach(result::put);
                    return result;
                });
    }
}
