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

    public ImageCreateOptions deviceCgroupRule(List<LinuxDeviceCgroup> deviceCgroupRules) {
        payload.put("device_cgroup_rule", new JsonArray(deviceCgroupRules.stream().map(LinuxDeviceCgroup::json).toList()));
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

    public ImageCreateOptions devices(List<LinuxDevice> devices) {
        payload.put("devices", new JsonArray(devices.stream().map(LinuxDevice::json).toList()));
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

    public record ImageVolume(String destination, boolean readWrite, String source) {
    }

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

    public ImageCreateOptions intelRdt(
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

    public ImageCreateOptions ipcns(String nsmode, String value) {
        payload.put("ipcns", new JsonObject().put("nsmode", nsmode).put("value", value));
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

    public ImageCreateOptions logConfiguration(String driver, Map<String, String> options, String path, long size) {
        JsonObject map = new JsonObject();
        map.put("driver", driver).put("path", path).put("size", size);
        JsonObject opts = new JsonObject();
        options.forEach(opts::put);
        map.put("options", opts);
        payload.put("log_configuration", map);
        return this;
    }

    public ImageCreateOptions managePassword(boolean managePassword) {
        payload.put("manage_password", managePassword);
        return this;
    }

    public ImageCreateOptions mask(List<String> mask) {
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

    public ImageCreateOptions mounts(List<Mount> mounts) {
        JsonArray array = mounts.stream().map(Mount::json).collect(JsonArray::new, JsonArray::add, JsonArray::addAll);
        payload.put("mounts", array);
        return this;
    }

    public ImageCreateOptions name(String name) {
        payload.put("name", name);
        return this;
    }

    public ImageCreateOptions netns(String nsmode, String value) {
        payload.put("netns", new JsonObject().put("nsmode", nsmode).put("value", value));
        return this;
    }

    public ImageCreateOptions networkOptions(Map<String, List<String>> options) {
        JsonObject map = new JsonObject();
        options.forEach((prop, opts) -> map.put(prop, new JsonArray(opts)));
        payload.put("network_options", map);
        return this;
    }

    public record PerNetworkOptions(List<String> aliases,
                                    String interfaceName,
                                    List<String> staticIps,
                                    String staticMac) {
        public JsonObject json() {
            return new JsonObject()
                    .put("aliases", new JsonArray(aliases))
                    .put("interface_name", interfaceName)
                    .put("static_ips", new JsonArray(staticIps))
                    .put("static_mac", staticMac);
        }
    }

    public ImageCreateOptions networks(Map<String, PerNetworkOptions> networks) {
        JsonObject map = new JsonObject();
        networks.forEach((prop, opts) -> map.put(prop, opts.json()));
        payload.put("Networks", map);
        return this;
    }

    public ImageCreateOptions noNewPrivileges(boolean noNewPrivileges) {
        payload.put("no_new_privileges", noNewPrivileges);
        return this;
    }

    public ImageCreateOptions ociRuntime(String ociRuntime) {
        payload.put("oci_runtime", ociRuntime);
        return this;
    }

    public ImageCreateOptions oomScoreAdj(long oomScoreAdj) {
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

    public ImageCreateOptions overlayVolumes(List<OverlayVolume> volumes) {
        payload.put("overlay_volumes", new JsonArray(volumes.stream().map(OverlayVolume::json).toList()));
        return this;
    }

    public ImageCreateOptions passwdEntry(String passwdEntry) {
        payload.put("passwd_entry", passwdEntry);
        return this;
    }

    public ImageCreateOptions personality(String domain, List<String> flags) {
        payload.put("personality", new JsonObject().put("domain", domain).put("flags", new JsonArray(flags)));
        return this;
    }

    public ImageCreateOptions pidns(String nsmode, String value) {
        payload.put("pidns", new JsonObject().put("nsmode", nsmode).put("value", value));
        return this;
    }

    public ImageCreateOptions pod(String name) {
        payload.put("pod", name);
        return this;
    }

    public record PortMapping(int containerPort,
                              String hostIp,
                              int hostPort,
                              String protocol,
                              int range) {
        public JsonObject json() {
            return new JsonObject()
                    .put("container_port", containerPort)
                    .put("host_ip", hostIp)
                    .put("host_port", hostPort)
                    .put("protocol", protocol)
                    .put("range", range);
        }
    }

    public ImageCreateOptions portMappings(List<PortMapping> portMappings) {
        payload.put("portmappings", new JsonArray(portMappings.stream().map(PortMapping::json).toList()));
        return this;
    }

    public ImageCreateOptions privileged(boolean privileged) {
        payload.put("privileged", privileged);
        return this;
    }

    public ImageCreateOptions procfsOpts(List<String> procfsOpts) {
        payload.put("procfs_opts", new JsonArray(procfsOpts));
        return this;
    }

    public ImageCreateOptions publishImagePorts(boolean publishImagePorts) {
        payload.put("publish_image_ports", publishImagePorts);
        return this;
    }

    public record POSIXRlimit(long hard, long soft, String type) {
        public JsonObject json() {
            return new JsonObject()
                    .put("hard", hard)
                    .put("soft", soft)
                    .put("type", type);
        }
    }

    public ImageCreateOptions rLimits(List<POSIXRlimit> rLimits) {
        payload.put("r_limits", new JsonArray(rLimits.stream().map(POSIXRlimit::json).toList()));
        return this;
    }

    public ImageCreateOptions rawImageName(String rawImageName) {
        payload.put("raw_image_name", rawImageName);
        return this;
    }

    public ImageCreateOptions readOnlyFilesystem(boolean readOnlyFilesystem) {
        payload.put("read_only_filesystem", readOnlyFilesystem);
        return this;
    }

    public ImageCreateOptions readWriteTmpfs(boolean readWriteTmpfs) {
        payload.put("read_write_tmpfs", readWriteTmpfs);
        return this;
    }

    public ImageCreateOptions remove(boolean remove) {
        payload.put("remove", remove);
        return this;
    }

    public record LinuxBlockIO(int leafWeight,
                               List<LinuxThrottleDevice> throttleReadBpsDevice,
                               List<LinuxThrottleDevice> throttleReadIOPSDevice,
                               List<LinuxThrottleDevice> throttleWriteBpsDevice,
                               List<LinuxThrottleDevice> throttleWriteIOPSDevice,
                               int weight,
                               List<LinuxWeightDevice> weightDevice) {
        public JsonObject json() {
            return new JsonObject()
                    .put("leafWeight", leafWeight)
                    .put("throttleReadBpsDevice", new JsonArray(throttleReadBpsDevice.stream().map(LinuxThrottleDevice::json).toList()))
                    .put("throttleReadIOPSDevice", new JsonArray(throttleReadIOPSDevice.stream().map(LinuxThrottleDevice::json).toList()))
                    .put("throttleWriteBpsDevice", new JsonArray(throttleWriteBpsDevice.stream().map(LinuxThrottleDevice::json).toList()))
                    .put("throttleWriteIOPSDevice", new JsonArray(throttleWriteIOPSDevice.stream().map(LinuxThrottleDevice::json).toList()))
                    .put("weight", weight)
                    .put("weightDevice", new JsonArray(weightDevice.stream().map(LinuxWeightDevice::json).toList()));
        }
    }

    public record LinuxThrottleDevice(long major, long minor, long rate) {
        public JsonObject json() {
            return new JsonObject()
                    .put("major", major)
                    .put("minor", minor)
                    .put("rate", rate);
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

    public record LinuxCPU(long burst,
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
            return new JsonObject()
                    .put("limit", limit)
                    .put("pageSize", pageSize);
        }
    }

    public record LinuxMemory(boolean checkBeforeUpdate,
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
                    .put("priorities", new JsonArray(priorities.stream().map(LinuxInterfacePriority::json).toList()));
        }
    }

    public record LinuxInterfacePriority(String name, int priority) {
        public JsonObject json() {
            return new JsonObject()
                    .put("name", name)
                    .put("priority", priority);
        }
    }

    public record LinuxPids(long limit) {
        public JsonObject json() {
            return new JsonObject()
                    .put("limit", limit);
        }
    }

    public record LinuxRdma(int hcaHandles, int hcaObjects) {
        public JsonObject json() {
            return new JsonObject()
                    .put("hcaHandles", hcaHandles)
                    .put("hcaObjects", hcaObjects);
        }
    }

    public ImageCreateOptions resourceLimits(
            LinuxBlockIO blockIO,
            LinuxCPU cpu,
            List<LinuxDeviceCgroup> devices,
            List<LinuxHugepageLimit> hugepageLimits,
            LinuxMemory memory,
            LinuxNetwork network,
            LinuxPids pids,
            Map<String, LinuxRdma> rdma,
            Map<String, String> unified
    ) {
        JsonObject map = new JsonObject();
        map.put("blockIO", blockIO.json());
        map.put("cpu", cpu.json());
        map.put("devices", new JsonArray(devices.stream().map(LinuxDeviceCgroup::json).toList()));
        map.put("hugepageLimits", new JsonArray(hugepageLimits.stream().map(LinuxHugepageLimit::json).toList()));
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

    public ImageCreateOptions restartPolicy(String restartPolicy) {
        payload.put("restart_policy", restartPolicy);
        return this;
    }

    public ImageCreateOptions restartTries(long restartTries) {
        payload.put("restart_tries", restartTries);
        return this;
    }

    public ImageCreateOptions rootfs(String rootfs) {
        payload.put("rootfs", rootfs);
        return this;
    }

    public ImageCreateOptions rootfsMapping(String rootfsMapping) {
        payload.put("rootfs_mapping", rootfsMapping);
        return this;
    }

    public ImageCreateOptions rootfsOverlay(boolean rootfsOverlay) {
        payload.put("rootfs_overlay", rootfsOverlay);
        return this;
    }

    public ImageCreateOptions rootfsPropagation(String rootfsPropagation) {
        payload.put("rootfs_propagation", rootfsPropagation);
        return this;
    }

    public ImageCreateOptions sdnotifyMode(String sdnotifyMode) {
        payload.put("sdnotifyMode", sdnotifyMode);
        return this;
    }

    public ImageCreateOptions seccompPolicy(String seccompPolicy) {
        payload.put("seccomp_policy", seccompPolicy);
        return this;
    }

    public ImageCreateOptions seccompProfilePath(String seccompProfilePath) {
        payload.put("seccomp_profile_path", seccompProfilePath);
        return this;
    }

    public ImageCreateOptions secretEnv(Map<String, String> secretEnv) {
        JsonObject map = new JsonObject();
        secretEnv.forEach(map::put);
        payload.put("secret_env", map);
        return this;
    }

    public record Secret(String key, String secret) {
        public JsonObject json() {
            return new JsonObject()
                    .put("Key", key)
                    .put("Secret", secret);
        }
    }

    public ImageCreateOptions secrets(List<Secret> secrets) {
        payload.put("secrets", new JsonArray(secrets.stream().map(Secret::json).toList()));
        return this;
    }

    public ImageCreateOptions selinuxOpts(List<String> selinuxOpts) {
        payload.put("selinux_opts", new JsonArray(selinuxOpts));
        return this;
    }

    public ImageCreateOptions shmSize(long shmSize) {
        payload.put("shm_size", shmSize);
        return this;
    }

    public ImageCreateOptions shmSizeSystemd(long shmSizeSystemd) {
        payload.put("shm_size_systemd", shmSizeSystemd);
        return this;
    }

    public record StartupHealthCheck(
            long interval,
            long retries,
            long startPeriod,
            long successes,
            List<String> test,
            long timeout
    ) {
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

    public ImageCreateOptions startupHealthConfig(StartupHealthCheck startupHealthCheck) {
        payload.put("startupHealthConfig", startupHealthCheck.json());
        return this;
    }

    public ImageCreateOptions stdin(boolean stdin) {
        payload.put("stdin", stdin);
        return this;
    }

    public ImageCreateOptions stopSignal(long stopSignal) {
        payload.put("stop_signal", stopSignal);
        return this;
    }

    public ImageCreateOptions stopTimeout(long stopTimeout) {
        payload.put("stop_timeout", stopTimeout);
        return this;
    }

    public ImageCreateOptions storageOpts(Map<String, String> storageOpts) {
        JsonObject map = new JsonObject();
        storageOpts.forEach(map::put);
        payload.put("storage_opts", map);
        return this;
    }

    public ImageCreateOptions sysctl(Map<String, String> sysctl) {
        JsonObject map = new JsonObject();
        sysctl.forEach(map::put);
        payload.put("sysctl", map);
        return this;
    }

    public ImageCreateOptions systemd(String systemd) {
        payload.put("systemd", systemd);
        return this;
    }

    public ImageCreateOptions terminal(boolean terminal) {
        payload.put("terminal", terminal);
        return this;
    }

    public ImageCreateOptions throttleReadBpsDevice(Map<String, LinuxThrottleDevice> throttleReadBpsDevice) {
        JsonObject map = new JsonObject();
        throttleReadBpsDevice.forEach(map::put);
        payload.put("throttleReadBpsDevice", map);
        return this;
    }

    public ImageCreateOptions throttleReadIOPSDevice(Map<String, LinuxThrottleDevice> throttleReadIOPSDevice) {
        JsonObject map = new JsonObject();
        throttleReadIOPSDevice.forEach(map::put);
        payload.put("throttleReadIOPSDevice", map);
        return this;
    }

    public ImageCreateOptions throttleWriteBpsDevice(Map<String, LinuxThrottleDevice> throttleWriteBpsDevice) {
        JsonObject map = new JsonObject();
        throttleWriteBpsDevice.forEach(map::put);
        payload.put("throttleWriteBpsDevice", map);
        return this;
    }

    public ImageCreateOptions throttleWriteIOPSDevice(Map<String, LinuxThrottleDevice> throttleWriteIOPSDevice) {
        JsonObject map = new JsonObject();
        throttleWriteIOPSDevice.forEach(map::put);
        payload.put("throttleWriteIOPSDevice", map);
        return this;
    }

    public ImageCreateOptions timeout(long timeout) {
        payload.put("timeout", timeout);
        return this;
    }

    public ImageCreateOptions timezone(String timezone) {
        payload.put("timezone", timezone);
        return this;
    }

    public ImageCreateOptions umask(String umask) {
        payload.put("umask", umask);
        return this;
    }


}
