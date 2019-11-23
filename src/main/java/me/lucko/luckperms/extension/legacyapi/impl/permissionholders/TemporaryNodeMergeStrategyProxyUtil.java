package me.lucko.luckperms.extension.legacyapi.impl.permissionholders;


import net.luckperms.api.model.data.TemporaryNodeMergeStrategy;

public enum TemporaryNodeMergeStrategyProxyUtil {
    ;

    public static TemporaryNodeMergeStrategy modern(me.lucko.luckperms.api.TemporaryMergeBehaviour mergeBehaviour) {
        switch (mergeBehaviour) {
            case FAIL_WITH_ALREADY_HAS:
                return TemporaryNodeMergeStrategy.NONE;
            case ADD_NEW_DURATION_TO_EXISTING:
                return TemporaryNodeMergeStrategy.ADD_NEW_DURATION_TO_EXISTING;
            case REPLACE_EXISTING_IF_DURATION_LONGER:
                return TemporaryNodeMergeStrategy.REPLACE_EXISTING_IF_DURATION_LONGER;
            default:
                throw new AssertionError();
        }
    }
}
