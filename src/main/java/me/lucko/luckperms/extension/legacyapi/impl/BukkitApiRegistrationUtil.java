package me.lucko.luckperms.extension.legacyapi.impl;

import me.lucko.luckperms.api.LuckPermsApi;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

public enum BukkitApiRegistrationUtil {
    ;

    public static void register(LuckPermsApi api) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("LuckPerms");
        Bukkit.getServicesManager().register(LuckPermsApi.class, api, plugin, ServicePriority.Normal);
    }
}
