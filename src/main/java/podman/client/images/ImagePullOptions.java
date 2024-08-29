package podman.client.images;

import io.vertx.uritemplate.Variables;

public class ImagePullOptions {

    private boolean allTags = false;
    private String arch;
    private boolean compatMode = false;
    private String os;
    private String policy = "always";
    private boolean quiet = false;
    private boolean tlsVerify = true;
    private String variant;
    private String registryAuth;

    public boolean allTags() {
        return allTags;
    }

    public ImagePullOptions setAllTags(boolean allTags) {
        this.allTags = allTags;
        return this;
    }

    public String arch() {
        return arch;
    }

    public ImagePullOptions setArch(String arch) {
        this.arch = arch;
        return this;
    }

    public boolean compatMode() {
        return compatMode;
    }

    public ImagePullOptions setCompatMode(boolean compatMode) {
        this.compatMode = compatMode;
        return this;
    }

    public String os() {
        return os;
    }

    public ImagePullOptions setOs(String os) {
        this.os = os;
        return this;
    }

    public String policy() {
        return policy;
    }

    public ImagePullOptions setPolicy(String policy) {
        this.policy = policy;
        return this;
    }

    public boolean quiet() {
        return quiet;
    }

    public ImagePullOptions setQuiet(boolean quiet) {
        this.quiet = quiet;
        return this;
    }

    public boolean tlsVerify() {
        return tlsVerify;
    }

    public ImagePullOptions setTlsVerify(boolean tlsVerify) {
        this.tlsVerify = tlsVerify;
        return this;
    }

    public String variant() {
        return variant;
    }

    public ImagePullOptions setVariant(String variant) {
        this.variant = variant;
        return this;
    }

    public String registryAuth() {
        return registryAuth;
    }

    public ImagePullOptions setRegistryAuth(String registryAuth) {
        this.registryAuth = registryAuth;
        return this;
    }

    public Variables fillQueryParams(Variables vars) {
        vars.set("allTags", String.valueOf(allTags));
        if (arch != null) {
            vars.set("Arch", arch);
        }
        vars.set("compatMode", String.valueOf(compatMode));
        if (os != null) {
            vars.set("OS", os);
        }
        vars.set("policy", policy);
        vars.set("quiet", String.valueOf(quiet));
        vars.set("tlsVerify", String.valueOf(tlsVerify));
        if (variant != null) {
            vars.set("Variant", variant);
        }
        return vars;
    }
}
