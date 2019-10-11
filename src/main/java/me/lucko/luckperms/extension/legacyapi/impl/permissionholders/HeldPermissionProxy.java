package me.lucko.luckperms.extension.legacyapi.impl.permissionholders;

import me.lucko.luckperms.api.HeldPermission;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.context.ContextSet;
import me.lucko.luckperms.extension.legacyapi.impl.node.NodeProxy;
import net.luckperms.api.node.HeldNode;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;
import java.util.OptionalLong;

public class HeldPermissionProxy<T> implements HeldPermission<T> {
    private final HeldNode<T> heldNode;

    public HeldPermissionProxy(HeldNode<T> heldNode) {
        this.heldNode = heldNode;
    }

    @Override
    public @NonNull T getHolder() {
        return this.heldNode.getHolder();
    }

    @Override
    public @NonNull String getPermission() {
        return this.heldNode.getNode().getKey();
    }

    @Override
    public boolean getValue() {
        return this.heldNode.getNode().getValue();
    }

    @Override
    public @NonNull Optional<String> getServer() {
        return asNode().getServer();
    }

    @Override
    public @NonNull Optional<String> getWorld() {
        return asNode().getWorld();
    }

    @Override
    public @NonNull OptionalLong getExpiry() {
        return this.heldNode.getNode().getExpiry() != null ? OptionalLong.of(this.heldNode.getNode().getExpiry().toEpochMilli()) : OptionalLong.empty();
    }

    @Override
    public @NonNull ContextSet getContexts() {
        return asNode().getFullContexts();
    }

    @Override
    public @NonNull Node asNode() {
        return new NodeProxy(this.heldNode.getNode());
    }
}
