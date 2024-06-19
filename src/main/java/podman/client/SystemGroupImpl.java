package podman.client;

import static io.vertx.core.http.HttpResponseExpectation.*;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.core.parsetools.JsonEvent;
import io.vertx.core.parsetools.JsonParser;
import io.vertx.core.streams.ReadStream;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import podman.internal.HttpResponsePredicates;

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
                .send()
                .expecting(SC_OK)
                .map(HttpResponse::body);
    }

    @Override
    public Future<JsonObject> info() {
        String path = state.options().getVersionedBasePath() + "libpod/info";
        return state.webClient()
                .request(HttpMethod.GET, state.socketAddress(), path)
                .as(BodyCodec.jsonObject())
                .send()
                .expecting(SC_OK)
                .map(HttpResponse::body);
    }

    @Override
    public Future<JsonObject> df() {
        String path = state.options().getVersionedBasePath() + "libpod/system/df";
        return state.webClient()
                .request(HttpMethod.GET, state.socketAddress(), path)
                .as(BodyCodec.jsonObject())
                .send()
                .expecting(SC_OK)
                .map(HttpResponse::body);
    }

    @Override
    public Future<JsonObject> ping() {
        String path = state.options().getVersionedBasePath() + "libpod/_ping";
        return state.webClient()
                .request(HttpMethod.HEAD, state.socketAddress(), path)
                .send()
                .expecting(SC_OK)
                .map(response -> {
                    JsonObject result = new JsonObject();
                    response.headers().forEach(result::put);
                    return result;
                });
    }

    @Override
    public Future<JsonObject> prune(SystemPruneOptions pruneOptions) {
        String path = state.options().getVersionedBasePath() + "libpod/system/prune";
        HttpRequest<Buffer> request = state.webClient().request(HttpMethod.POST, state.socketAddress(), path);
        return pruneOptions
                .fillQueryParams(request)
                .as(BodyCodec.jsonObject())
                .send()
                .expecting(HttpResponsePredicates.statusCode(200))
                .map(HttpResponse::body);
    }

    @Override
    public Future<JsonObject> check(SystemCheckOptions checkOptions) {
        String path = state.options().getVersionedBasePath() + "libpod/system/check";
        HttpRequest<Buffer> request = state.webClient().request(HttpMethod.POST, state.socketAddress(), path);
        return checkOptions
                .fillQueryParams(request)
                .as(BodyCodec.jsonObject())
                .send()
                .expecting(HttpResponsePredicates.statusCode(200))
                .map(HttpResponse::body);
    }

    @Override
    public ReadStream<JsonEvent> getEvents(GetEventsOptions getEventsOptions) {
        JsonParser parser = JsonParser.newParser().objectValueMode();
        String path = state.options().getVersionedBasePath() + "libpod/events";
        HttpRequest<Buffer> request = state.webClient().request(HttpMethod.GET, state.socketAddress(), path);
        getEventsOptions
                .fillQueryParams(request)
                .as(BodyCodec.jsonStream(parser))
                .send()
                .expecting(SC_OK);
        return parser;
    }
}
