package me.lucko.luckperms.extension.legacyapi.impl.misc;

import net.luckperms.api.model.data.DataMutateResult;

public enum DataMutateResultProxyUtil {
    ;

    public static DataMutateResult modern(me.lucko.luckperms.api.DataMutateResult result) {
        switch (result) {
            case SUCCESS:
                return DataMutateResult.SUCCESS;
            case FAIL:
                return DataMutateResult.FAIL;
            case LACKS:
                return DataMutateResult.FAIL_LACKS;
            case ALREADY_HAS:
                return DataMutateResult.FAIL_ALREADY_HAS;
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
            case FAIL_LACKS:
                return me.lucko.luckperms.api.DataMutateResult.LACKS;
            case FAIL_ALREADY_HAS:
                return me.lucko.luckperms.api.DataMutateResult.ALREADY_HAS;
            default:
                throw new AssertionError();
        }
    }
}
