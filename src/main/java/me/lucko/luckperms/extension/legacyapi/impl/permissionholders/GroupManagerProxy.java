package me.lucko.luckperms.extension.legacyapi.impl.permissionholders;

import com.google.common.collect.Lists;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.HeldPermission;
import me.lucko.luckperms.api.manager.GroupManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class GroupManagerProxy implements GroupManager {
    private final net.luckperms.api.model.group.GroupManager groupManager;

    public GroupManagerProxy(net.luckperms.api.model.group.GroupManager groupManager) {
        this.groupManager = groupManager;
    }

    @Override
    public @NonNull CompletableFuture<Group> createAndLoadGroup(@NonNull String name) {
        return this.groupManager.createAndLoadGroup(name).thenApply(GroupProxy::new);
    }

    @Override
    public @NonNull CompletableFuture<Optional<Group>> loadGroup(@NonNull String name) {
        return this.groupManager.loadGroup(name).thenApply(opt -> opt.map(GroupProxy::new));
    }

    @Override
    public @NonNull CompletableFuture<Void> saveGroup(@NonNull Group group) {
        return this.groupManager.saveGroup(((GroupProxy) group).getUnderlyingGroup());
    }

    @Override
    public @NonNull CompletableFuture<Void> deleteGroup(@NonNull Group group) {
        return this.groupManager.deleteGroup(((GroupProxy) group).getUnderlyingGroup());
    }

    @Override
    public @NonNull CompletableFuture<Void> loadAllGroups() {
        return this.groupManager.loadAllGroups();
    }

    @Override
    public @NonNull CompletableFuture<List<HeldPermission<String>>> getWithPermission(@NonNull String permission) {
        return this.groupManager.getWithPermission(permission).thenApply(list -> Lists.transform(list, HeldPermissionProxy::new));
    }

    @Override
    public @Nullable Group getGroup(@NonNull String name) {
        net.luckperms.api.model.group.Group group = this.groupManager.getGroup(name);
        return group == null ? null : new GroupProxy(group);
    }

    @Override
    public @NonNull Set<Group> getLoadedGroups() {
        return this.groupManager.getLoadedGroups().stream().map(GroupProxy::new).collect(Collectors.toSet());
    }

    @Override
    public boolean isLoaded(@NonNull String name) {
        return this.groupManager.isLoaded(name);
    }
}
