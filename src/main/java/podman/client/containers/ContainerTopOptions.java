package podman.client.containers;

import io.vertx.uritemplate.Variables;

public class ContainerTopOptions {

    private int delay = 5;

    private String[] psArgs;

    private boolean stream;

    public int delay() {
        return delay;
    }

    public ContainerTopOptions setDelay(int delay) {
        this.delay = delay;
        return this;
    }

    public String[] getPsArgs() {
        return psArgs;
    }

    public ContainerTopOptions setPsArgs(String[] psArgs) {
        this.psArgs = psArgs;
        return this;
    }

    public boolean stream() {
        return stream;
    }

    public ContainerTopOptions setStream(boolean stream) {
        this.stream = stream;
        return this;
    }

    public Variables fillQueryParams(Variables vars) {
        vars.set("delay", String.valueOf(delay));
        vars.set("psArgs", String.valueOf(psArgs));
        vars.set("stream", String.valueOf(stream));
        return vars;
    }
}
