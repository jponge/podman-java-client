package podman.client.containers;

import io.vertx.uritemplate.Variables;

public class ContainerGetLogsOptions {

    private boolean follow;
    private String since;
    private boolean stderr = true;
    private boolean stdout = true;
    private String tail;
    private boolean timestamps;
    private String until;

    public boolean follow() {
        return follow;
    }

    public ContainerGetLogsOptions setFollow(boolean follow) {
        this.follow = follow;
        return this;
    }

    public String since() {
        return since;
    }

    public ContainerGetLogsOptions setSince(String since) {
        this.since = since;
        return this;
    }

    public boolean stderr() {
        return stderr;
    }

    public ContainerGetLogsOptions setStderr(boolean stderr) {
        this.stderr = stderr;
        return this;
    }

    public boolean stdout() {
        return stdout;
    }

    public ContainerGetLogsOptions setStdout(boolean stdout) {
        this.stdout = stdout;
        return this;
    }

    public String tail() {
        return tail;
    }

    public ContainerGetLogsOptions setTail(String tail) {
        this.tail = tail;
        return this;
    }

    public boolean timestamps() {
        return timestamps;
    }

    public ContainerGetLogsOptions setTimestamps(boolean timestamps) {
        this.timestamps = timestamps;
        return this;
    }

    public String until() {
        return until;
    }

    public ContainerGetLogsOptions setUntil(String until) {
        this.until = until;
        return this;
    }

    public Variables fillQueryParams(Variables vars) {
        vars.set("follow", String.valueOf(follow));
        vars.set("stderr", String.valueOf(stderr));
        vars.set("stdout", String.valueOf(stdout));
        vars.set("timestamps", String.valueOf(timestamps));
        if (since != null) {
            vars.set("since", since);
        }
        if (tail != null) {
            vars.set("tail", tail);
        }
        if (until != null) {
            vars.set("until", until);
        }
        return vars;
    }
}
