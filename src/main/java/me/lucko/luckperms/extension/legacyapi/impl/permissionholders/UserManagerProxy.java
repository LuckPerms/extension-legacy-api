package me.lucko.luckperms.extension.legacyapi.impl.permissionholders;

import com.google.common.collect.Lists;
import me.lucko.luckperms.api.HeldPermission;
import me.lucko.luckperms.api.PlayerSaveResult;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.manager.UserManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class UserManagerProxy implements UserManager {
    private final net.luckperms.api.model.user.UserManager userManager;

    public UserManagerProxy(net.luckperms.api.model.user.UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public @NonNull CompletableFuture<User> loadUser(@NonNull UUID uuid, @Nullable String username) {
        return this.userManager.loadUser(uuid, username).thenApply(UserProxy::new);
    }

    @Override
    public @NonNull CompletableFuture<UUID> lookupUuid(@NonNull String username) {
        return this.userManager.lookupUniqueId(username);
    }

    @Override
    public @NonNull CompletableFuture<String> lookupUsername(@NonNull UUID uuid) {
        return this.userManager.lookupUsername(uuid);
    }

    @Override
    public @NonNull CompletableFuture<Void> saveUser(@NonNull User user) {
        return this.userManager.saveUser(((UserProxy) user).getUnderlyingUser());
    }

    @Override
    public @NonNull CompletableFuture<PlayerSaveResult> savePlayerData(@NonNull UUID uuid, @NonNull String username) {
        return this.userManager.savePlayerData(uuid, username).thenApply(PlayerSaveResultProxy::new);
    }

    @Override
    public @NonNull CompletableFuture<Set<UUID>> getUniqueUsers() {
        return this.userManager.getUniqueUsers();
    }

    @Override
    public @NonNull CompletableFuture<List<HeldPermission<UUID>>> getWithPermission(@NonNull String permission) {
        return this.userManager.getWithPermission(permission).thenApply(list -> Lists.transform(list, HeldPermissionProxy::new));
    }

    @Override
    public @Nullable User getUser(@NonNull UUID uuid) {
        net.luckperms.api.model.user.User user = this.userManager.getUser(uuid);
        return user == null ? null : new UserProxy(user);
    }

    @Override
    public @Nullable User getUser(@NonNull String name) {
        net.luckperms.api.model.user.User user = this.userManager.getUser(name);
        return user == null ? null : new UserProxy(user);
    }

    @Override
    public @NonNull Set<User> getLoadedUsers() {
        return this.userManager.getLoadedUsers().stream().map(UserProxy::new).collect(Collectors.toSet());
    }

    @Override
    public boolean isLoaded(@NonNull UUID uuid) {
        return this.userManager.isLoaded(uuid);
    }

    @Override
    public void cleanupUser(@NonNull User user) {
        this.userManager.cleanupUser(((UserProxy) user).getUnderlyingUser());
    }
}
