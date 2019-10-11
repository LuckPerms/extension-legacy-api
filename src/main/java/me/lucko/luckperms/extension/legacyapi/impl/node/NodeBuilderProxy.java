package me.lucko.luckperms.extension.legacyapi.impl.node;

import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.context.ContextSet;
import me.lucko.luckperms.extension.legacyapi.impl.contextset.ContextSetProxyUtil;
import net.luckperms.api.context.DefaultContextKeys;
import net.luckperms.api.node.NodeBuilder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;
import java.util.Set;

public class NodeBuilderProxy implements Node.Builder {
    private NodeBuilder<?, ?> builder;

    public NodeBuilderProxy(NodeBuilder<?, ?> builder) {
        this.builder = builder;
    }

    public NodeBuilderProxy(String key) {
        this.builder = net.luckperms.api.node.Node.builder(key);
    }

    public NodeBuilder<?, ?> getBuilder() {
        return this.builder;
    }

    @Override
    public @NonNull NodeBuilderProxy copyFrom(@NonNull Node node) {
        if (node instanceof NodeProxy) {
            this.builder = ((NodeProxy) node).getUnderlyingNode().toBuilder();
        } else {
            this.builder = net.luckperms.api.node.Node.builder(node.getPermission());
            setValue(node.getValue());
            if (node.isTemporary()) {
                setExpiry(node.getExpiryUnixTime());
            }
            setExtraContext(node.getFullContexts());
        }
        return this;
    }

    @Override
    public @NonNull NodeBuilderProxy setNegated(boolean negated) {
        this.builder.value(!negated);
        return this;
    }

    @Override
    public @NonNull NodeBuilderProxy setValue(boolean value) {
        this.builder.value(value);
        return this;
    }

    @Override
    public @NonNull NodeBuilderProxy setOverride(boolean override) {
        return this;
    }

    @Override
    public @NonNull NodeBuilderProxy setExpiry(long expiryUnixTimestamp) {
        this.builder.expiry(expiryUnixTimestamp);
        return this;
    }

    @Override
    public @NonNull NodeBuilderProxy clearExpiry() {
        this.builder.clearExpiry();
        return this;
    }

    @Override
    public @NonNull NodeBuilderProxy setWorld(@Nullable String world) {
        this.builder.withContext(DefaultContextKeys.WORLD_KEY, world);
        return this;
    }

    @Override
    public @NonNull NodeBuilderProxy setServer(@Nullable String server) {
        this.builder.withContext(DefaultContextKeys.SERVER_KEY, server);
        return this;
    }

    @Override
    public @NonNull NodeBuilderProxy withExtraContext(@NonNull String key, @NonNull String value) {
        this.builder.withContext(key, value);
        return this;
    }

    @Override
    public @NonNull NodeBuilderProxy withExtraContext(@NonNull Map<String, String> map) {
        withExtraContext(map.entrySet());
        return this;
    }

    @Override
    public @NonNull NodeBuilderProxy withExtraContext(@NonNull Set<Map.Entry<String, String>> context) {
        for (Map.Entry<String, String> entry : context) {
            withExtraContext(entry);
        }
        return this;
    }

    @Override
    public @NonNull NodeBuilderProxy withExtraContext(Map.@NonNull Entry<String, String> entry) {
        return withExtraContext(entry.getKey(), entry.getValue());
    }

    @Override
    public @NonNull NodeBuilderProxy withExtraContext(@NonNull ContextSet contextSet) {
        this.builder.withContext(ContextSetProxyUtil.modernImmutable(contextSet));
        return this;
    }

    @Override
    public @NonNull NodeBuilderProxy setExtraContext(@NonNull ContextSet contextSet) {
        this.builder.context(ContextSetProxyUtil.modernImmutable(contextSet));
        return this;
    }

    public net.luckperms.api.node.Node buildModern() {
        return this.builder.build();
    }
    
    @Override
    public @NonNull Node build() {
        return new NodeProxy(buildModern());
    }
}
