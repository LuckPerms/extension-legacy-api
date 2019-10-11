package me.lucko.luckperms.extension.legacyapi;

import me.lucko.luckperms.extension.legacyapi.impl.ApiRegistrationUtil;
import me.lucko.luckperms.extension.legacyapi.impl.BukkitApiRegistrationUtil;
import me.lucko.luckperms.extension.legacyapi.impl.LegacyApiProvider;
import me.lucko.luckperms.extension.legacyapi.impl.SpongeApiRegistrationUtil;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.extension.Extension;

public class LegacyApiExtension implements Extension {

    private final LuckPerms luckPerms;

    public LegacyApiExtension(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
    }

    @Override
    public void load() {
        LegacyApiProvider legacyProvider = new LegacyApiProvider(this.luckPerms);

        ApiRegistrationUtil.registerProvider(legacyProvider);

        if (classExists("org.bukkit.plugin.ServicesManager")) {
            BukkitApiRegistrationUtil.register(legacyProvider);
        }
        if (classExists("org.spongepowered.api.service.ServiceManager")) {
            SpongeApiRegistrationUtil.register(legacyProvider);
        }
    }

    @Override
    public void unload() {
        ApiRegistrationUtil.unregisterProvider();
    }

    private static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
