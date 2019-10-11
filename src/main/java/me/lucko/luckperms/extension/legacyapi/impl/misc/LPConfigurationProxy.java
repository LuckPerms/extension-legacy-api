package me.lucko.luckperms.extension.legacyapi.impl.misc;

import me.lucko.luckperms.api.LPConfiguration;
import net.luckperms.api.LuckPerms;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

public class LPConfigurationProxy implements LPConfiguration {
    private final LuckPerms luckPerms;

    public LPConfigurationProxy(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    @Override
    public @NonNull String getServer() {
        return this.luckPerms.getServerName();
    }

    @Override
    public boolean getIncludeGlobalPerms() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getIncludeGlobalWorldPerms() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getApplyGlobalGroups() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getApplyGlobalWorldGroups() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull String getStorageMethod() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getSplitStorage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Map<String, String> getSplitStorageOptions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Unsafe unsafe() {
        throw new UnsupportedOperationException();
    }
}
