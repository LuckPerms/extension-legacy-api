package me.lucko.luckperms.extension.legacyapi.impl.permissionholders;

import me.lucko.luckperms.api.DataMutateResult;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.extension.legacyapi.impl.misc.DataMutateResultProxyUtil;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.UUID;

public class UserProxy extends PermissionHolderProxy implements User {
    private final net.luckperms.api.model.user.User user;

    public UserProxy(net.luckperms.api.model.user.User user) {
        super(user);
        this.user = user;
    }

    public net.luckperms.api.model.user.User getUnderlyingUser() {
        return this.user;
    }

    @Override
    public @NonNull UUID getUuid() {
        return this.user.getUniqueId();
    }

    @Override
    public @Nullable String getName() {
        return this.user.getUsername();
    }

    @Override
    public @NonNull String getPrimaryGroup() {
        return this.user.getPrimaryGroup();
    }

    @Override
    public @NonNull DataMutateResult setPrimaryGroup(@NonNull String group) {
        return DataMutateResultProxyUtil.legacy(this.user.setPrimaryGroup(group));
    }

    @Override
    public void refreshPermissions() {
        this.user.refreshCachedData();
    }

    @Override
    public void setupDataCache() {
        // do nothing!
    }
}
