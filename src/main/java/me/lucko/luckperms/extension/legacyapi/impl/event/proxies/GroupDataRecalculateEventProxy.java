package me.lucko.luckperms.extension.legacyapi.impl.event.proxies;

import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.caching.GroupData;
import me.lucko.luckperms.api.event.LuckPermsEvent;
import me.lucko.luckperms.api.event.group.GroupDataRecalculateEvent;
import me.lucko.luckperms.extension.legacyapi.impl.LegacyApiProvider;
import me.lucko.luckperms.extension.legacyapi.impl.cacheddata.CachedDataProxy;
import me.lucko.luckperms.extension.legacyapi.impl.permissionholders.GroupProxy;
import org.checkerframework.checker.nullness.qual.NonNull;

public class GroupDataRecalculateEventProxy implements GroupDataRecalculateEvent {
    private final LegacyApiProvider api;
    private final net.luckperms.api.event.group.GroupDataRecalculateEvent event;

    public GroupDataRecalculateEventProxy(LegacyApiProvider api, net.luckperms.api.event.group.GroupDataRecalculateEvent event) {
        this.api = api;
        this.event = event;
    }

    @Override
    public @NonNull Group getGroup() {
        return new GroupProxy(event.getGroup());
    }

    @Override
    public @NonNull GroupData getData() {
        return new CachedDataProxy(event.getData());
    }

    @Override
    public @NonNull LuckPermsApi getApi() {
        return api;
    }

    @Override
    public @NonNull Class<? extends LuckPermsEvent> getEventType() {
        return GroupDataRecalculateEvent.class;
    }
}
