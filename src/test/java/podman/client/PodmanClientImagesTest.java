package podman.client;

import static helpers.AsyncTestHelpers.awaitResult;
import static org.assertj.core.api.Assertions.assertThat;

import helpers.PodmanHelpers;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.concurrent.Flow;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import podman.client.images.ImagePullOptions;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
public class PodmanClientImagesTest {

    static final String PODMAN_HELLO_REF = "quay.io/podman/hello:latest";
    static final String NOT_FOUND_REF = "quay.io/idonotexist/icannotbefound:idonotexist";
    static final String NOT_REACHABLE_REF = "sample.test/podman/hello:latest";

    Vertx vertx;
    PodmanClient client;

    @BeforeAll
    void setup() throws Throwable {
        VertxOptions vertxOptions = new VertxOptions().setPreferNativeTransport(true);
        vertx = Vertx.vertx(vertxOptions);

        String socketPath = awaitResult(PodmanHelpers.podmanSocketPath(vertx));
        PodmanClient.Options options = new PodmanClient.Options().setSocketPath(socketPath);
        client = PodmanClient.create(vertx, options);
    }

    @AfterAll
    void cleanup() throws Throwable {
        awaitResult(client.close());
        awaitResult(vertx.close());
    }

    @Test
    void pullExistingImage() throws Throwable {
        Flow.Publisher<JsonObject> pull = client.images().pull(PODMAN_HELLO_REF, new ImagePullOptions());
        AssertSubscriber<JsonObject> sub = AssertSubscriber.create(Long.MAX_VALUE);
        pull.subscribe(sub);

        sub.awaitCompletion().assertCompleted();
        List<JsonObject> events = sub.getItems();
        assertThat(events)
                .anyMatch(event ->
                        event.containsKey("stream") && event.getString("stream").contains("Trying to pull"))
                .anyMatch(event ->
                        event.containsKey("stream") && event.getString("stream").contains("Writing manifest"))
                .last()
                .matches(event -> event.containsKey("images")
                        && event.containsKey("id")
                        && !event.getJsonArray("images").isEmpty());

        String imageId = events.getLast().getString("id");
        assertThat(awaitResult(client.images().exists(imageId))).isTrue();
    }

    @Test
    void bogusImageDoesNotExist() throws Throwable {
        String imageId = "this-does-not-exist-unless-you-created-such-an-image-name-but-then-what-can-i-do";
        assertThat(awaitResult(client.images().exists(imageId))).isFalse();
    }

    @Test
    void pullNonExistingImage() {
        Flow.Publisher<JsonObject> pull = client.images().pull(NOT_FOUND_REF, new ImagePullOptions());
        AssertSubscriber<JsonObject> sub = AssertSubscriber.create(Long.MAX_VALUE);
        pull.subscribe(sub);

        sub.awaitCompletion().assertCompleted();
        List<JsonObject> events = sub.getItems();

        assertThat(events)
                .first()
                .matches(event ->
                        event.containsKey("stream") && event.getString("stream").contains(("Trying to pull")));
        assertThat(events)
                .last()
                .matches(event ->
                        event.containsKey("error") && event.getString("error").contains(("unauthorized")));
    }

    @Test
    void pullFromNonReachableRegistry() {
        Flow.Publisher<JsonObject> pull = client.images().pull(NOT_REACHABLE_REF, new ImagePullOptions());
        AssertSubscriber<JsonObject> sub = AssertSubscriber.create(Long.MAX_VALUE);
        pull.subscribe(sub);

        sub.awaitCompletion().assertCompleted();
        List<JsonObject> events = sub.getItems();

        assertThat(events)
                .first()
                .matches(event ->
                        event.containsKey("stream") && event.getString("stream").contains(("Trying to pull")));
        assertThat(events)
                .last()
                .matches(event ->
                        event.containsKey("error") && event.getString("error").contains(("no such host")));
    }
}
