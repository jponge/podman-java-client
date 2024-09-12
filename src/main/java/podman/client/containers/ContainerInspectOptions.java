package podman.client.containers;

import io.vertx.uritemplate.Variables;

public class ContainerInspectOptions {

    private boolean size;

    public boolean size() {
        return size;
    }

    public ContainerInspectOptions setSize(boolean size) {
        this.size = size;
        return this;
    }

    public Variables fillQueryParams(Variables vars) {
        vars.set("size", String.valueOf(size));
        return vars;
    }
}
