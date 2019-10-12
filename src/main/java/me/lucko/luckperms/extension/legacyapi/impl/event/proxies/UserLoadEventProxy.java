package me.lucko.luckperms.extension.legacyapi.impl.event.proxies;

import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.event.LuckPermsEvent;
import me.lucko.luckperms.api.event.user.UserLoadEvent;
import me.lucko.luckperms.extension.legacyapi.impl.LegacyApiProvider;
import me.lucko.luckperms.extension.legacyapi.impl.permissionholders.UserProxy;
import org.checkerframework.checker.nullness.qual.NonNull;

public class UserLoadEventProxy implements UserLoadEvent {
    private final LegacyApiProvider api;
    private final net.luckperms.api.event.user.UserLoadEvent event;

    public UserLoadEventProxy(LegacyApiProvider api, net.luckperms.api.event.user.UserLoadEvent event) {
        this.api = api;
        this.event = event;
    }

    @Override
    public @NonNull User getUser() {
        return new UserProxy(event.getUser());
    }

    @Override
    public @NonNull LuckPermsApi getApi() {
        return api;
    }

    @Override
    public @NonNull Class<? extends LuckPermsEvent> getEventType() {
        return UserLoadEvent.class;
    }
}
