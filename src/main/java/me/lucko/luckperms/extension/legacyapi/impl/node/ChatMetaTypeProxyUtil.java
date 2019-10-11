package me.lucko.luckperms.extension.legacyapi.impl.node;

import net.luckperms.api.node.ChatMetaType;

public enum ChatMetaTypeProxyUtil {
    ;

    public static ChatMetaType modernType(me.lucko.luckperms.api.ChatMetaType type) {
        switch (type) {
            case PREFIX:
                return ChatMetaType.PREFIX;
            case SUFFIX:
                return ChatMetaType.SUFFIX;
            default:
                throw new AssertionError();
        }
    }

    public static me.lucko.luckperms.api.ChatMetaType legacyType(ChatMetaType type) {
        switch (type) {
            case PREFIX:
                return me.lucko.luckperms.api.ChatMetaType.PREFIX;
            case SUFFIX:
                return me.lucko.luckperms.api.ChatMetaType.SUFFIX;
            default:
                throw new AssertionError();
        }
    }

}
