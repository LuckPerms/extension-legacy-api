package me.lucko.luckperms.extension.legacyapi.impl.misc;

import me.lucko.luckperms.api.ActionLogger;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.HeldPermission;
import me.lucko.luckperms.api.Log;
import me.lucko.luckperms.api.LogEntry;
import me.lucko.luckperms.api.Storage;
import me.lucko.luckperms.api.Track;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.manager.GroupManager;
import me.lucko.luckperms.api.manager.TrackManager;
import me.lucko.luckperms.api.manager.UserManager;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class ForwardingStorageProxy implements Storage {
    private static final Function<Throwable, Boolean> CONSUME_EXCEPTION = throwable -> {
        throwable.printStackTrace();
        return false;
    };

    private static Function<Throwable, Boolean> consumeExceptionToFalse() {
        return CONSUME_EXCEPTION;
    }

    private static <T> Function<Throwable, T> consumeExceptionToNull() {
        return throwable -> {
            throwable.printStackTrace();
            return null;
        };
    }
    
    private final ActionLogger actionLogger;
    private final UserManager userManager;
    private final GroupManager groupManager;
    private final TrackManager trackManager;

    public ForwardingStorageProxy(ActionLogger actionLogger, UserManager userManager, GroupManager groupManager, TrackManager trackManager) {
        this.actionLogger = actionLogger;
        this.userManager = userManager;
        this.groupManager = groupManager;
        this.trackManager = trackManager;
    }

    @Override
    public @NonNull String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isAcceptingLogins() {
        return true;
    }

    @Override
    public @NonNull Executor getSyncExecutor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull Executor getAsyncExecutor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NonNull CompletableFuture<Boolean> logAction(@NonNull LogEntry entry) {
        return this.actionLogger.submitToStorage(entry)
                .thenApply(r -> true)
                .exceptionally(consumeExceptionToFalse());
    }

    @Override
    public @NonNull CompletableFuture<Log> getLog() {
        return this.actionLogger.getLog().exceptionally(consumeExceptionToNull());
    }

    @Override
    public @NonNull CompletableFuture<Boolean> loadUser(@NonNull UUID uuid, String username) {
        return this.userManager.loadUser(uuid, username)
                .thenApply(r -> true)
                .exceptionally(consumeExceptionToFalse());
    }

    @Override
    public @NonNull CompletableFuture<Boolean> saveUser(@NonNull User user) {
        return this.userManager.saveUser(user)
                .thenApply(r -> true)
                .exceptionally(consumeExceptionToFalse());
    }

    @Override
    public @NonNull CompletableFuture<Set<UUID>> getUniqueUsers() {
        return this.userManager.getUniqueUsers().exceptionally(consumeExceptionToNull());
    }

    @Override
    public @NonNull CompletableFuture<List<HeldPermission<UUID>>> getUsersWithPermission(@NonNull String permission) {
        return this.userManager.getWithPermission(permission).exceptionally(consumeExceptionToNull());
    }

    @Override
    public @NonNull CompletableFuture<Boolean> createAndLoadGroup(@NonNull String name) {
        return this.groupManager.createAndLoadGroup(name)
                .thenApply(r -> true)
                .exceptionally(consumeExceptionToFalse());
    }

    @Override
    public @NonNull CompletableFuture<Boolean> loadGroup(@NonNull String name) {
        return this.groupManager.loadGroup(name)
                .thenApply(Optional::isPresent)
                .exceptionally(consumeExceptionToFalse());
    }

    @Override
    public @NonNull CompletableFuture<Boolean> loadAllGroups() {
        return this.groupManager.loadAllGroups()
                .thenApply(r -> true)
                .exceptionally(consumeExceptionToFalse());
    }

    @Override
    public @NonNull CompletableFuture<Boolean> saveGroup(@NonNull Group group) {
        return this.groupManager.saveGroup(group)
                .thenApply(r -> true)
                .exceptionally(consumeExceptionToFalse());
    }

    @Override
    public @NonNull CompletableFuture<Boolean> deleteGroup(@NonNull Group group) {
        return this.groupManager.deleteGroup(group)
                .thenApply(r -> true)
                .exceptionally(consumeExceptionToFalse());
    }

    @Override
    public @NonNull CompletableFuture<List<HeldPermission<String>>> getGroupsWithPermission(@NonNull String permission) {
        return this.groupManager.getWithPermission(permission)
                .exceptionally(consumeExceptionToNull());
    }

    @Override
    public @NonNull CompletableFuture<Boolean> createAndLoadTrack(@NonNull String name) {
        return this.trackManager.createAndLoadTrack(name)
                .thenApply(r -> true)
                .exceptionally(consumeExceptionToFalse());
    }

    @Override
    public @NonNull CompletableFuture<Boolean> loadTrack(@NonNull String name) {
        return this.trackManager.loadTrack(name)
                .thenApply(Optional::isPresent)
                .exceptionally(consumeExceptionToFalse());
    }

    @Override
    public @NonNull CompletableFuture<Boolean> loadAllTracks() {
        return this.trackManager.loadAllTracks()
                .thenApply(r -> true)
                .exceptionally(consumeExceptionToFalse());
    }

    @Override
    public @NonNull CompletableFuture<Boolean> saveTrack(@NonNull Track track) {
        return this.trackManager.saveTrack(track)
                .thenApply(r -> true)
                .exceptionally(consumeExceptionToFalse());
    }

    @Override
    public @NonNull CompletableFuture<Boolean> deleteTrack(@NonNull Track track) {
        return this.trackManager.deleteTrack(track)
                .thenApply(r -> true)
                .exceptionally(consumeExceptionToFalse());
    }

    @Override
    public @NonNull CompletableFuture<Boolean> saveUUIDData(@NonNull String username, @NonNull UUID uuid) {
        return this.userManager.savePlayerData(uuid, username)
                .thenApply(r -> true)
                .exceptionally(consumeExceptionToFalse());
    }

    @Override
    public @NonNull CompletableFuture<UUID> getUUID(@NonNull String username) {
        return this.userManager.lookupUuid(username).exceptionally(consumeExceptionToNull());
    }

    @Override
    public @NonNull CompletableFuture<String> getName(@NonNull UUID uuid) {
        return this.userManager.lookupUsername(uuid).exceptionally(consumeExceptionToNull());
    }
}
