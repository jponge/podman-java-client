package podman.client;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import podman.internal.JsonLabels;

public class SecretCreateOptions {

    private String driver;
    private String driverOpts;
    private final JsonLabels labels = new JsonLabels();

    public String driver() {
        return driver;
    }

    public SecretCreateOptions setDriver(String driver) {
        this.driver = driver;
        return this;
    }

    public String driverOpts() {
        return driverOpts;
    }

    public SecretCreateOptions setDriverOpts(String driverOpts) {
        this.driverOpts = driverOpts;
        return this;
    }

    public JsonObject labels() {
        return labels.labels();
    }

    public SecretCreateOptions label(String key, String value) {
        labels.put(key, value);
        return this;
    }

    public <T> HttpRequest<T> fillQueryParams(HttpRequest<T> request) {
        if (driver != null) {
            request.addQueryParam("driver", driver);
        }
        if (driverOpts != null) {
            request.addQueryParam("driveropts", driverOpts);
        }
        return request.addQueryParam("labels", labels().encode());
    }
}
