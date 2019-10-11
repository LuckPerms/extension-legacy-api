package me.lucko.luckperms.extension.legacyapi.impl;

import me.lucko.luckperms.api.LuckPermsApi;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.plugin.PluginContainer;

public enum SpongeApiRegistrationUtil {
    ;

    public static void register(LuckPermsApi api) {
        PluginContainer container = Sponge.getPluginManager().getPlugin("luckperms").orElseThrow(() -> new RuntimeException("Can't get LuckPerms plugin container"));
        Object plugin = container.getInstance().orElseThrow(() -> new RuntimeException("Can't get LuckPerms plugin instance from container"));
        Sponge.getServiceManager().setProvider(plugin, LuckPermsApi.class, api);
    }
}
