package me.lucko.luckperms.extension.legacyapi.impl.misc;

import net.luckperms.api.util.Tristate;

public enum TristateProxyUtil {
    ;

    public static Tristate modern(me.lucko.luckperms.api.Tristate tristate) {
        switch (tristate) {
            case TRUE:
                return Tristate.TRUE;
            case FALSE:
                return Tristate.FALSE;
            case UNDEFINED:
                return Tristate.UNDEFINED;
            default:
                throw new AssertionError();
        }
    }

    public static me.lucko.luckperms.api.Tristate legacy(Tristate tristate) {
        switch (tristate) {
            case TRUE:
                return me.lucko.luckperms.api.Tristate.TRUE;
            case FALSE:
                return me.lucko.luckperms.api.Tristate.FALSE;
            case UNDEFINED:
                return me.lucko.luckperms.api.Tristate.UNDEFINED;
            default:
                throw new AssertionError();
        }
    }
}
