package me.lucko.luckperms.extension.legacyapi.impl.event.proxies;

import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.caching.GroupData;
import me.lucko.luckperms.api.event.LuckPermsEvent;
import me.lucko.luckperms.api.event.group.GroupCacheLoadEvent;
import me.lucko.luckperms.extension.legacyapi.impl.LegacyApiProvider;
import me.lucko.luckperms.extension.legacyapi.impl.cacheddata.CachedDataProxy;
import me.lucko.luckperms.extension.legacyapi.impl.permissionholders.GroupProxy;
import org.checkerframework.checker.nullness.qual.NonNull;

public class GroupCacheLoadEventProxy implements GroupCacheLoadEvent {
    private final LegacyApiProvider api;
    private final net.luckperms.api.event.group.GroupCacheLoadEvent event;

    public GroupCacheLoadEventProxy(LegacyApiProvider api, net.luckperms.api.event.group.GroupCacheLoadEvent event) {
        this.api = api;
        this.event = event;
    }

    @Override
    public @NonNull Group getGroup() {
        return new GroupProxy(this.event.getGroup());
    }

    @Override
    public @NonNull GroupData getLoadedData() {
        return new CachedDataProxy(this.event.getLoadedData());
    }

    @Override
    public @NonNull LuckPermsApi getApi() {
        return this.api;
    }

    @Override
    public @NonNull Class<? extends LuckPermsEvent> getEventType() {
        return GroupCacheLoadEvent.class;
    }
}
