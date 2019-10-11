package me.lucko.luckperms.extension.legacyapi.impl.track;

import me.lucko.luckperms.api.PromotionResult;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class PromotionResultProxy implements PromotionResult {
    private final net.luckperms.api.track.PromotionResult promotionResult;

    public PromotionResultProxy(net.luckperms.api.track.PromotionResult promotionResult) {
        this.promotionResult = promotionResult;
    }

    @Override
    public @NonNull Status getStatus() {
        return legacyStatus(this.promotionResult.getStatus());
    }

    @Override
    public @NonNull Optional<String> getGroupFrom() {
        return this.promotionResult.getGroupFrom();
    }

    @Override
    public @NonNull Optional<String> getGroupTo() {
        return this.promotionResult.getGroupTo();
    }

    private static Status legacyStatus(net.luckperms.api.track.PromotionResult.Status status) {
        switch (status) {
            case SUCCESS:
                return Status.SUCCESS;
            case END_OF_TRACK:
                return Status.END_OF_TRACK;
            case AMBIGUOUS_CALL:
                return Status.AMBIGUOUS_CALL;
            case MALFORMED_TRACK:
                return Status.MALFORMED_TRACK;
            case UNDEFINED_FAILURE:
                return Status.UNDEFINED_FAILURE;
            case ADDED_TO_FIRST_GROUP:
                return Status.ADDED_TO_FIRST_GROUP;
            default:
                throw new AssertionError();
        }
    }
}
