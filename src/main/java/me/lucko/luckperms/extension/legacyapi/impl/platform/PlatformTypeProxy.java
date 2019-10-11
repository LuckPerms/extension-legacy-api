package me.lucko.luckperms.extension.legacyapi.impl.platform;

import me.lucko.luckperms.api.platform.PlatformType;
import net.luckperms.api.platform.Platform;

public class PlatformTypeProxy {

    public static PlatformType proxy(Platform.Type platformType) {
        switch (platformType) {
            case BUKKIT:
                return PlatformType.BUKKIT;
            case BUNGEECORD:
                return PlatformType.BUNGEE;
            case SPONGE:
                return PlatformType.SPONGE;
            case NUKKIT:
                return PlatformType.NUKKIT;
            case VELOCITY:
                return PlatformType.VELOCITY;
            default:
                throw new AssertionError();
        }
    }

}
