package me.lucko.luckperms.extension.legacyapi.impl.permissionholders;

import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.context.ContextSet;
import me.lucko.luckperms.extension.legacyapi.impl.contextset.ContextSetProxyUtil;
import net.luckperms.api.query.QueryOptions;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.OptionalInt;

public class GroupProxy extends PermissionHolderProxy implements Group {
    private final net.luckperms.api.model.group.Group group;

    public GroupProxy(net.luckperms.api.model.group.Group group) {
        super(group);
        this.group = group;
    }

    public net.luckperms.api.model.group.Group getUnderlyingGroup() {
        return this.group;
    }

    @Override
    public @NonNull String getName() {
        return this.group.getName();
    }

    @Override
    public @Nullable String getDisplayName() {
        return this.group.getDisplayName();
    }

    @Override
    public @Nullable String getDisplayName(@NonNull ContextSet contextSet) {
        return this.group.getDisplayName(QueryOptions.contextual(ContextSetProxyUtil.modernImmutable(contextSet)));
    }

    @Override
    public @NonNull OptionalInt getWeight() {
        return this.group.getWeight();
    }
}
