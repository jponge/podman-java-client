package podman.client.containers;

import io.vertx.uritemplate.Variables;

public class ContainerDeleteOptions {

    private boolean depend;
    private boolean force;
    private boolean ignore;
    private int timeout = 10;
    private boolean deleteVolumes;

    public boolean depend() {
        return depend;
    }

    public ContainerDeleteOptions setDepend(boolean depend) {
        this.depend = depend;
        return this;
    }

    public boolean force() {
        return force;
    }

    public ContainerDeleteOptions setForce(boolean force) {
        this.force = force;
        return this;
    }

    public boolean ignore() {
        return ignore;
    }

    public ContainerDeleteOptions setIgnore(boolean ignore) {
        this.ignore = ignore;
        return this;
    }

    public int timeout() {
        return timeout;
    }

    public ContainerDeleteOptions setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public boolean deleteVolumes() {
        return deleteVolumes;
    }

    public ContainerDeleteOptions setDeleteVolumes(boolean deleteVolumes) {
        this.deleteVolumes = deleteVolumes;
        return this;
    }

    public Variables fillQueryParams(Variables vars) {
        vars.set("depend", String.valueOf(depend));
        vars.set("force", String.valueOf(force));
        vars.set("ignore", String.valueOf(ignore));
        vars.set("timeout", String.valueOf(timeout));
        vars.set("v", String.valueOf(deleteVolumes));
        return vars;
    }
}
