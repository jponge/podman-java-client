package podman.client.containers;

import io.vertx.uritemplate.Variables;

public class ContainerInspectOptions {

    private boolean size;
    private String since;
    private boolean stderr = true;
    private boolean stdout = true;
    private String tail;
    private boolean timestamps;
    private String until;

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
