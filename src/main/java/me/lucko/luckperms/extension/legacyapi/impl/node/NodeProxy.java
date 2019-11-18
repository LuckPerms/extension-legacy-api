package me.lucko.luckperms.extension.legacyapi.impl.node;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import me.lucko.luckperms.api.LocalizedNode;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.StandardNodeEquality;
import me.lucko.luckperms.api.context.ContextSet;
import me.lucko.luckperms.api.nodetype.NodeType;
import me.lucko.luckperms.api.nodetype.NodeTypeKey;
import me.lucko.luckperms.api.nodetype.types.DisplayNameType;
import me.lucko.luckperms.api.nodetype.types.InheritanceType;
import me.lucko.luckperms.api.nodetype.types.MetaType;
import me.lucko.luckperms.api.nodetype.types.PrefixType;
import me.lucko.luckperms.api.nodetype.types.RegexType;
import me.lucko.luckperms.api.nodetype.types.SuffixType;
import me.lucko.luckperms.api.nodetype.types.WeightType;
import me.lucko.luckperms.extension.legacyapi.impl.contextset.ContextSetProxyUtil;
import net.luckperms.api.context.DefaultContextKeys;
import net.luckperms.api.context.MutableContextSet;
import net.luckperms.api.node.NodeEqualityPredicate;
import net.luckperms.api.node.metadata.types.InheritanceOriginMetadata;
import net.luckperms.api.node.types.DisplayNameNode;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.MetaNode;
import net.luckperms.api.node.types.PermissionNode;
import net.luckperms.api.node.types.PrefixNode;
import net.luckperms.api.node.types.RegexPermissionNode;
import net.luckperms.api.node.types.SuffixNode;
import net.luckperms.api.node.types.WeightNode;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.regex.Pattern;

public class NodeProxy implements LocalizedNode {
    private final net.luckperms.api.node.Node node;

    public NodeProxy(net.luckperms.api.node.Node node) {
        this.node = node;
    }

    public net.luckperms.api.node.Node getUnderlyingNode() {
        return this.node;
    }

    @Override
    public @NonNull String getPermission() {
        return this.node.getKey();
    }

    @Override
    public boolean getValue() {
        return this.node.getValue();
    }

    @Override
    public boolean isOverride() {
        return false;
    }

    @Override
    public @NonNull Optional<String> getServer() {
        return this.node.getContexts().getAnyValue(DefaultContextKeys.SERVER_KEY);
    }

    @Override
    public @NonNull Optional<String> getWorld() {
        return this.node.getContexts().getAnyValue(DefaultContextKeys.WORLD_KEY);
    }

    @Override
    public boolean isServerSpecific() {
        return this.node.getContexts().containsKey(DefaultContextKeys.SERVER_KEY);
    }

    @Override
    public boolean isWorldSpecific() {
        return this.node.getContexts().containsKey(DefaultContextKeys.WORLD_KEY);
    }

    @Override
    public boolean appliesGlobally() {
        return this.node.getContexts().isEmpty();
    }

    @Override
    public boolean hasSpecificContext() {
        return !this.node.getContexts().isEmpty();
    }

    @Override
    public boolean shouldApplyWithContext(@NonNull ContextSet contextSet) {
        return this.node.getContexts().isSatisfiedBy(ContextSetProxyUtil.modernImmutable(contextSet));
    }

    @Override
    public @NonNull List<String> resolveShorthand() {
        Collection<String> shorthand = this.node.resolveShorthand();
        return shorthand instanceof List<?> ? ((List<String>) shorthand) : ImmutableList.copyOf(shorthand);
    }

    @Override
    public boolean isTemporary() {
        return this.node.hasExpiry();
    }

    @Override
    public long getExpiryUnixTime() throws IllegalStateException {
        Instant expiry = this.node.getExpiry();
        if (expiry == null) {
            throw new IllegalStateException();
        }
        return expiry.getEpochSecond();
    }

    @Override
    public @NonNull Date getExpiry() throws IllegalStateException {
        Instant expiry = this.node.getExpiry();
        if (expiry == null) {
            throw new IllegalStateException();
        }
        return Date.from(expiry);
    }

    @Override
    public long getSecondsTilExpiry() throws IllegalStateException {
        Instant expiry = this.node.getExpiry();
        if (expiry == null) {
            throw new IllegalStateException();
        }

        return ChronoUnit.SECONDS.between(Instant.now(), expiry);
    }

    @Override
    public boolean hasExpired() {
        return this.node.hasExpired();
    }

    @Override
    public @NonNull ContextSet getContexts() {
        MutableContextSet contexts = this.node.getContexts().mutableCopy();
        contexts.removeAll(DefaultContextKeys.SERVER_KEY);
        contexts.removeAll(DefaultContextKeys.WORLD_KEY);
        return ContextSetProxyUtil.legacyImmutable(contexts);
    }

    @Override
    public @NonNull ContextSet getFullContexts() {
        return ContextSetProxyUtil.legacyImmutable(this.node.getContexts());
    }

    @Override
    public boolean isWildcard() {
        return this.node instanceof PermissionNode && ((PermissionNode) this.node).isWildcard();
    }

    @Override
    public int getWildcardLevel() throws IllegalStateException {
        if (!isWildcard()) {
            throw new IllegalStateException();
        }

        OptionalInt wildcardLevel = ((PermissionNode) this.node).getWildcardLevel();
        if (!wildcardLevel.isPresent()) {
            throw new IllegalStateException();
        }
        return wildcardLevel.getAsInt();
    }

    @Override
    public boolean hasTypeData() {
        return this instanceof InheritanceNode ||
                this instanceof RegexPermissionNode ||
                this instanceof PrefixNode ||
                this instanceof SuffixNode ||
                this instanceof MetaNode ||
                this instanceof DisplayNameNode ||
                this instanceof WeightNode;
    }


    private NodeType getAsType(NodeTypeKey<?> key) {
        if (key == InheritanceType.KEY && this.node instanceof InheritanceNode) {
            return new InheritanceType() {
                @Override
                public @NonNull String getGroupName() {
                    return ((InheritanceNode) NodeProxy.this.node).getGroupName();
                }
            };
        }
        if (key == RegexType.KEY && this.node instanceof RegexPermissionNode) {
            return new RegexType() {
                @Override
                public @NonNull String getPatternString() {
                    return ((RegexPermissionNode) NodeProxy.this.node).getPatternString();
                }

                @Override
                public @NonNull Optional<Pattern> getPattern() {
                    return ((RegexPermissionNode) NodeProxy.this.node).getPattern();
                }
            };
        }
        if (key == PrefixType.KEY && this.node instanceof PrefixNode) {
            return new PrefixType() {
                @Override
                public int getPriority() {
                    return ((PrefixNode) NodeProxy.this.node).getPriority();
                }

                @Override
                public @NonNull String getPrefix() {
                    return ((PrefixNode) NodeProxy.this.node).getMetaValue();
                }

                @Override
                public Map.@NonNull Entry<Integer, String> getAsEntry() {
                    return Maps.immutableEntry(getPriority(), getPrefix());
                }
            };
        }
        if (key == SuffixType.KEY && this.node instanceof SuffixNode) {
            return new SuffixType() {
                @Override
                public int getPriority() {
                    return ((SuffixNode) NodeProxy.this.node).getPriority();
                }

                @Override
                public @NonNull String getSuffix() {
                    return ((SuffixNode) NodeProxy.this.node).getMetaValue();
                }

                @Override
                public Map.@NonNull Entry<Integer, String> getAsEntry() {
                    return Maps.immutableEntry(getPriority(), getSuffix());
                }
            };
        }
        if (key == MetaType.KEY && this.node instanceof MetaNode) {
            return new MetaType() {
                @Override
                public @NonNull String getKey() {
                    return ((MetaNode) NodeProxy.this.node).getMetaKey();
                }

                @Override
                public @NonNull String getValue() {
                    return ((MetaNode) NodeProxy.this.node).getMetaValue();
                }
            };
        }
        if (key == DisplayNameType.KEY && this.node instanceof DisplayNameNode) {
            return new DisplayNameType() {
                @Override
                public @NonNull String getDisplayName() {
                    return ((DisplayNameNode) NodeProxy.this.node).getDisplayName();
                }
            };
        }
        if (key == WeightType.KEY && this.node instanceof WeightNode) {
            return (WeightType) ((WeightNode) this.node)::getWeight;
        }

        return null;
    }

    @Override
    public <T extends NodeType> Optional<T> getTypeData(NodeTypeKey<T> key) {
        return (Optional<T>) Optional.ofNullable(getAsType(key));
    }

    @Override
    public boolean equals(Node other, me.lucko.luckperms.api.NodeEqualityPredicate equalityPredicate) {
        if (equalityPredicate instanceof StandardNodeEquality) {
            return this.node.equals(((NodeProxy) other).node, modernNodeEquality((StandardNodeEquality) equalityPredicate));
        }
        throw new UnsupportedOperationException("legacy API support is only implemented for the standard equality predicates");
    }

    @Override
    public Builder toBuilder() {
        NodeBuilderProxy builder = new NodeBuilderProxy(this.node.getKey());
        builder.getBuilder().value(this.node.getValue())
                .context(this.node.getContexts())
                .expiry(this.node.getExpiry());

        Optional<InheritanceOriginMetadata> inheritanceOrigin = this.node.getMetadata(InheritanceOriginMetadata.KEY);
        if (inheritanceOrigin.isPresent()) {
            builder.getBuilder().withMetadata(InheritanceOriginMetadata.KEY, inheritanceOrigin.get());
        }

        return builder;
    }

    private static NodeEqualityPredicate modernNodeEquality(StandardNodeEquality equality) {
        switch (equality) {
            case EXACT:
                return NodeEqualityPredicate.EXACT;
            case IGNORE_VALUE:
                return NodeEqualityPredicate.IGNORE_VALUE;
            case IGNORE_EXPIRY_TIME:
                return NodeEqualityPredicate.IGNORE_EXPIRY_TIME;
            case IGNORE_EXPIRY_TIME_AND_VALUE:
                return NodeEqualityPredicate.IGNORE_EXPIRY_TIME_AND_VALUE;
            case IGNORE_VALUE_OR_IF_TEMPORARY:
                return NodeEqualityPredicate.IGNORE_VALUE_OR_IF_TEMPORARY;
            default:
                throw new AssertionError();
        }
    }

    @Override
    public @NonNull Node getNode() {
        return this;
    }

    @Override
    public @NonNull String getLocation() {
        Optional<InheritanceOriginMetadata> origin = this.node.getMetadata(InheritanceOriginMetadata.KEY);
        if (!origin.isPresent()) {
            throw new IllegalStateException("location information not present");
        }
        return origin.get().getOrigin().getName();
    }
}
