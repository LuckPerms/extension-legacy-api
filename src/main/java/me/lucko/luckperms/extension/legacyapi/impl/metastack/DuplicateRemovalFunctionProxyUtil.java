package me.lucko.luckperms.extension.legacyapi.impl.metastack;

import net.luckperms.api.metastacking.DuplicateRemovalFunction;

public enum DuplicateRemovalFunctionProxyUtil {
    ;

    public static DuplicateRemovalFunction modern(me.lucko.luckperms.api.metastacking.DuplicateRemovalFunction duplicateRemovalFunction) {
        if (duplicateRemovalFunction == me.lucko.luckperms.api.metastacking.DuplicateRemovalFunction.RETAIN_ALL) {
            return DuplicateRemovalFunction.RETAIN_ALL;
        }
        if (duplicateRemovalFunction == me.lucko.luckperms.api.metastacking.DuplicateRemovalFunction.FIRST_ONLY) {
            return DuplicateRemovalFunction.FIRST_ONLY;
        }
        if (duplicateRemovalFunction == me.lucko.luckperms.api.metastacking.DuplicateRemovalFunction.LAST_ONLY) {
            return DuplicateRemovalFunction.LAST_ONLY;
        }
        throw new IllegalArgumentException("don't know how to turn " + duplicateRemovalFunction + " into modern type");
    }

    public static me.lucko.luckperms.api.metastacking.DuplicateRemovalFunction legacy(DuplicateRemovalFunction duplicateRemovalFunction) {
        if (duplicateRemovalFunction == net.luckperms.api.metastacking.DuplicateRemovalFunction.RETAIN_ALL) {
            return me.lucko.luckperms.api.metastacking.DuplicateRemovalFunction.RETAIN_ALL;
        }
        if (duplicateRemovalFunction == net.luckperms.api.metastacking.DuplicateRemovalFunction.FIRST_ONLY) {
            return me.lucko.luckperms.api.metastacking.DuplicateRemovalFunction.FIRST_ONLY;
        }
        if (duplicateRemovalFunction == net.luckperms.api.metastacking.DuplicateRemovalFunction.LAST_ONLY) {
            return me.lucko.luckperms.api.metastacking.DuplicateRemovalFunction.LAST_ONLY;
        }
        throw new IllegalArgumentException("don't know how to turn " + duplicateRemovalFunction + " into legacy type");
    }
}
