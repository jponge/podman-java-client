package podman.client;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.parsetools.JsonEvent;
import io.vertx.core.streams.ReadStream;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import podman.client.images.ImagePullOptions;
import podman.machine.PodmanMachineClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collector;

import static helpers.AsyncTestHelpers.awaitResult;
import static helpers.AsyncTestHelpers.awaitStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

        PodmanMachineClient machineClient = PodmanMachineClient.create(vertx);
        String socketPath = awaitResult(machineClient.findDefaultMachineConnectionSocketPath());
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
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Throwable> failure = new AtomicReference<>();
        ArrayList<JsonObject> events = new ArrayList<>();

        vertx.runOnContext(action -> {
            client.images().pull(PODMAN_HELLO_REF, new ImagePullOptions())
                    .onSuccess(stream -> {
                        stream.exceptionHandler(err -> {
                            failure.set(err);
                            latch.countDown();
                        });
                        stream.endHandler(done -> latch.countDown());
                        stream.handler(event -> events.add(event.objectValue()));
                    })
                    .onFailure(err -> {
                        failure.set(err);
                        latch.countDown();
                    });
        });

        assertThat(latch.await(30, TimeUnit.SECONDS)).isTrue();
        assertThat(failure.get()).isNull();

        assertThat(events)
                .anyMatch(event -> event.containsKey("stream") && event.getString("stream").contains("Trying to pull"))
                .anyMatch(event -> event.containsKey("stream") && event.getString("stream").contains("Writing manifest"))
                .last().matches(event -> event.containsKey("images") && event.containsKey("id") && !event.getJsonArray("images").isEmpty());
    }

    @Test
    @Disabled
    void pullNonExistingImage() throws Throwable {
        ReadStream<JsonEvent> stream = awaitResult(client.images().pull(NOT_FOUND_REF, new ImagePullOptions()));
        ArrayList<JsonObject> events = new ArrayList<>();
        awaitStream(stream, Duration.ofSeconds(30), event -> events.add(event.objectValue()));

        assertThat(events).hasSizeGreaterThan(1);
        JsonObject error = events.getLast();
        assertThat(error.containsKey("error")).isTrue();
        assertThat(error.getString("error")).contains("access to the requested resource is not authorized");
    }

    @Test
    @Disabled
    void pullNonReachableImage() throws Throwable {

//        vertx.runOnContext(v -> {
//            client.images().pull(NOT_REACHABLE_REF, new ImagePullOptions())
//                    .onSuccess(stream -> {
//                        stream.handler(e -> System.out.println(e.objectValue().encodePrettily()));
//                    })
//                    .onFailure(Throwable::printStackTrace);
//        });

//        Thread.sleep(30_000);

        ReadStream<JsonEvent> stream = awaitResult(client.images().pull(NOT_REACHABLE_REF, new ImagePullOptions()));

        ArrayList<JsonObject> events = new ArrayList<>();
        awaitStream(stream, Duration.ofSeconds(30), event -> events.add(event.objectValue()));
        System.out.println(events);
    }

//    void rawPull() throws Throwable {
//        ReadStream<Buffer> stream = awaitResult(client.images().rawPull(NOT_REACHABLE_REF, new ImagePullOptions()));
//        awaitStream(stream, Duration.ofSeconds(30), buffer -> System.out.println(">>> " + buffer.toString()));

//        CountDownLatch latch = new CountDownLatch(1);
//        vertx.runOnContext(v -> {
//            client.images().rawPull(NOT_REACHABLE_REF, new ImagePullOptions())
//                    .onSuccess(stream -> {
//                        stream.exceptionHandler(err -> {
//                            System.out.println("!!! " + err.getMessage());
//                            latch.countDown();
//                        });
//                        stream.endHandler(end -> {
//                            System.out.println("--- done");
//                            latch.countDown();
//                        });
//                        stream.handler(buffer -> {
//                            System.out.println(">>> " + buffer.toString());
//                        });
//                    });
//        });
//        latch.await(10, TimeUnit.SECONDS);
//    }
}
