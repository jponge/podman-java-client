package podman.client.secrets;

import static io.vertx.core.http.HttpResponseExpectation.*;
import static podman.internal.HttpPredicates.requestException;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import podman.client.JsonFilters;
import podman.internal.ClientState;

public class SecretsGroupImpl implements SecretsGroup {

    private final ClientState state;

    public SecretsGroupImpl(ClientState state) {
        this.state = state;
    }

    @Override
    public Future<JsonObject> create(String name, String data, SecretCreateOptions options) {
        String path = state.options().getVersionedBasePath() + "libpod/secrets/create";
        HttpRequest<Buffer> request = state.webClient()
                .request(HttpMethod.POST, state.socketAddress(), path)
                .addQueryParam("name", name);
        return options.fillQueryParams(request)
                .sendBuffer(Buffer.buffer(data))
                .expecting(SC_OK.or(SC_CREATED).and(JSON).wrappingFailure(requestException()))
                .map(HttpResponse::bodyAsJsonObject);
    }

    @Override
    public Future<Void> remove(String name) {
        String path = state.options().getVersionedBasePath() + "libpod/secrets/" + name;
        return state.webClient()
                .request(HttpMethod.DELETE, state.socketAddress(), path)
                .send()
                .expecting(SC_OK.or(SC_NO_CONTENT).wrappingFailure(requestException()))
                .mapEmpty();
    }

    @Override
    public Future<Boolean> exists(String name) {
        String path = state.options().getVersionedBasePath() + "libpod/secrets/" + name + "/exists";
        return state.webClient()
                .request(HttpMethod.GET, state.socketAddress(), path)
                .send()
                .expecting(SC_OK.or(SC_NO_CONTENT).or(SC_NOT_FOUND).wrappingFailure(requestException()))
                .map(response -> response.statusCode() != 404);
    }

    @Override
    public Future<JsonObject> inspect(String name, boolean showSecret) {
        String path = state.options().getVersionedBasePath() + "libpod/secrets/" + name + "/json";
        return state.webClient()
                .request(HttpMethod.GET, state.socketAddress(), path)
                .addQueryParam("showsecret", String.valueOf(showSecret))
                .send()
                .expecting(SC_OK.and(JSON).wrappingFailure(requestException()))
                .map(HttpResponse::bodyAsJsonObject);
    }

    @Override
    public Future<JsonArray> list(JsonFilters filters) {
        String path = state.options().getVersionedBasePath() + "libpod/secrets/json";
        return state.webClient()
                .request(HttpMethod.GET, state.socketAddress(), path)
                .addQueryParam("filters", filters.json().encode())
                .send()
                .expecting(SC_OK.and(JSON).wrappingFailure(requestException()))
                .map(HttpResponse::bodyAsJsonArray);
    }
}
