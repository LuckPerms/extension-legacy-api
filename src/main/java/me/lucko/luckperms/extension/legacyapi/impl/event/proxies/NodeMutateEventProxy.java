package me.lucko.luckperms.extension.legacyapi.impl.event.proxies;

import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.PermissionHolder;
import me.lucko.luckperms.api.event.LuckPermsEvent;
import me.lucko.luckperms.api.event.node.NodeMutateEvent;
import me.lucko.luckperms.extension.legacyapi.impl.LegacyApiProvider;
import me.lucko.luckperms.extension.legacyapi.impl.node.NodeProxy;
import me.lucko.luckperms.extension.legacyapi.impl.permissionholders.GroupProxy;
import me.lucko.luckperms.extension.legacyapi.impl.permissionholders.UserProxy;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Set;
import java.util.stream.Collectors;

public class NodeMutateEventProxy implements NodeMutateEvent {
    private final LegacyApiProvider api;
    private final net.luckperms.api.event.node.NodeMutateEvent event;

    public NodeMutateEventProxy(LegacyApiProvider api, net.luckperms.api.event.node.NodeMutateEvent event) {
        this.api = api;
        this.event = event;
    }

    @Override
    public @NonNull PermissionHolder getTarget() {
        if (event.getTarget() instanceof User) {
            return new UserProxy((User) event.getTarget());
        } else {
            return new GroupProxy((Group) event.getTarget());
        }
    }

    @Override
    public @NonNull Set<Node> getDataBefore() {
        return event.getDataBefore().stream().map(NodeProxy::new).collect(Collectors.toSet());
    }

    @Override
    public @NonNull Set<Node> getDataAfter() {
        return event.getDataAfter().stream().map(NodeProxy::new).collect(Collectors.toSet());
    }

    @Override
    public @NonNull LuckPermsApi getApi() {
        return api;
    }

    @Override
    public @NonNull Class<? extends LuckPermsEvent> getEventType() {
        return NodeMutateEvent.class;
    }
}
