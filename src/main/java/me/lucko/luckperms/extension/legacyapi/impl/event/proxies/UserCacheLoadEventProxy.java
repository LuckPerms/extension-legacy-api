package me.lucko.luckperms.extension.legacyapi.impl.event.proxies;

import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.UserData;
import me.lucko.luckperms.api.event.LuckPermsEvent;
import me.lucko.luckperms.api.event.user.UserCacheLoadEvent;
import me.lucko.luckperms.extension.legacyapi.impl.LegacyApiProvider;
import me.lucko.luckperms.extension.legacyapi.impl.cacheddata.CachedDataProxy;
import me.lucko.luckperms.extension.legacyapi.impl.permissionholders.UserProxy;
import org.checkerframework.checker.nullness.qual.NonNull;

public class UserCacheLoadEventProxy implements UserCacheLoadEvent {
    private final LegacyApiProvider api;
    private final net.luckperms.api.event.user.UserCacheLoadEvent event;

    public UserCacheLoadEventProxy(LegacyApiProvider api, net.luckperms.api.event.user.UserCacheLoadEvent event) {
        this.api = api;
        this.event = event;
    }

    @Override
    public @NonNull User getUser() {
        return new UserProxy(this.event.getUser());
    }

    @Override
    public @NonNull UserData getLoadedData() {
        return new CachedDataProxy(this.event.getLoadedData());
    }

    @Override
    public @NonNull LuckPermsApi getApi() {
        return this.api;
    }

    @Override
    public @NonNull Class<? extends LuckPermsEvent> getEventType() {
        return UserCacheLoadEvent.class;
    }
}
