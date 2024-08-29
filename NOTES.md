# Early stage development notes

## No JSON mapping

Return types are almost always `JsonObject` / `JsonArray`.
This greatly simplifies the design over mapping to complex POJO trees with Jackson.

Complex JSON trees can be queried with `JsonPointer`.

The `machine` classes provide wrapper result types that offer methods to access specific entries either by key, or using `JsonPointer` queries, while still providing raw access to the decorated `JsonObject`. 

Most option passing data types such as `SecretCreateOptions` can be POJO with a few fields and helper methods to fill HTTP request parameters, or convert to a JSON type.
There are however a few _huge_ Podman types such as `ImageCreateOptions` where there would be too many fields and subtypes.
In such a case a more sensible design is to hold a `JsonObject` field, and (fluent) methods gradually contribute to forming the eventual payload.

## Threading assumptions

The Vert.x core HTTP client is used to make calls to the Podman API.

Some operations on this client such as setting data handlers are very thread sensitive, or data can be corrupted / lost.

- Most one-shot operations (e.g., `Future<JsonObject>`-returning API methods) can work fine when called from _any_ thread, because body handlers are set from a Vert.x event-loop thread.
- `Flow.Publisher`-providing methods can be called from any thread, because the `ReadStream` handler setting is done on an event-loop thread by construction with Mutiny Zero, not by end user code.
