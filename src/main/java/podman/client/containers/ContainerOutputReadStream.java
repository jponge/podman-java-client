package podman.client.containers;

import static podman.client.containers.MultiplexedStreamFrame.Type.fromCode;

import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;
import io.vertx.core.streams.ReadStream;

class ContainerOutputReadStream implements ReadStream<MultiplexedStreamFrame> {

    private final RecordParser parser;
    private int mode = 0;
    private byte streamCode;

    public ContainerOutputReadStream(ReadStream<Buffer> stream) {
        this.parser = RecordParser.newFixed(8, stream);
        parser.pause(); // Make sure we go into fetch mode
    }

    @Override
    public ReadStream<MultiplexedStreamFrame> exceptionHandler(Handler<Throwable> handler) {
        parser.exceptionHandler(handler);
        return this;
    }

    @Override
    public ReadStream<MultiplexedStreamFrame> handler(Handler<MultiplexedStreamFrame> handler) {
        parser.handler(buffer -> {
            switch (mode) {
                case 0:
                    streamCode = buffer.getByte(0);
                    int frameLength = buffer.getInt(4);
                    mode = 1;
                    parser.fixedSizeMode(frameLength);
                    parser.fetch(1);
                    break;
                case 1:
                    mode = 0;
                    handler.handle(new MultiplexedStreamFrame(fromCode(streamCode), buffer));
                    parser.fixedSizeMode(8);
                    break;
                default:
                    throw new IllegalStateException("Current mode is " + mode);
            }
        });
        return this;
    }

    @Override
    public ReadStream<MultiplexedStreamFrame> pause() {
        parser.pause();
        return this;
    }

    @Override
    public ReadStream<MultiplexedStreamFrame> resume() {
        parser.resume();
        return this;
    }

    @Override
    public ReadStream<MultiplexedStreamFrame> fetch(long amount) {
        parser.fetch(amount);
        return this;
    }

    @Override
    public ReadStream<MultiplexedStreamFrame> endHandler(Handler<Void> endHandler) {
        parser.endHandler(endHandler);
        return this;
    }
}
