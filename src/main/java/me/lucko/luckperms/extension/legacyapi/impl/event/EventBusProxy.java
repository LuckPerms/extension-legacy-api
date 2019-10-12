package me.lucko.luckperms.extension.legacyapi.impl.event;

import me.lucko.luckperms.api.event.EventBus;
import me.lucko.luckperms.api.event.EventHandler;
import me.lucko.luckperms.api.event.LuckPermsEvent;
import me.lucko.luckperms.api.event.group.GroupCacheLoadEvent;
import me.lucko.luckperms.api.event.group.GroupDataRecalculateEvent;
import me.lucko.luckperms.api.event.node.NodeAddEvent;
import me.lucko.luckperms.api.event.node.NodeClearEvent;
import me.lucko.luckperms.api.event.node.NodeMutateEvent;
import me.lucko.luckperms.api.event.node.NodeRemoveEvent;
import me.lucko.luckperms.api.event.user.UserCacheLoadEvent;
import me.lucko.luckperms.api.event.user.UserDataRecalculateEvent;
import me.lucko.luckperms.api.event.user.UserFirstLoginEvent;
import me.lucko.luckperms.api.event.user.UserLoadEvent;
import me.lucko.luckperms.api.event.user.track.UserDemoteEvent;
import me.lucko.luckperms.api.event.user.track.UserPromoteEvent;
import me.lucko.luckperms.api.event.user.track.UserTrackEvent;
import me.lucko.luckperms.extension.legacyapi.impl.LegacyApiProvider;
import me.lucko.luckperms.extension.legacyapi.impl.event.proxies.GroupCacheLoadEventProxy;
import me.lucko.luckperms.extension.legacyapi.impl.event.proxies.GroupDataRecalculateEventProxy;
import me.lucko.luckperms.extension.legacyapi.impl.event.proxies.NodeAddEventProxy;
import me.lucko.luckperms.extension.legacyapi.impl.event.proxies.NodeClearEventProxy;
import me.lucko.luckperms.extension.legacyapi.impl.event.proxies.NodeMutateEventProxy;
import me.lucko.luckperms.extension.legacyapi.impl.event.proxies.NodeRemoveEventProxy;
import me.lucko.luckperms.extension.legacyapi.impl.event.proxies.UserCacheLoadEventProxy;
import me.lucko.luckperms.extension.legacyapi.impl.event.proxies.UserDataRecalculateEventProxy;
import me.lucko.luckperms.extension.legacyapi.impl.event.proxies.UserDemoteEventProxy;
import me.lucko.luckperms.extension.legacyapi.impl.event.proxies.UserFirstLoginEventProxy;
import me.lucko.luckperms.extension.legacyapi.impl.event.proxies.UserLoadEventProxy;
import me.lucko.luckperms.extension.legacyapi.impl.event.proxies.UserPromoteEventProxy;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.EventSubscription;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class EventBusProxy implements EventBus {
    private final LegacyApiProvider apiProvider;
    private final LuckPerms luckPerms;

    private final Map<Class<? extends LuckPermsEvent>, Mapping<?, ?>> mappings = new HashMap<>();

    public EventBusProxy(LegacyApiProvider apiProvider, LuckPerms luckPerms) {
        this.apiProvider = apiProvider;
        this.luckPerms = luckPerms;

        addMapping(new Mapping<>(GroupCacheLoadEvent.class, net.luckperms.api.event.group.GroupCacheLoadEvent.class, GroupCacheLoadEventProxy::new));
        addMapping(new Mapping<>(GroupDataRecalculateEvent.class, net.luckperms.api.event.group.GroupDataRecalculateEvent.class, GroupDataRecalculateEventProxy::new));

        addMapping(new Mapping<>(NodeAddEvent.class, net.luckperms.api.event.node.NodeAddEvent.class, NodeAddEventProxy::new));
        addMapping(new Mapping<>(NodeClearEvent.class, net.luckperms.api.event.node.NodeClearEvent.class, NodeClearEventProxy::new));
        addMapping(new Mapping<>(NodeMutateEvent.class, net.luckperms.api.event.node.NodeMutateEvent.class, (api, event) -> {
            if (event instanceof net.luckperms.api.event.node.NodeAddEvent) {
                return new NodeAddEventProxy(api, (net.luckperms.api.event.node.NodeAddEvent) event);
            }
            if (event instanceof net.luckperms.api.event.node.NodeClearEvent) {
                return new NodeClearEventProxy(api, (net.luckperms.api.event.node.NodeClearEvent) event);
            }
            if (event instanceof net.luckperms.api.event.node.NodeRemoveEvent) {
                return new NodeRemoveEventProxy(api, (net.luckperms.api.event.node.NodeRemoveEvent) event);
            }
            return new NodeMutateEventProxy(api, event);
        }));
        addMapping(new Mapping<>(NodeRemoveEvent.class, net.luckperms.api.event.node.NodeRemoveEvent.class, NodeRemoveEventProxy::new));

        addMapping(new Mapping<>(UserCacheLoadEvent.class, net.luckperms.api.event.user.UserCacheLoadEvent.class, UserCacheLoadEventProxy::new));
        addMapping(new Mapping<>(UserDataRecalculateEvent.class, net.luckperms.api.event.user.UserDataRecalculateEvent.class, UserDataRecalculateEventProxy::new));
        addMapping(new Mapping<>(UserDemoteEvent.class, net.luckperms.api.event.user.track.UserDemoteEvent.class, UserDemoteEventProxy::new));
        addMapping(new Mapping<>(UserFirstLoginEvent.class, net.luckperms.api.event.user.UserFirstLoginEvent.class, UserFirstLoginEventProxy::new));
        addMapping(new Mapping<>(UserLoadEvent.class, net.luckperms.api.event.user.UserLoadEvent.class, UserLoadEventProxy::new));
        addMapping(new Mapping<>(UserPromoteEvent.class, net.luckperms.api.event.user.track.UserPromoteEvent.class, UserPromoteEventProxy::new));
        addMapping(new Mapping<>(UserTrackEvent.class, net.luckperms.api.event.user.track.UserTrackEvent.class, (api, event) -> {
            if (event instanceof net.luckperms.api.event.user.track.UserPromoteEvent) {
                return new UserPromoteEventProxy(api, (net.luckperms.api.event.user.track.UserPromoteEvent) event);
            }
            if (event instanceof net.luckperms.api.event.user.track.UserDemoteEvent) {
                return new UserDemoteEventProxy(api, (net.luckperms.api.event.user.track.UserDemoteEvent) event);
            }
            throw new IllegalArgumentException("Unknown track event type: " + event.getEventType().getName());
        }));
    }
    
    private <T extends LuckPermsEvent> void addMapping(Mapping<T, ?> mapping) {
        this.mappings.put(mapping.legacyClass, mapping);
    }

    @Override
    public @NonNull <T extends LuckPermsEvent> EventHandler<T> subscribe(@NonNull Class<T> eventClass, @NonNull Consumer<? super T> handler) {
        //noinspection unchecked
        Mapping<T, ?> mapping = (Mapping<T, ?>) this.mappings.get(eventClass);
        if (mapping == null) {
            throw new UnsupportedOperationException("Unable to proxy " + eventClass.getName() + " events");
        }

        EventSubscription<?> subscription = mapping.registerSubscription(handler);
        return new ProxiedEventHandler<>(eventClass, subscription, handler);
    }

    @Override
    public @NonNull <T extends LuckPermsEvent> EventHandler<T> subscribe(Object plugin, @NonNull Class<T> eventClass, @NonNull Consumer<? super T> handler) {
        //noinspection unchecked
        Mapping<T, ?> mapping = (Mapping<T, ?>) this.mappings.get(eventClass);
        if (mapping == null) {
            throw new UnsupportedOperationException("Unable to proxy " + eventClass.getName() + " events");
        }

        EventSubscription<?> subscription = mapping.registerSubscription(plugin, handler);
        return new ProxiedEventHandler<>(eventClass, subscription, handler);
    }

    @Override
    public @NonNull <T extends LuckPermsEvent> Set<EventHandler<T>> getHandlers(@NonNull Class<T> eventClass) {
        throw new UnsupportedOperationException();
    }

    private final class Mapping<L extends LuckPermsEvent, M extends net.luckperms.api.event.LuckPermsEvent> {
        private final Class<L> legacyClass;
        private final Class<M> modernClass;
        private final BiFunction<LegacyApiProvider, M, L> proxyFactory;

        private Mapping(Class<L> legacyClass, Class<M> modernClass, BiFunction<LegacyApiProvider, M, L> proxyFactory) {
            this.legacyClass = legacyClass;
            this.modernClass = modernClass;
            this.proxyFactory = proxyFactory;
        }

        EventSubscription<M> registerSubscription(Consumer<? super L> consumer) {
            return EventBusProxy.this.luckPerms.getEventBus().subscribe(this.modernClass, m -> {
                L legacy = this.proxyFactory.apply(EventBusProxy.this.apiProvider, m);
                consumer.accept(legacy);
            });
        }

        EventSubscription<M> registerSubscription(Object plugin, Consumer<? super L> consumer) {
            return EventBusProxy.this.luckPerms.getEventBus().subscribe(plugin, this.modernClass, m -> {
                L legacy = this.proxyFactory.apply(EventBusProxy.this.apiProvider, m);
                consumer.accept(legacy);
            });
        }
    }

    private final class ProxiedEventHandler<T extends LuckPermsEvent> implements EventHandler<T> {
        private final Class<T> type;
        private final EventSubscription<?> parentSubscription;
        private final Consumer<? super T> consumer;

        private ProxiedEventHandler(Class<T> type, EventSubscription<?> parentSubscription, Consumer<? super T> consumer) {
            this.type = type;
            this.parentSubscription = parentSubscription;
            this.consumer = consumer;
        }

        @Override
        public @NonNull Class<T> getEventClass() {
            return this.type;
        }

        @Override
        public boolean isActive() {
            return this.parentSubscription.isActive();
        }

        @Override
        public boolean unregister() {
            this.parentSubscription.close();
            return true;
        }

        @Override
        public @NonNull Consumer<? super T> getConsumer() {
            return this.consumer;
        }

        @Override
        public int getCallCount() {
            throw new UnsupportedOperationException();
        }
    }
}
