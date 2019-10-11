package me.lucko.luckperms.extension.legacyapi.impl.track;

import me.lucko.luckperms.api.DemotionResult;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class DemotionResultProxy implements DemotionResult {
    private final net.luckperms.api.track.DemotionResult demotionResult;

    public DemotionResultProxy(net.luckperms.api.track.DemotionResult demotionResult) {
        this.demotionResult = demotionResult;
    }

    @Override
    public @NonNull Status getStatus() {
        return legacyStatus(this.demotionResult.getStatus());
    }

    @Override
    public @NonNull Optional<String> getGroupFrom() {
        return this.demotionResult.getGroupFrom();
    }

    @Override
    public @NonNull Optional<String> getGroupTo() {
        return this.demotionResult.getGroupTo();
    }
    
    private static Status legacyStatus(net.luckperms.api.track.DemotionResult.Status status) {
        switch (status) {
            case SUCCESS:
                return Status.SUCCESS;
            case UNDEFINED_FAILURE:
                return Status.UNDEFINED_FAILURE;
            case MALFORMED_TRACK:
                return Status.MALFORMED_TRACK;
            case AMBIGUOUS_CALL:
                return Status.AMBIGUOUS_CALL;
            case NOT_ON_TRACK:
                return Status.NOT_ON_TRACK;
            case REMOVED_FROM_FIRST_GROUP:
                return Status.REMOVED_FROM_FIRST_GROUP;
            default:
                throw new AssertionError();
        }
    }
}
