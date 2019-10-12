package me.lucko.luckperms.extension.legacyapi.impl.event.proxies;

import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Track;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.event.LuckPermsEvent;
import me.lucko.luckperms.api.event.source.Source;
import me.lucko.luckperms.api.event.user.track.UserPromoteEvent;
import me.lucko.luckperms.extension.legacyapi.impl.LegacyApiProvider;
import me.lucko.luckperms.extension.legacyapi.impl.permissionholders.UserProxy;
import me.lucko.luckperms.extension.legacyapi.impl.track.TrackProxy;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;

public class UserPromoteEventProxy implements UserPromoteEvent {
    private final LegacyApiProvider api;
    private final net.luckperms.api.event.user.track.UserPromoteEvent event;

    public UserPromoteEventProxy(LegacyApiProvider api, net.luckperms.api.event.user.track.UserPromoteEvent event) {
        this.api = api;
        this.event = event;
    }

    @Override
    public @NonNull Track getTrack() {
        return new TrackProxy(event.getTrack());
    }

    @Override
    public @NonNull User getUser() {
        return new UserProxy(event.getUser());
    }

    @Override
    public @NonNull Optional<String> getGroupFrom() {
        return event.getGroupFrom();
    }

    @Override
    public @NonNull Optional<String> getGroupTo() {
        return event.getGroupTo();
    }

    @Override
    public @NonNull LuckPermsApi getApi() {
        return api;
    }

    @Override
    public @NonNull Class<? extends LuckPermsEvent> getEventType() {
        return UserPromoteEvent.class;
    }

    @Override
    public @NonNull Source getSource() {
        throw new UnsupportedOperationException();
    }
}
