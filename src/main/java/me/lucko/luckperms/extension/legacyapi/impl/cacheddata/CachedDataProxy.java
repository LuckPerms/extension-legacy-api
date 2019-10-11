package me.lucko.luckperms.extension.legacyapi.impl.cacheddata;

import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.caching.CachedData;
import me.lucko.luckperms.api.caching.GroupData;
import me.lucko.luckperms.api.caching.MetaContexts;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.caching.PermissionData;
import me.lucko.luckperms.api.caching.UserData;
import me.lucko.luckperms.extension.legacyapi.impl.contextset.ContextsProxyUtil;
import net.luckperms.api.cacheddata.CachedDataManager;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.CompletableFuture;

public class CachedDataProxy implements CachedData, UserData, GroupData {
    private final CachedDataManager cachedDataManager;

    public CachedDataProxy(CachedDataManager cachedDataManager) {
        this.cachedDataManager = cachedDataManager;
    }

    @Override
    public @NonNull PermissionData getPermissionData(@NonNull Contexts contexts) {
        return new PermissionDataProxy(this.cachedDataManager.getPermissionData(ContextsProxyUtil.modernContexts(contexts)));
    }

    @Override
    public @NonNull MetaData getMetaData(@NonNull MetaContexts contexts) {
        return new MetaDataProxy(this.cachedDataManager.getMetaData(ContextsProxyUtil.modernContexts(contexts)));
    }

    @Override
    public @NonNull MetaData getMetaData(@NonNull Contexts contexts) {
        return new MetaDataProxy(this.cachedDataManager.getMetaData(ContextsProxyUtil.modernContexts(contexts)));
    }

    @Override
    public @NonNull PermissionData calculatePermissions(@NonNull Contexts contexts) {
        return new PermissionDataProxy(this.cachedDataManager.permissionData().calculate(ContextsProxyUtil.modernContexts(contexts)));
    }

    @Override
    public @NonNull MetaData calculateMeta(@NonNull MetaContexts contexts) {
        return new MetaDataProxy(this.cachedDataManager.metaData().calculate(ContextsProxyUtil.modernContexts(contexts)));
    }

    @Override
    public @NonNull MetaData calculateMeta(@NonNull Contexts contexts) {
        return new MetaDataProxy(this.cachedDataManager.metaData().calculate(ContextsProxyUtil.modernContexts(contexts)));
    }

    @Override
    public void recalculatePermissions(@NonNull Contexts contexts) {
        this.cachedDataManager.permissionData().recalculate(ContextsProxyUtil.modernContexts(contexts));
    }

    @Override
    public void recalculateMeta(@NonNull MetaContexts contexts) {
        this.cachedDataManager.metaData().recalculate(ContextsProxyUtil.modernContexts(contexts));
    }

    @Override
    public void recalculateMeta(@NonNull Contexts contexts) {
        this.cachedDataManager.metaData().recalculate(ContextsProxyUtil.modernContexts(contexts));
    }

    @Override
    public @NonNull CompletableFuture<? extends PermissionData> reloadPermissions(@NonNull Contexts contexts) {
        return this.cachedDataManager.permissionData().reload(ContextsProxyUtil.modernContexts(contexts)).thenApply(PermissionDataProxy::new);
    }

    @Override
    public @NonNull CompletableFuture<? extends MetaData> reloadMeta(@NonNull MetaContexts contexts) {
        return this.cachedDataManager.metaData().reload(ContextsProxyUtil.modernContexts(contexts)).thenApply(MetaDataProxy::new);
    }

    @Override
    public @NonNull CompletableFuture<? extends MetaData> reloadMeta(@NonNull Contexts contexts) {
        return this.cachedDataManager.metaData().reload(ContextsProxyUtil.modernContexts(contexts)).thenApply(MetaDataProxy::new);
    }

    @Override
    public void recalculatePermissions() {
        this.cachedDataManager.permissionData().recalculate();
    }

    @Override
    public void recalculateMeta() {
        this.cachedDataManager.metaData().recalculate();
    }

    @Override
    public @NonNull CompletableFuture<Void> reloadPermissions() {
        return this.cachedDataManager.permissionData().reload();
    }

    @Override
    public @NonNull CompletableFuture<Void> reloadMeta() {
        return this.cachedDataManager.metaData().reload();
    }

    @Override
    public void invalidatePermissions(@NonNull Contexts contexts) {
        this.cachedDataManager.permissionData().invalidate(ContextsProxyUtil.modernContexts(contexts));
    }

    @Override
    public void invalidateMeta(@NonNull MetaContexts contexts) {
        this.cachedDataManager.metaData().invalidate(ContextsProxyUtil.modernContexts(contexts));
    }

    @Override
    public void invalidateMeta(@NonNull Contexts contexts) {
        this.cachedDataManager.metaData().invalidate(ContextsProxyUtil.modernContexts(contexts));
    }

    @Override
    public void invalidatePermissions() {
        this.cachedDataManager.permissionData().invalidate();
    }

    @Override
    public void invalidateMeta() {
        this.cachedDataManager.metaData().invalidate();
    }

    @Override
    public void invalidatePermissionCalculators() {
        this.cachedDataManager.invalidatePermissionCalculators();
    }
}
