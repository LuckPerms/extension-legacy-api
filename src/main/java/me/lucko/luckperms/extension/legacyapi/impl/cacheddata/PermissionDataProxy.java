package me.lucko.luckperms.extension.legacyapi.impl.cacheddata;

import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.Tristate;
import me.lucko.luckperms.api.caching.PermissionData;
import me.lucko.luckperms.extension.legacyapi.impl.contextset.ContextsProxyUtil;
import me.lucko.luckperms.extension.legacyapi.impl.misc.TristateProxyUtil;
import net.luckperms.api.cacheddata.CachedPermissionData;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

public class PermissionDataProxy implements PermissionData {
    private final CachedPermissionData data;

    public PermissionDataProxy(CachedPermissionData data) {
        this.data = data;
    }

    @Override
    public @NonNull Tristate getPermissionValue(@NonNull String permission) {
        return TristateProxyUtil.legacy(this.data.checkPermission(permission));
    }

    @Override
    public void invalidateCache() {
        this.data.invalidateCache();
    }

    @Override
    public @NonNull Map<String, Boolean> getImmutableBacking() {
        return this.data.getPermissionMap();
    }

    @Override
    public @NonNull Contexts getContexts() {
        return ContextsProxyUtil.legacyContexts(this.data.getQueryOptions());
    }
}
