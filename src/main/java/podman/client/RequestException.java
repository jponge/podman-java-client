package podman.client;

import io.vertx.core.impl.NoStackTraceException;

public class RequestException extends NoStackTraceException {

    private final int statusCode;
    private final String contentType;
    private final String payload;

    public RequestException(int statusCode, String contentType, String payload) {
        super(payload);
        this.statusCode = statusCode;
        this.contentType = contentType;
        this.payload = payload;
    }

    public RequestException(Throwable err) {
        super(err);
        this.statusCode = -1;
        this.contentType = null;
        this.payload = null;
    }

    public int statusCode() {
        return statusCode;
    }

    public String payload() {
        return payload;
    }

    public String contentType() {
        return contentType;
    }

    @Override
    public String getMessage() {
        if (statusCode != -1) {
            return "[status=" + statusCode + ", content-type=" + contentType + "] " + payload;
        } else {
            return getCause().getMessage();
        }
    }
}
