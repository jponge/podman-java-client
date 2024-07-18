# Early stage development notes

## No JSON mapping

Return types are almost always `JsonObject` / `JsonArray`.
This greatly simplifies the design over mapping to complex POJO trees with Jackson.

Complex JSON trees can be queried with `JsonPointer`.

The `machine` classes provide wrapper result types that offer methods to access specific entries either by key, or using `JsonPointer` queries, while still providing raw access to the decorated `JsonObject`. 

## Threading assumptions

The Vert.x core HTTP client is used to make calls to the Podman API.

Some operations on this client such as setting data handlers are very thread sensitive, or data can be corrupted / lost.

- Most one-shot operations (e.g., `Future<JsonObject>`-returning API methods) can work fine when called from _any_ thread, because body handlers are set from a Vert.x event-loop thread.
- `ReadStream`-providing methods will need to be called from a Vert.x event-loop thread, and the client will check which thread is being called.

Note that we _might hypothetically_ get around the `ReadStream` limitation by implementing a `ReadWriteStream` adapter that would be a `WriteStream` for a `HttpClientResponse` pipe, and a `ReadStream` for the outside.
