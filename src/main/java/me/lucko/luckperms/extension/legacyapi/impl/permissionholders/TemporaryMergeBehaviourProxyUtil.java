package me.lucko.luckperms.extension.legacyapi.impl.permissionholders;

import net.luckperms.api.model.TemporaryMergeBehaviour;

public enum TemporaryMergeBehaviourProxyUtil {
    ;

    public static TemporaryMergeBehaviour modern(me.lucko.luckperms.api.TemporaryMergeBehaviour mergeBehaviour) {
        switch (mergeBehaviour) {
            case FAIL_WITH_ALREADY_HAS:
                return TemporaryMergeBehaviour.FAIL_WITH_ALREADY_HAS;
            case ADD_NEW_DURATION_TO_EXISTING:
                return TemporaryMergeBehaviour.ADD_NEW_DURATION_TO_EXISTING;
            case REPLACE_EXISTING_IF_DURATION_LONGER:
                return TemporaryMergeBehaviour.REPLACE_EXISTING_IF_DURATION_LONGER;
            default:
                throw new AssertionError();
        }
    }
}
