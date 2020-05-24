package me.lucko.luckperms.extension.legacyapi.impl.metastack;

import me.lucko.luckperms.api.metastacking.MetaStackElement;

public enum MetaStackElementProxyUtil {
    ;

    public static MetaStackElement legacy(net.luckperms.api.metastacking.MetaStackElement modern) {
        if (modern instanceof ModernMetaStackElementProxy) {
            return ((ModernMetaStackElementProxy) modern).getUnderlyingElement();
        }
        return new LegacyMetaStackElementProxy(modern);
    }

    public static net.luckperms.api.metastacking.MetaStackElement modern(MetaStackElement legacy) {
        if (legacy instanceof LegacyMetaStackElementProxy) {
            return ((LegacyMetaStackElementProxy) legacy).getUnderlyingElement();
        }
        return new ModernMetaStackElementProxy(legacy);
    }

}
