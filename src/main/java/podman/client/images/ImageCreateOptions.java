package podman.client.images;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.Map;

public class ImageCreateOptions {

    private final JsonObject payload = new JsonObject();

    public JsonObject json() {
        return payload;
    }

    public ImageCreateOptions annotations(Map<String, String> annotations) {
        JsonObject map = new JsonObject();
        annotations.forEach(map::put);
        payload.put("annotations", map);
        return this;
    }

    public ImageCreateOptions apparmorProfile(String profile) {
        payload.put("apparmor_profile", profile);
        return this;
    }

    public ImageCreateOptions baseHostsFile(String baseHostsFile) {
        payload.put("base_hosts_file", baseHostsFile);
        return this;
    }

    public ImageCreateOptions capAdd(List<String> capabilities) {
        payload.put("cap_add", new JsonArray(capabilities));
        return this;
    }

    public ImageCreateOptions capDrop(List<String> capabilities) {
        payload.put("cap_drop", new JsonArray(capabilities));
        return this;
    }

    public ImageCreateOptions cgroupParent(String cgroupParent) {
        payload.put("cgroup_parent", cgroupParent);
        return this;
    }

    public ImageCreateOptions cgroupNS(String nsmode, String value) {
        payload.put("cgroupns", new JsonObject().put("nsmode", nsmode).put("value", value));
        return this;
    }

    public ImageCreateOptions cgroupsMode(String cgroupsMode) {
        payload.put("cgroups_mode", cgroupsMode);
        return this;
    }

    public ImageCreateOptions chrootDirectories(List<String> chrootDirectories) {
        payload.put("chroot_directories", new JsonArray(chrootDirectories));
        return this;
    }

    public ImageCreateOptions cniNetworks(List<String> cniNetworks) {
        payload.put("cni_networks", new JsonArray(cniNetworks));
        return this;
    }

    public ImageCreateOptions command(List<String> command) {
        payload.put("command", new JsonArray(command));
        return this;
    }

    public ImageCreateOptions conmonPidFile(String conmonPidFile) {
        payload.put("conmon_pid_file", conmonPidFile);
        return this;
    }

    public ImageCreateOptions containerCreateCommand(List<String> containerCreateCommand) {
        payload.put("containerCreateCommand", new JsonArray(containerCreateCommand));
        return this;
    }

    public ImageCreateOptions createWorkingDir(boolean createWorkingDir) {
        payload.put("create_working_dir", createWorkingDir);
        return this;
    }

    public ImageCreateOptions dependencyContainers(List<String> dependencyContainers) {
        payload.put("dependencyContainers", new JsonArray(dependencyContainers));
        return this;
    }

    public record LinuxDeviceCgroup(String access, boolean allow, long major, long minor, String type) {}

    public ImageCreateOptions deviceCgroupRule(List<LinuxDeviceCgroup> deviceCgroupRules) {
        JsonArray array = new JsonArray();
        for (LinuxDeviceCgroup rule : deviceCgroupRules) {
            array.add(new JsonObject()
                    .put("access", rule.access)
                    .put("allow", rule.allow)
                    .put("major", rule.major)
                    .put("minor", rule.minor)
                    .put("type", rule.type));
        }
        payload.put("device_cgroup_rule", array);
        return this;
    }

    public record LinuxDevice(int fileMode, int gid, long major, long minor, String path, String type, int uid) {}

    public ImageCreateOptions devices(List<LinuxDevice> devices) {
        JsonArray array = new JsonArray();
        for (LinuxDevice device : devices) {
            array.add(new JsonObject()
                    .put("fileMode", device.fileMode)
                    .put("gid", device.gid)
                    .put("major", device.major)
                    .put("minor", device.minor)
                    .put("path", device.path)
                    .put("type", device.type)
                    .put("uid", device.uid));
        }
        payload.put("devices", array);
        return this;
    }

    public ImageCreateOptions devicesFrom(List<String> devices) {
        payload.put("devices_from", new JsonArray(devices));
        return this;
    }

    public ImageCreateOptions dnsOption(List<String> dnsOptions) {
        payload.put("dns_option", new JsonArray(dnsOptions));
        return this;
    }

    public ImageCreateOptions dnsSearch(List<String> dnsSearch) {
        payload.put("dns_search", new JsonArray(dnsSearch));
        return this;
    }

    public ImageCreateOptions dnsServer(List<String> dnsServer) {
        payload.put("dns_server", new JsonArray(dnsServer));
        return this;
    }

    public ImageCreateOptions entrypoint(List<String> entrypoint) {
        payload.put("entrypoint", new JsonArray(entrypoint));
        return this;
    }

    public ImageCreateOptions env(Map<String, String> env) {
        JsonObject map = new JsonObject();
        env.forEach(map::put);
        payload.put("env", map);
        return this;
    }

    public ImageCreateOptions envHost(boolean envHost) {
        payload.put("env_host", envHost);
        return this;
    }

    public ImageCreateOptions expose(Map<Integer, String> ports) {
        JsonObject map = new JsonObject();
        ports.forEach((port, value) -> map.put(String.valueOf(port), value));
        payload.put("expose", map);
        return this;
    }

    public ImageCreateOptions groupEntry(String groupEntry) {
        payload.put("group_entry", groupEntry);
        return this;
    }

    public ImageCreateOptions groups(List<String> groups) {
        JsonArray array = new JsonArray();
        groups.forEach(array::add);
        payload.put("groups", array);
        return this;
    }

    public ImageCreateOptions healthCheckOnFailureAction(long action) {
        payload.put("health_check_on_failure_action", action);
        return this;
    }

    public ImageCreateOptions healthConfig(
            long interval, long retries, long startPeriod, List<String> test, long timeout) {
        payload.put(
                "healthconfig",
                new JsonObject()
                        .put("Interval", interval)
                        .put("Retries", retries)
                        .put("StartPeriod", startPeriod)
                        .put("Test", new JsonArray(test))
                        .put("Timeout", timeout));
        return this;
    }

    public ImageCreateOptions hostDeviceList(List<LinuxDevice> hostDeviceList) {
        JsonArray array = new JsonArray();
        hostDeviceList.forEach(device -> array.add(new JsonObject()
                .put("fileMode", device.fileMode)
                .put("gid", device.gid)
                .put("major", device.major)
                .put("minor", device.minor)
                .put("path", device.path)
                .put("type", device.type)
                .put("uid", device.uid)));
        payload.put("host_device_list", array);
        return this;
    }

    public ImageCreateOptions hostAdd(List<String> hosts) {
        payload.put("hostadd", new JsonArray(hosts));
        return this;
    }

    public ImageCreateOptions hostname(String hostname) {
        payload.put("hostname", hostname);
        return this;
    }

    public ImageCreateOptions hostUsers(List<String> users) {
        payload.put("hostusers", new JsonArray(users));
        return this;
    }

    public ImageCreateOptions httpProxy(boolean httpProxy) {
        payload.put("httpproxy", httpProxy);
        return this;
    }

    public record IDMap(long containerId, long hostId, long size) {

        public JsonObject json() {
            return new JsonObject()
                    .put("container_id", containerId)
                    .put("host_id", hostId)
                    .put("size", size);
        }
    }

    public record AutoUserNsOptions(
            List<IDMap> additionalGidMappings,
            List<IDMap> additionalUidMappings,
            String groupFile,
            int initialSize,
            String passwdFile,
            int size) {

        public JsonObject json() {
            JsonObject map = new JsonObject();
            map.put(
                    "AdditionalGIDMappings",
                    new JsonArray(
                            additionalGidMappings.stream().map(IDMap::json).toList()));
            map.put(
                    "AdditionalUIDMappings",
                    new JsonArray(
                            additionalUidMappings.stream().map(IDMap::json).toList()));
            return map.put("GroupFile", groupFile)
                    .put("InitialSize", initialSize)
                    .put("PasswdFile", passwdFile)
                    .put("Size", size);
        }
    }

    public ImageCreateOptions idMappings(
            boolean autoUserNs,
            AutoUserNsOptions autoUserNsOpts,
            IDMap gidMap,
            boolean hostGidMapping,
            boolean hostUidMapping,
            IDMap uidMap) {
        JsonObject map = new JsonObject();
        map.put("AutoUserNs", autoUserNs);
        map.put("AutoUserNsOpts", autoUserNsOpts.json());
        map.put("GIDMap", gidMap.json());
        map.put("HostGIDMapping", hostGidMapping);
        map.put("HostUIDMapping", hostUidMapping);
        map.put("UIDMap", uidMap.json());
        return this;
    }

    public ImageCreateOptions image(String image) {
        payload.put("image", image);
        return this;
    }

    public ImageCreateOptions imageArch(String arch) {
        payload.put("image_arch", arch);
        return this;
    }

    public ImageCreateOptions imageOs(String os) {
        payload.put("image_os", os);
        return this;
    }

    public ImageCreateOptions imageVariant(String variant) {
        payload.put("image_variant", variant);
        return this;
    }

    public ImageCreateOptions imageVolumeMode(String mode) {
        payload.put("image_volume_mode", mode);
        return this;
    }

    public record ImageVolume(
            String destination,
            boolean readWrite,
            String source
    ) {}

    public ImageCreateOptions imageVolumes(List<ImageVolume> imageVolumes) {
        JsonArray array = new JsonArray();
        for (ImageVolume volume : imageVolumes) {
            array.add(new JsonObject()
                    .put("Destination", volume.destination)
                    .put("ReadWrite", volume.readWrite)
                    .put("Source", volume.source));
        }
        payload.put("image_volumes", array);
        return this;
    }

    public ImageCreateOptions init(boolean init) {
        payload.put("init", init);
        return this;
    }

    public ImageCreateOptions initContainerType(String initContainerType) {
        payload.put("init_container_type", initContainerType);
        return this;
    }

    public ImageCreateOptions initPath(String initPath) {
        payload.put("init_path", initPath);
        return this;
    }

    public ImageCreateOptions intelRdt(String closId, boolean enableCMT, boolean enableMBM, String l3CacheSchema, String memBwSchema) {
        payload.put("intelRdt", new JsonObject()
                .put("closID", closId)
                .put("enableCMT", enableCMT)
                .put("enableMBM", enableMBM)
                .put("l3CacheSchema", l3CacheSchema)
                .put("memBwSchema", memBwSchema));
        return this;
    }

    public ImageCreateOptions ipcns(String nsmode, String value) {
        payload.put("ipcns", new JsonObject()
                .put("nsmode", nsmode)
                .put("value", value));
        return this;
    }

    public ImageCreateOptions labelNested(boolean labelNested) {
        payload.put("label_nested", labelNested);
        return this;
    }

    public ImageCreateOptions labels(Map<String, String> labels) {
        JsonObject map = new JsonObject();
        labels.forEach(map::put);
        payload.put("labels", map);
        return this;
    }


}
