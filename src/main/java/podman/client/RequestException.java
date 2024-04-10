package podman.client;

import io.vertx.core.impl.NoStackTraceException;

public class RequestException extends NoStackTraceException {

    private final int statusCode;
    private final String payload;

    public RequestException(int statusCode, String payload) {
        super(payload);
        this.statusCode = statusCode;
        this.payload = payload;
    }

    public int statusCode() {
        return statusCode;
    }

    public String payload() {
        return payload;
    }
}
