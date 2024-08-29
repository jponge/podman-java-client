package podman.client.secrets;

import io.vertx.core.json.JsonObject;
import io.vertx.uritemplate.Variables;

public class SecretCreateOptions {

    private String driver;
    private String driverOpts;
    private final JsonObject labels = new JsonObject();

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
        return labels;
    }

    public SecretCreateOptions label(String key, String value) {
        labels.put(key, value);
        return this;
    }

    public Variables fillQueryParams(Variables vars) {
        if (driver != null) {
            vars.set("driver", driver);
        }
        if (driverOpts != null) {
            vars.set("driveropts", driverOpts);
        }
        return vars.set("labels", labels.encode());
    }
}
