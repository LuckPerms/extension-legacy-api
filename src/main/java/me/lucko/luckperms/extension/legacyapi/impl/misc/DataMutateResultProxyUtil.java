package me.lucko.luckperms.extension.legacyapi.impl.misc;

import net.luckperms.api.model.DataMutateResult;

public enum DataMutateResultProxyUtil {
    ;

    public static DataMutateResult modern(me.lucko.luckperms.api.DataMutateResult result) {
        switch (result) {
            case SUCCESS:
                return DataMutateResult.SUCCESS;
            case FAIL:
                return DataMutateResult.FAIL;
            case LACKS:
                return DataMutateResult.LACKS;
            case ALREADY_HAS:
                return DataMutateResult.ALREADY_HAS;
            default:
                throw new AssertionError();
        }
    }

    public static me.lucko.luckperms.api.DataMutateResult legacy(DataMutateResult result) {
        switch (result) {
            case SUCCESS:
                return me.lucko.luckperms.api.DataMutateResult.SUCCESS;
            case FAIL:
                return me.lucko.luckperms.api.DataMutateResult.FAIL;
            case LACKS:
                return me.lucko.luckperms.api.DataMutateResult.LACKS;
            case ALREADY_HAS:
                return me.lucko.luckperms.api.DataMutateResult.ALREADY_HAS;
            default:
                throw new AssertionError();
        }
    }
}
