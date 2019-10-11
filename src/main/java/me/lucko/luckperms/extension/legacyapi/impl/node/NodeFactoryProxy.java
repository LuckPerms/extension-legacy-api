package me.lucko.luckperms.extension.legacyapi.impl.node;

import me.lucko.luckperms.api.ChatMetaType;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.NodeFactory;
import net.luckperms.api.node.NodeBuilderRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

public class NodeFactoryProxy implements NodeFactory {
    private final NodeBuilderRegistry builders;

    public NodeFactoryProxy(NodeBuilderRegistry builders) {
        this.builders = builders;
    }

    @Override
    public Node.@NonNull Builder newBuilder(@NonNull String permission) {
        return new NodeBuilderProxy(this.builders.forKey(permission));
    }

    @Override
    public Node.@NonNull Builder newBuilderFromExisting(@NonNull Node other) {
        return other.toBuilder();
    }

    @Override
    public Node.@NonNull Builder makeGroupNode(@NonNull Group group) {
        return new NodeBuilderProxy(this.builders.forInheritance().group(group.getName()));
    }

    @Override
    public Node.@NonNull Builder makeGroupNode(@NonNull String groupName) {
        return new NodeBuilderProxy(this.builders.forInheritance().group(groupName));
    }

    @Override
    public Node.@NonNull Builder makeMetaNode(@NonNull String key, @NonNull String value) {
        return new NodeBuilderProxy(this.builders.forMeta().key(key).value(value));
    }

    @Override
    public Node.@NonNull Builder makeChatMetaNode(@NonNull ChatMetaType type, int priority, @NonNull String value) {
        switch (type) {
            case PREFIX:
                return new NodeBuilderProxy(this.builders.forPrefix().prefix(value).priority(priority));
            case SUFFIX:
                return new NodeBuilderProxy(this.builders.forSuffix().suffix(value).priority(priority));
            default:
                throw new AssertionError();
        }
    }

    @Override
    public Node.@NonNull Builder makePrefixNode(int priority, @NonNull String prefix) {
        return new NodeBuilderProxy(this.builders.forPrefix().prefix(prefix).priority(priority));
    }

    @Override
    public Node.@NonNull Builder makeSuffixNode(int priority, @NonNull String suffix) {
        return new NodeBuilderProxy(this.builders.forSuffix().suffix(suffix).priority(priority));
    }
}
