package podman.client.volumes;

import static io.vertx.core.http.HttpResponseExpectation.*;
import static podman.internal.HttpPredicates.requestException;

import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import podman.client.JsonFilters;
import podman.internal.ClientState;

public class VolumesGroupImpl implements VolumesGroup {

    private final ClientState state;

    public VolumesGroupImpl(ClientState state) {
        this.state = state;
    }

    @Override
    public Future<JsonObject> create(VolumeCreateOptions options) {
        String path = state.options().getVersionedBasePath() + "libpod/volumes/create";
        return state.webClient()
                .request(HttpMethod.POST, state.socketAddress(), path)
                .sendJsonObject(options.json())
                .expecting(SC_OK.or(SC_CREATED).and(JSON).wrappingFailure(requestException()))
                .map(HttpResponse::bodyAsJsonObject);
    }

    @Override
    public Future<JsonObject> inspect(String name) {
        String path = state.options().getVersionedBasePath() + "libpod/volumes/" + name + "/json";
        return state.webClient()
                .request(HttpMethod.GET, state.socketAddress(), path)
                .send()
                .expecting(SC_OK.and(JSON).wrappingFailure(requestException()))
                .map(HttpResponse::bodyAsJsonObject);
    }

    @Override
    public Future<JsonArray> list(JsonFilters filters) {
        String path = state.options().getVersionedBasePath() + "libpod/volumes/json";
        return state.webClient()
                .request(HttpMethod.GET, state.socketAddress(), path)
                .addQueryParam("filters", filters.json().encode())
                .send()
                .expecting(SC_OK.and(JSON).wrappingFailure(requestException()))
                .map(HttpResponse::bodyAsJsonArray);
    }

    @Override
    public Future<JsonArray> prune(JsonFilters filters) {
        String path = state.options().getVersionedBasePath() + "libpod/volumes/prune";
        return state.webClient()
                .request(HttpMethod.POST, state.socketAddress(), path)
                .addQueryParam("filters", filters.json().encode())
                .send()
                .expecting(SC_OK.and(JSON).wrappingFailure(requestException()))
                .map(HttpResponse::bodyAsJsonArray);
    }

    @Override
    public Future<Void> remove(String name, boolean force) {
        String path = state.options().getVersionedBasePath() + "libpod/volumes/" + name;
        return state.webClient()
                .request(HttpMethod.DELETE, state.socketAddress(), path)
                .addQueryParam("force", String.valueOf(force))
                .send()
                .expecting(SC_OK.or(SC_NO_CONTENT).wrappingFailure(requestException()))
                .mapEmpty();
    }

    @Override
    public Future<Boolean> exists(String name) {
        String path = state.options().getVersionedBasePath() + "libpod/volumes/" + name + "/exists";
        return state.webClient()
                .request(HttpMethod.GET, state.socketAddress(), path)
                .send()
                .expecting(SC_OK.or(SC_NO_CONTENT).or(SC_NOT_FOUND).wrappingFailure(requestException()))
                .map(response -> response.statusCode() != 404);
    }
}
