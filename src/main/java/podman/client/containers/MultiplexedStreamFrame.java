package podman.client.containers;

import io.vertx.core.buffer.Buffer;

public record MultiplexedStreamFrame(Type type, Buffer buffer) {

    public enum Type {
        STD_IN,
        STD_OUT,
        STD_ERR;

        static Type fromCode(int code) {
            return switch (code) {
                case 0 -> STD_IN;
                case 1 -> STD_OUT;
                case 2 -> STD_ERR;
                default -> throw new IllegalArgumentException("Unknown code " + code);
            };
        }
    }
}
