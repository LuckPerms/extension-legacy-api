package me.lucko.luckperms.extension.legacyapi.impl.permissionholders;

import me.lucko.luckperms.api.PlayerSaveResult;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerSaveResultProxy implements PlayerSaveResult {
    private final net.luckperms.api.model.PlayerSaveResult playerSaveResult;

    public PlayerSaveResultProxy(net.luckperms.api.model.PlayerSaveResult playerSaveResult) {
        this.playerSaveResult = playerSaveResult;
    }

    @Override
    public @NonNull Set<Status> getStatus() {
        return this.playerSaveResult.getOutcomes().stream().map(PlayerSaveResultProxy::legacyStatus).collect(Collectors.toSet());
    }

    @Override
    public @Nullable String getOldUsername() {
        return this.playerSaveResult.getPreviousUsername();
    }

    @Override
    public @Nullable Set<UUID> getOtherUuids() {
        return this.playerSaveResult.getOtherUniqueIds();
    }

    private static Status legacyStatus(net.luckperms.api.model.PlayerSaveResult.Outcome outcome) {
        switch (outcome) {
            case CLEAN_INSERT:
                return Status.CLEAN_INSERT;
            case NO_CHANGE:
                return Status.NO_CHANGE;
            case USERNAME_UPDATED:
                return Status.USERNAME_UPDATED;
            case OTHER_UNIQUE_IDS_PRESENT_FOR_USERNAME:
                return Status.OTHER_UUIDS_PRESENT_FOR_USERNAME;
            default:
                throw new AssertionError();
        }
    }
}
