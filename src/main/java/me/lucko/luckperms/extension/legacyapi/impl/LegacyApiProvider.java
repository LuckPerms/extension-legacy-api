package me.lucko.luckperms.extension.legacyapi.impl;

import me.lucko.luckperms.api.ActionLogger;
import me.lucko.luckperms.api.LPConfiguration;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.MessagingService;
import me.lucko.luckperms.api.NodeFactory;
import me.lucko.luckperms.api.Storage;
import me.lucko.luckperms.api.UuidCache;
import me.lucko.luckperms.api.context.ContextManager;
import me.lucko.luckperms.api.event.EventBus;
import me.lucko.luckperms.api.manager.GroupManager;
import me.lucko.luckperms.api.manager.TrackManager;
import me.lucko.luckperms.api.manager.UserManager;
import me.lucko.luckperms.api.messenger.MessengerProvider;
import me.lucko.luckperms.api.metastacking.MetaStackFactory;
import me.lucko.luckperms.api.platform.PlatformInfo;
import me.lucko.luckperms.extension.legacyapi.impl.actionlogger.ActionLoggerProxy;
import me.lucko.luckperms.extension.legacyapi.impl.contextset.ContextManagerProxy;
import me.lucko.luckperms.extension.legacyapi.impl.event.EventBusProxy;
import me.lucko.luckperms.extension.legacyapi.impl.messaging.MessagingServiceProxy;
import me.lucko.luckperms.extension.legacyapi.impl.metastack.MetaStackFactoryProxy;
import me.lucko.luckperms.extension.legacyapi.impl.misc.DummyUuidCache;
import me.lucko.luckperms.extension.legacyapi.impl.misc.ForwardingStorageProxy;
import me.lucko.luckperms.extension.legacyapi.impl.misc.LPConfigurationProxy;
import me.lucko.luckperms.extension.legacyapi.impl.node.NodeFactoryProxy;
import me.lucko.luckperms.extension.legacyapi.impl.permissionholders.GroupManagerProxy;
import me.lucko.luckperms.extension.legacyapi.impl.permissionholders.UserManagerProxy;
import me.lucko.luckperms.extension.legacyapi.impl.platform.PlatformInfoProxy;
import me.lucko.luckperms.extension.legacyapi.impl.track.TrackManagerProxy;
import net.luckperms.api.LuckPerms;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class LegacyApiProvider implements LuckPermsApi {
    private final LuckPerms luckPerms;
    private final EventBusProxy eventBusProxy;

    public LegacyApiProvider(LuckPerms luckPerms) {
        this.luckPerms = luckPerms;
        this.eventBusProxy = new EventBusProxy(this, luckPerms);
    }

    @Override
    public PlatformInfo getPlatformInfo() {
        return new PlatformInfoProxy(this.luckPerms.getPlatform(), this.luckPerms.getPluginMetadata());
    }

    @Override
    public UserManager getUserManager() {
        return new UserManagerProxy(this.luckPerms.getUserManager());
    }

    @Override
    public GroupManager getGroupManager() {
        return new GroupManagerProxy(this.luckPerms.getGroupManager());
    }

    @Override
    public TrackManager getTrackManager() {
        return new TrackManagerProxy(this.luckPerms.getTrackManager());
    }

    @Override
    public CompletableFuture<Void> runUpdateTask() {
        return this.luckPerms.runUpdateTask();
    }

    @Override
    public EventBus getEventBus() {
        return this.eventBusProxy;
    }

    @Override
    public LPConfiguration getConfiguration() {
        return new LPConfigurationProxy(this.luckPerms);
    }

    @Override
    public Storage getStorage() {
        return new ForwardingStorageProxy(getActionLogger(), getUserManager(), getGroupManager(), getTrackManager());
    }

    @Override
    public Optional<MessagingService> getMessagingService() {
        return this.luckPerms.getMessagingService().map(MessagingServiceProxy::new);
    }

    @Override
    public void registerMessengerProvider(MessengerProvider messengerProvider) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ActionLogger getActionLogger() {
        return new ActionLoggerProxy(this.luckPerms.getActionLogger());
    }

    @SuppressWarnings("deprecation")
    @Override
    public UuidCache getUuidCache() {
        return DummyUuidCache.INSTANCE;
    }

    @Override
    public ContextManager getContextManager() {
        return new ContextManagerProxy(this.luckPerms.getContextManager());
    }

    @Override
    public Collection<String> getKnownPermissions() {
        return this.luckPerms.getPlatform().getKnownPermissions();
    }

    @Override
    public NodeFactory getNodeFactory() {
        return new NodeFactoryProxy(this.luckPerms.getNodeBuilderRegistry());
    }

    @Override
    public MetaStackFactory getMetaStackFactory() {
        return new MetaStackFactoryProxy(this.luckPerms.getMetaStackFactory());
    }
}
