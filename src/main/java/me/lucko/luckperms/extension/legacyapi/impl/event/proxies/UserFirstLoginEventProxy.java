package me.lucko.luckperms.extension.legacyapi.impl.event.proxies;

import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.event.LuckPermsEvent;
import me.lucko.luckperms.api.event.user.UserFirstLoginEvent;
import me.lucko.luckperms.extension.legacyapi.impl.LegacyApiProvider;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public class UserFirstLoginEventProxy implements UserFirstLoginEvent {
    private final LegacyApiProvider api;
    private final net.luckperms.api.event.user.UserFirstLoginEvent event;

    public UserFirstLoginEventProxy(LegacyApiProvider api, net.luckperms.api.event.user.UserFirstLoginEvent event) {
        this.api = api;
        this.event = event;
    }

    @Override
    public @NonNull UUID getUuid() {
        return event.getUniqueId();
    }

    @Override
    public @NonNull String getUsername() {
        return event.getUsername();
    }

    @Override
    public @NonNull LuckPermsApi getApi() {
        return api;
    }

    @Override
    public @NonNull Class<? extends LuckPermsEvent> getEventType() {
        return UserFirstLoginEvent.class;
    }
}
