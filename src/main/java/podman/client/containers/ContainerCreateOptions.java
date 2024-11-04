package podman.client.containers;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.Map;

public class ContainerCreateOptions {

    private final JsonObject payload = new JsonObject();

    public JsonObject json() {
        return payload;
    }

    public ContainerCreateOptions annotations(Map<String, String> annotations) {
        JsonObject map = new JsonObject();
        annotations.forEach(map::put);
        payload.put("annotations", map);
        return this;
    }

    public ContainerCreateOptions apparmorProfile(String profile) {
        payload.put("apparmor_profile", profile);
        return this;
    }

    public ContainerCreateOptions baseHostsFile(String baseHostsFile) {
        payload.put("base_hosts_file", baseHostsFile);
        return this;
    }

    public ContainerCreateOptions capAdd(List<String> capabilities) {
        payload.put("cap_add", new JsonArray(capabilities));
        return this;
    }

    public ContainerCreateOptions capDrop(List<String> capabilities) {
        payload.put("cap_drop", new JsonArray(capabilities));
        return this;
    }

    public ContainerCreateOptions cgroupParent(String cgroupParent) {
        payload.put("cgroup_parent", cgroupParent);
        return this;
    }

    public ContainerCreateOptions cgroupNS(String nsmode, String value) {
        payload.put("cgroupns", new JsonObject().put("nsmode", nsmode).put("value", value));
        return this;
    }

    public ContainerCreateOptions cgroupsMode(String cgroupsMode) {
        payload.put("cgroups_mode", cgroupsMode);
        return this;
    }

    public ContainerCreateOptions chrootDirectories(List<String> chrootDirectories) {
        payload.put("chroot_directories", new JsonArray(chrootDirectories));
        return this;
    }

    public ContainerCreateOptions cniNetworks(List<String> cniNetworks) {
        payload.put("cni_networks", new JsonArray(cniNetworks));
        return this;
    }

    public ContainerCreateOptions command(List<String> command) {
        payload.put("command", new JsonArray(command));
        return this;
    }

    public ContainerCreateOptions conmonPidFile(String conmonPidFile) {
        payload.put("conmon_pid_file", conmonPidFile);
        return this;
    }

    public ContainerCreateOptions containerCreateCommand(List<String> containerCreateCommand) {
        payload.put("containerCreateCommand", new JsonArray(containerCreateCommand));
        return this;
    }

    public ContainerCreateOptions createWorkingDir(boolean createWorkingDir) {
        payload.put("create_working_dir", createWorkingDir);
        return this;
    }

    public ContainerCreateOptions dependencyContainers(List<String> dependencyContainers) {
        payload.put("dependencyContainers", new JsonArray(dependencyContainers));
        return this;
    }

    public record LinuxDeviceCgroup(String access, boolean allow, long major, long minor, String type) {
        public JsonObject json() {
            return new JsonObject()
                    .put("access", access)
                    .put("allow", allow)
                    .put("major", major)
                    .put("minor", minor)
                    .put("type", type);
        }
    }

    public ContainerCreateOptions deviceCgroupRule(List<LinuxDeviceCgroup> deviceCgroupRules) {
        payload.put(
                "device_cgroup_rule",
                new JsonArray(
                        deviceCgroupRules.stream().map(LinuxDeviceCgroup::json).toList()));
        return this;
    }

    public record LinuxDevice(int fileMode, int gid, long major, long minor, String path, String type, int uid) {
        public JsonObject json() {
            return new JsonObject()
                    .put("fileMode", fileMode)
                    .put("gid", gid)
                    .put("major", major)
                    .put("minor", minor)
                    .put("path", path)
                    .put("type", type)
                    .put("uid", uid);
        }
    }

    public ContainerCreateOptions devices(List<LinuxDevice> devices) {
        payload.put(
                "devices", new JsonArray(devices.stream().map(LinuxDevice::json).toList()));
        return this;
    }

    public ContainerCreateOptions devicesFrom(List<String> devices) {
        payload.put("devices_from", new JsonArray(devices));
        return this;
    }

    public ContainerCreateOptions dnsOption(List<String> dnsOptions) {
        payload.put("dns_option", new JsonArray(dnsOptions));
        return this;
    }

    public ContainerCreateOptions dnsSearch(List<String> dnsSearch) {
        payload.put("dns_search", new JsonArray(dnsSearch));
        return this;
    }

    public ContainerCreateOptions dnsServer(List<String> dnsServer) {
        payload.put("dns_server", new JsonArray(dnsServer));
        return this;
    }

    public ContainerCreateOptions entrypoint(List<String> entrypoint) {
        payload.put("entrypoint", new JsonArray(entrypoint));
        return this;
    }

    public ContainerCreateOptions env(Map<String, String> env) {
        JsonObject map = new JsonObject();
        env.forEach(map::put);
        payload.put("env", map);
        return this;
    }

    public ContainerCreateOptions envHost(boolean envHost) {
        payload.put("env_host", envHost);
        return this;
    }

    public ContainerCreateOptions expose(Map<Integer, String> ports) {
        JsonObject map = new JsonObject();
        ports.forEach((port, value) -> map.put(String.valueOf(port), value));
        payload.put("expose", map);
        return this;
    }

    public ContainerCreateOptions groupEntry(String groupEntry) {
        payload.put("group_entry", groupEntry);
        return this;
    }

    public ContainerCreateOptions groups(List<String> groups) {
        JsonArray array = new JsonArray();
        groups.forEach(array::add);
        payload.put("groups", array);
        return this;
    }

    public ContainerCreateOptions healthCheckOnFailureAction(long action) {
        payload.put("health_check_on_failure_action", action);
        return this;
    }

    public ContainerCreateOptions healthConfig(
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

    public ContainerCreateOptions hostDeviceList(List<LinuxDevice> hostDeviceList) {
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

    public ContainerCreateOptions hostAdd(List<String> hosts) {
        payload.put("hostadd", new JsonArray(hosts));
        return this;
    }

    public ContainerCreateOptions hostname(String hostname) {
        payload.put("hostname", hostname);
        return this;
    }

    public ContainerCreateOptions hostUsers(List<String> users) {
        payload.put("hostusers", new JsonArray(users));
        return this;
    }

    public ContainerCreateOptions httpProxy(boolean httpProxy) {
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

    public ContainerCreateOptions idMappings(
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

    public ContainerCreateOptions image(String image) {
        payload.put("image", image);
        return this;
    }

    public ContainerCreateOptions imageArch(String arch) {
        payload.put("image_arch", arch);
        return this;
    }

    public ContainerCreateOptions imageOs(String os) {
        payload.put("image_os", os);
        return this;
    }

    public ContainerCreateOptions imageVariant(String variant) {
        payload.put("image_variant", variant);
        return this;
    }

    public ContainerCreateOptions imageVolumeMode(String mode) {
        payload.put("image_volume_mode", mode);
        return this;
    }

    public record ImageVolume(String destination, boolean readWrite, String source, String subPath) {
        public JsonObject json() {
            var json = new JsonObject();
            json.put("Destination", destination);
            json.put("ReadWrite", readWrite);
            json.put("Source", source);
            json.put("subPath", subPath);
            return json;
        }
    }

    public ContainerCreateOptions imageVolumes(List<ImageVolume> imageVolumes) {
        JsonArray array = new JsonArray();
        imageVolumes.forEach(volume -> array.add(volume.json()));
        payload.put("image_volumes", array);
        return this;
    }

    public ContainerCreateOptions init(boolean init) {
        payload.put("init", init);
        return this;
    }

    public ContainerCreateOptions initContainerType(String initContainerType) {
        payload.put("init_container_type", initContainerType);
        return this;
    }

    public ContainerCreateOptions initPath(String initPath) {
        payload.put("init_path", initPath);
        return this;
    }

    public ContainerCreateOptions intelRdt(
            String closId, boolean enableCMT, boolean enableMBM, String l3CacheSchema, String memBwSchema) {
        payload.put(
                "intelRdt",
                new JsonObject()
                        .put("closID", closId)
                        .put("enableCMT", enableCMT)
                        .put("enableMBM", enableMBM)
                        .put("l3CacheSchema", l3CacheSchema)
                        .put("memBwSchema", memBwSchema));
        return this;
    }

    public ContainerCreateOptions ipcns(String nsmode, String value) {
        payload.put("ipcns", new JsonObject().put("nsmode", nsmode).put("value", value));
        return this;
    }

    public ContainerCreateOptions labelNested(boolean labelNested) {
        payload.put("label_nested", labelNested);
        return this;
    }

    public ContainerCreateOptions labels(Map<String, String> labels) {
        JsonObject map = new JsonObject();
        labels.forEach(map::put);
        payload.put("labels", map);
        return this;
    }

    public ContainerCreateOptions logConfiguration(String driver, Map<String, String> options, String path, long size) {
        JsonObject map = new JsonObject();
        map.put("driver", driver).put("path", path).put("size", size);
        JsonObject opts = new JsonObject();
        options.forEach(opts::put);
        map.put("options", opts);
        payload.put("log_configuration", map);
        return this;
    }

    public ContainerCreateOptions managePassword(boolean managePassword) {
        payload.put("manage_password", managePassword);
        return this;
    }

    public ContainerCreateOptions mask(List<String> mask) {
        payload.put("mask", new JsonArray(mask));
        return this;
    }

    public record Mount(
            BindOptions bindOptions,
            String consistency,
            boolean readOnly,
            String source,
            String target,
            TmpfsOptions tmpfsOptions,
            String type,
            VolumeOptions volumeOptions) {
        public JsonObject json() {
            return new JsonObject()
                    .put("BindOptions", bindOptions.json())
                    .put("ClusterOptions", new JsonObject()) // this one is an empty Go struct...
                    .put("Consistency", consistency)
                    .put("ReadOnly", readOnly)
                    .put("Source", source)
                    .put("Target", target)
                    .put("TmpfsOptions", tmpfsOptions.json())
                    .put("Type", type)
                    .put("VolumeOptions", volumeOptions.json());
        }
    }

    public record BindOptions(
            boolean createMountPoint,
            boolean nonRecursive,
            String propagation,
            boolean readOnlyForceRecursive,
            boolean readOnlyNonRecursive) {
        public JsonObject json() {
            return new JsonObject()
                    .put("CreateMountPoint", createMountPoint)
                    .put("NonRecursive", nonRecursive)
                    .put("Propagation", propagation)
                    .put("ReadOnlyForceRecursive", readOnlyForceRecursive)
                    .put("ReadOnlyNonRecursive", readOnlyNonRecursive);
        }
    }

    public record TmpfsOptions(int mode, long sizeBytes) {
        public JsonObject json() {
            return new JsonObject().put("Mode", mode).put("SizeBytes", sizeBytes);
        }
    }

    public record VolumeOptions(DriverConfig driverConfig, Map<String, String> labels, boolean noCopy) {
        public JsonObject json() {
            JsonObject map = new JsonObject();
            labels.forEach(map::put);
            return new JsonObject()
                    .put("DriverConfig", driverConfig.json())
                    .put("Labels", map)
                    .put("NoCopy", noCopy);
        }
    }

    public record DriverConfig(String name, Map<String, String> options) {
        public JsonObject json() {
            JsonObject map = new JsonObject();
            options.forEach(map::put);
            return new JsonObject().put("Name", name).put("Options", map);
        }
    }

    public ContainerCreateOptions mounts(List<Mount> mounts) {
        JsonArray array = mounts.stream().map(Mount::json).collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
        payload.put("mounts", array);
        return this;
    }

    public ContainerCreateOptions name(String name) {
        payload.put("name", name);
        return this;
    }

    public ContainerCreateOptions netns(String nsmode, String value) {
        payload.put("netns", new JsonObject().put("nsmode", nsmode).put("value", value));
        return this;
    }

    public ContainerCreateOptions networkOptions(Map<String, List<String>> options) {
        JsonObject map = new JsonObject();
        options.forEach((prop, opts) -> map.put(prop, new JsonArray(opts)));
        payload.put("network_options", map);
        return this;
    }

    public record PerNetworkOptions(
            List<String> aliases, String interfaceName, List<String> staticIps, String staticMac) {
        public JsonObject json() {
            return new JsonObject()
                    .put("aliases", new JsonArray(aliases))
                    .put("interface_name", interfaceName)
                    .put("static_ips", new JsonArray(staticIps))
                    .put("static_mac", staticMac);
        }
    }

    public ContainerCreateOptions networks(Map<String, PerNetworkOptions> networks) {
        JsonObject map = new JsonObject();
        networks.forEach((prop, opts) -> map.put(prop, opts.json()));
        payload.put("Networks", map);
        return this;
    }

    public ContainerCreateOptions noNewPrivileges(boolean noNewPrivileges) {
        payload.put("no_new_privileges", noNewPrivileges);
        return this;
    }

    public ContainerCreateOptions ociRuntime(String ociRuntime) {
        payload.put("oci_runtime", ociRuntime);
        return this;
    }

    public ContainerCreateOptions oomScoreAdj(long oomScoreAdj) {
        payload.put("oom_score_adj", oomScoreAdj);
        return this;
    }

    public record OverlayVolume(String destination, List<String> options, String source) {
        public JsonObject json() {
            return new JsonObject()
                    .put("destination", destination)
                    .put("options", new JsonArray(options))
                    .put("source", source);
        }
    }

    public ContainerCreateOptions overlayVolumes(List<OverlayVolume> volumes) {
        payload.put(
                "overlay_volumes",
                new JsonArray(volumes.stream().map(OverlayVolume::json).toList()));
        return this;
    }

    public ContainerCreateOptions passwdEntry(String passwdEntry) {
        payload.put("passwd_entry", passwdEntry);
        return this;
    }

    public ContainerCreateOptions personality(String domain, List<String> flags) {
        payload.put("personality", new JsonObject().put("domain", domain).put("flags", new JsonArray(flags)));
        return this;
    }

    public ContainerCreateOptions pidns(String nsmode, String value) {
        payload.put("pidns", new JsonObject().put("nsmode", nsmode).put("value", value));
        return this;
    }

    public ContainerCreateOptions pod(String name) {
        payload.put("pod", name);
        return this;
    }

    public record PortMapping(int containerPort, String hostIp, int hostPort, String protocol, int range) {
        public JsonObject json() {
            return new JsonObject()
                    .put("container_port", containerPort)
                    .put("host_ip", hostIp)
                    .put("host_port", hostPort)
                    .put("protocol", protocol)
                    .put("range", range);
        }
    }

    public ContainerCreateOptions portMappings(List<PortMapping> portMappings) {
        payload.put(
                "portmappings",
                new JsonArray(portMappings.stream().map(PortMapping::json).toList()));
        return this;
    }

    public ContainerCreateOptions privileged(boolean privileged) {
        payload.put("privileged", privileged);
        return this;
    }

    public ContainerCreateOptions procfsOpts(List<String> procfsOpts) {
        payload.put("procfs_opts", new JsonArray(procfsOpts));
        return this;
    }

    public ContainerCreateOptions publishImagePorts(boolean publishImagePorts) {
        payload.put("publish_image_ports", publishImagePorts);
        return this;
    }

    public record POSIXRlimit(long hard, long soft, String type) {
        public JsonObject json() {
            return new JsonObject().put("hard", hard).put("soft", soft).put("type", type);
        }
    }

    public ContainerCreateOptions rLimits(List<POSIXRlimit> rLimits) {
        payload.put(
                "r_limits",
                new JsonArray(rLimits.stream().map(POSIXRlimit::json).toList()));
        return this;
    }

    public ContainerCreateOptions rawImageName(String rawImageName) {
        payload.put("raw_image_name", rawImageName);
        return this;
    }

    public ContainerCreateOptions readOnlyFilesystem(boolean readOnlyFilesystem) {
        payload.put("read_only_filesystem", readOnlyFilesystem);
        return this;
    }

    public ContainerCreateOptions readWriteTmpfs(boolean readWriteTmpfs) {
        payload.put("read_write_tmpfs", readWriteTmpfs);
        return this;
    }

    public ContainerCreateOptions remove(boolean remove) {
        payload.put("remove", remove);
        return this;
    }

    public record LinuxBlockIO(
            int leafWeight,
            List<LinuxThrottleDevice> throttleReadBpsDevice,
            List<LinuxThrottleDevice> throttleReadIOPSDevice,
            List<LinuxThrottleDevice> throttleWriteBpsDevice,
            List<LinuxThrottleDevice> throttleWriteIOPSDevice,
            int weight,
            List<LinuxWeightDevice> weightDevice) {
        public JsonObject json() {
            return new JsonObject()
                    .put("leafWeight", leafWeight)
                    .put(
                            "throttleReadBpsDevice",
                            new JsonArray(throttleReadBpsDevice.stream()
                                    .map(LinuxThrottleDevice::json)
                                    .toList()))
                    .put(
                            "throttleReadIOPSDevice",
                            new JsonArray(throttleReadIOPSDevice.stream()
                                    .map(LinuxThrottleDevice::json)
                                    .toList()))
                    .put(
                            "throttleWriteBpsDevice",
                            new JsonArray(throttleWriteBpsDevice.stream()
                                    .map(LinuxThrottleDevice::json)
                                    .toList()))
                    .put(
                            "throttleWriteIOPSDevice",
                            new JsonArray(throttleWriteIOPSDevice.stream()
                                    .map(LinuxThrottleDevice::json)
                                    .toList()))
                    .put("weight", weight)
                    .put(
                            "weightDevice",
                            new JsonArray(weightDevice.stream()
                                    .map(LinuxWeightDevice::json)
                                    .toList()));
        }
    }

    public record LinuxThrottleDevice(long major, long minor, long rate) {
        public JsonObject json() {
            return new JsonObject().put("major", major).put("minor", minor).put("rate", rate);
        }
    }

    public record LinuxWeightDevice(int leafWeight, long major, long minor, int weight) {
        public JsonObject json() {
            return new JsonObject()
                    .put("leafWeight", leafWeight)
                    .put("major", major)
                    .put("minor", minor)
                    .put("weight", weight);
        }
    }

    public record LinuxCPU(
            long burst,
            String cpus,
            long idle,
            String mems,
            long period,
            long quota,
            long realtimePeriod,
            long realtimeRuntime,
            long shares) {
        public JsonObject json() {
            return new JsonObject()
                    .put("burst", burst)
                    .put("cpus", cpus)
                    .put("idle", idle)
                    .put("mems", mems)
                    .put("period", period)
                    .put("quota", quota)
                    .put("realtimePeriod", realtimePeriod)
                    .put("realtimeRuntime", realtimeRuntime)
                    .put("shares", shares);
        }
    }

    public record LinuxHugepageLimit(long limit, String pageSize) {
        public JsonObject json() {
            return new JsonObject().put("limit", limit).put("pageSize", pageSize);
        }
    }

    public record LinuxMemory(
            boolean checkBeforeUpdate,
            boolean disableOOMKiller,
            long kernelTCP,
            long limit,
            long reservation,
            long swap,
            long swappiness,
            boolean useHierarchy) {
        public JsonObject json() {
            return new JsonObject()
                    .put("checkBeforeUpdate", checkBeforeUpdate)
                    .put("disableOOMKiller", disableOOMKiller)
                    .put("kernelTCP", kernelTCP)
                    .put("limit", limit)
                    .put("reservation", reservation)
                    .put("swap", swap)
                    .put("swappiness", swappiness)
                    .put("useHierarchy", useHierarchy);
        }
    }

    public record LinuxNetwork(int classID, List<LinuxInterfacePriority> priorities) {
        public JsonObject json() {
            return new JsonObject()
                    .put("classID", classID)
                    .put(
                            "priorities",
                            new JsonArray(priorities.stream()
                                    .map(LinuxInterfacePriority::json)
                                    .toList()));
        }
    }

    public record LinuxInterfacePriority(String name, int priority) {
        public JsonObject json() {
            return new JsonObject().put("name", name).put("priority", priority);
        }
    }

    public record LinuxPids(long limit) {
        public JsonObject json() {
            return new JsonObject().put("limit", limit);
        }
    }

    public record LinuxRdma(int hcaHandles, int hcaObjects) {
        public JsonObject json() {
            return new JsonObject().put("hcaHandles", hcaHandles).put("hcaObjects", hcaObjects);
        }
    }

    public ContainerCreateOptions resourceLimits(
            LinuxBlockIO blockIO,
            LinuxCPU cpu,
            List<LinuxDeviceCgroup> devices,
            List<LinuxHugepageLimit> hugepageLimits,
            LinuxMemory memory,
            LinuxNetwork network,
            LinuxPids pids,
            Map<String, LinuxRdma> rdma,
            Map<String, String> unified) {
        JsonObject map = new JsonObject();
        map.put("blockIO", blockIO.json());
        map.put("cpu", cpu.json());
        map.put(
                "devices",
                new JsonArray(devices.stream().map(LinuxDeviceCgroup::json).toList()));
        map.put(
                "hugepageLimits",
                new JsonArray(
                        hugepageLimits.stream().map(LinuxHugepageLimit::json).toList()));
        map.put("memory", memory.json());
        map.put("network", network.json());
        map.put("pids", pids.json());

        JsonObject rdmas = new JsonObject();
        rdma.forEach((key, value) -> rdmas.put(key, value.json()));
        map.put("rdma", rdmas);

        JsonObject unifieds = new JsonObject();
        unified.forEach(unifieds::put);
        map.put("unified", unifieds);

        payload.put("resource_limits", map);
        return this;
    }

    public ContainerCreateOptions restartPolicy(String restartPolicy) {
        payload.put("restart_policy", restartPolicy);
        return this;
    }

    public ContainerCreateOptions restartTries(long restartTries) {
        payload.put("restart_tries", restartTries);
        return this;
    }

    public ContainerCreateOptions rootfs(String rootfs) {
        payload.put("rootfs", rootfs);
        return this;
    }

    public ContainerCreateOptions rootfsMapping(String rootfsMapping) {
        payload.put("rootfs_mapping", rootfsMapping);
        return this;
    }

    public ContainerCreateOptions rootfsOverlay(boolean rootfsOverlay) {
        payload.put("rootfs_overlay", rootfsOverlay);
        return this;
    }

    public ContainerCreateOptions rootfsPropagation(String rootfsPropagation) {
        payload.put("rootfs_propagation", rootfsPropagation);
        return this;
    }

    public ContainerCreateOptions sdnotifyMode(String sdnotifyMode) {
        payload.put("sdnotifyMode", sdnotifyMode);
        return this;
    }

    public ContainerCreateOptions seccompPolicy(String seccompPolicy) {
        payload.put("seccomp_policy", seccompPolicy);
        return this;
    }

    public ContainerCreateOptions seccompProfilePath(String seccompProfilePath) {
        payload.put("seccomp_profile_path", seccompProfilePath);
        return this;
    }

    public ContainerCreateOptions secretEnv(Map<String, String> secretEnv) {
        JsonObject map = new JsonObject();
        secretEnv.forEach(map::put);
        payload.put("secret_env", map);
        return this;
    }

    public record Secret(String key, String secret) {
        public JsonObject json() {
            return new JsonObject().put("Key", key).put("Secret", secret);
        }
    }

    public ContainerCreateOptions secrets(List<Secret> secrets) {
        payload.put("secrets", new JsonArray(secrets.stream().map(Secret::json).toList()));
        return this;
    }

    public ContainerCreateOptions selinuxOpts(List<String> selinuxOpts) {
        payload.put("selinux_opts", new JsonArray(selinuxOpts));
        return this;
    }

    public ContainerCreateOptions shmSize(long shmSize) {
        payload.put("shm_size", shmSize);
        return this;
    }

    public ContainerCreateOptions shmSizeSystemd(long shmSizeSystemd) {
        payload.put("shm_size_systemd", shmSizeSystemd);
        return this;
    }

    public record StartupHealthCheck(
            long interval, long retries, long startPeriod, long successes, List<String> test, long timeout) {
        public JsonObject json() {
            return new JsonObject()
                    .put("Interval", interval)
                    .put("Retries", retries)
                    .put("StartPeriod", startPeriod)
                    .put("Successes", successes)
                    .put("Test", new JsonArray(test))
                    .put("Timeout", timeout);
        }
    }

    public ContainerCreateOptions startupHealthConfig(StartupHealthCheck startupHealthCheck) {
        payload.put("startupHealthConfig", startupHealthCheck.json());
        return this;
    }

    public ContainerCreateOptions stdin(boolean stdin) {
        payload.put("stdin", stdin);
        return this;
    }

    public ContainerCreateOptions stopSignal(long stopSignal) {
        payload.put("stop_signal", stopSignal);
        return this;
    }

    public ContainerCreateOptions stopTimeout(long stopTimeout) {
        payload.put("stop_timeout", stopTimeout);
        return this;
    }

    public ContainerCreateOptions storageOpts(Map<String, String> storageOpts) {
        JsonObject map = new JsonObject();
        storageOpts.forEach(map::put);
        payload.put("storage_opts", map);
        return this;
    }

    public ContainerCreateOptions sysctl(Map<String, String> sysctl) {
        JsonObject map = new JsonObject();
        sysctl.forEach(map::put);
        payload.put("sysctl", map);
        return this;
    }

    public ContainerCreateOptions systemd(String systemd) {
        payload.put("systemd", systemd);
        return this;
    }

    public ContainerCreateOptions terminal(boolean terminal) {
        payload.put("terminal", terminal);
        return this;
    }

    public ContainerCreateOptions throttleReadBpsDevice(Map<String, LinuxThrottleDevice> throttleReadBpsDevice) {
        JsonObject map = new JsonObject();
        throttleReadBpsDevice.forEach(map::put);
        payload.put("throttleReadBpsDevice", map);
        return this;
    }

    public ContainerCreateOptions throttleReadIOPSDevice(Map<String, LinuxThrottleDevice> throttleReadIOPSDevice) {
        JsonObject map = new JsonObject();
        throttleReadIOPSDevice.forEach(map::put);
        payload.put("throttleReadIOPSDevice", map);
        return this;
    }

    public ContainerCreateOptions throttleWriteBpsDevice(Map<String, LinuxThrottleDevice> throttleWriteBpsDevice) {
        JsonObject map = new JsonObject();
        throttleWriteBpsDevice.forEach(map::put);
        payload.put("throttleWriteBpsDevice", map);
        return this;
    }

    public ContainerCreateOptions throttleWriteIOPSDevice(Map<String, LinuxThrottleDevice> throttleWriteIOPSDevice) {
        JsonObject map = new JsonObject();
        throttleWriteIOPSDevice.forEach(map::put);
        payload.put("throttleWriteIOPSDevice", map);
        return this;
    }

    public ContainerCreateOptions timeout(long timeout) {
        payload.put("timeout", timeout);
        return this;
    }

    public ContainerCreateOptions timezone(String timezone) {
        payload.put("timezone", timezone);
        return this;
    }

    public ContainerCreateOptions umask(String umask) {
        payload.put("umask", umask);
        return this;
    }

    public ContainerCreateOptions unified(Map<String, String> unified) {
        JsonObject map = new JsonObject();
        unified.forEach(map::put);
        payload.put("unified", map);
        return this;
    }

    public ContainerCreateOptions unmask(List<String> unmask) {
        payload.put("unmask", new JsonArray(unmask));
        return this;
    }

    public ContainerCreateOptions unsetenv(List<String> unsetenv) {
        payload.put("unsetenv", new JsonArray(unsetenv));
        return this;
    }

    public ContainerCreateOptions unsetenvall(boolean unsetenvall) {
        payload.put("unsetenvall", unsetenvall);
        return this;
    }

    public ContainerCreateOptions useImageHosts(boolean useImageHosts) {
        payload.put("use_image_hosts", useImageHosts);
        return this;
    }

    public ContainerCreateOptions useImageResolveConf(boolean useImageResolveConf) {
        payload.put("use_image_resolve_conf", useImageResolveConf);
        return this;
    }

    public ContainerCreateOptions user(String user) {
        payload.put("user", user);
        return this;
    }

    public ContainerCreateOptions userns(String nsmode, String value) {
        payload.put("userns", new JsonObject().put("nsmode", nsmode).put("value", value));
        return this;
    }

    public ContainerCreateOptions utsns(String nsmode, String value) {
        payload.put("utsns", new JsonObject().put("nsmode", nsmode).put("value", value));
        return this;
    }

    public ContainerCreateOptions volatile_(boolean volatile_) {
        payload.put("volatile", volatile_);
        return this;
    }

    public record NamedVolume(String dest, boolean isAnonymous, String name, List<String> options, String subPath) {
        public JsonObject json() {
            return new JsonObject()
                    .put("Dest", dest)
                    .put("IsAnonymous", isAnonymous)
                    .put("Name", name)
                    .put("Options", new JsonArray(options))
                    .put("SubPath", subPath);
        }
    }

    public ContainerCreateOptions volumes(List<NamedVolume> volumes) {
        payload.put(
                "volumes", new JsonArray(volumes.stream().map(NamedVolume::json).toList()));
        return this;
    }

    public ContainerCreateOptions volumesFrom(List<String> volumesFrom) {
        payload.put("volumes_from", new JsonArray(volumesFrom));
        return this;
    }

    public ContainerCreateOptions weightDevice(Map<String, LinuxWeightDevice> weightDevice) {
        JsonObject map = new JsonObject();
        weightDevice.forEach((key, value) -> map.put(key, value.json()));
        payload.put("weightDevice", map);
        return this;
    }

    public ContainerCreateOptions workDir(String workDir) {
        payload.put("work_dir", workDir);
        return this;
    }
}
