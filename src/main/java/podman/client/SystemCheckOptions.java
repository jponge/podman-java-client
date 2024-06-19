package podman.client;

import io.vertx.ext.web.client.HttpRequest;

public class SystemCheckOptions {

    private boolean quick = true;
    private boolean repair = false;
    private boolean repairLossy = false;
    private String unreferencedLayerMaxAge;

    public boolean quick() {
        return quick;
    }

    public SystemCheckOptions setQuick(boolean quick) {
        this.quick = quick;
        return this;
    }

    public boolean repair() {
        return repair;
    }

    public SystemCheckOptions setRepair(boolean repair) {
        this.repair = repair;
        return this;
    }

    public boolean repairLossy() {
        return repairLossy;
    }

    public SystemCheckOptions setRepairLossy(boolean repairLossy) {
        this.repairLossy = repairLossy;
        return this;
    }

    public String unreferencedLayerMaxAge() {
        return unreferencedLayerMaxAge;
    }

    public SystemCheckOptions setUnreferencedLayerMaxAge(String unreferencedLayerMaxAge) {
        this.unreferencedLayerMaxAge = unreferencedLayerMaxAge;
        return this;
    }

    public <T> HttpRequest<T> fillQueryParams(HttpRequest<T> request) {
        if (unreferencedLayerMaxAge != null) {
            request.addQueryParam("unreferenced_layer_max_age", unreferencedLayerMaxAge);
        }
        return request.addQueryParam("quick", String.valueOf(quick))
                .addQueryParam("repair", String.valueOf(repair))
                .addQueryParam("repair_lossy", String.valueOf(repairLossy));
    }
}
