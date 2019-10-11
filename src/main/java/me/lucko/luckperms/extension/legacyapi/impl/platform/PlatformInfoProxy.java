package me.lucko.luckperms.extension.legacyapi.impl.platform;

import me.lucko.luckperms.api.platform.PlatformInfo;
import me.lucko.luckperms.api.platform.PlatformType;
import net.luckperms.api.platform.Platform;
import net.luckperms.api.platform.PluginMetadata;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Set;
import java.util.UUID;

public class PlatformInfoProxy implements PlatformInfo {
    private final Platform platform;
    private final PluginMetadata pluginMetadata;

    public PlatformInfoProxy(Platform platform, PluginMetadata pluginMetadata) {
        this.platform = platform;
        this.pluginMetadata = pluginMetadata;
    }

    @Override
    public @NonNull String getVersion() {
        return this.pluginMetadata.getVersion();
    }

    @Override
    public double getApiVersion() {
        return Double.parseDouble(this.pluginMetadata.getApiVersion());
    }

    @Override
    public @NonNull PlatformType getType() {
        return PlatformTypeProxy.proxy(this.platform.getType());
    }

    @Override
    public @NonNull Set<UUID> getUniqueConnections() {
        return this.platform.getUniqueConnections();
    }

    @Override
    public long getStartTime() {
        return this.platform.getStartTime().toEpochMilli();
    }
}
