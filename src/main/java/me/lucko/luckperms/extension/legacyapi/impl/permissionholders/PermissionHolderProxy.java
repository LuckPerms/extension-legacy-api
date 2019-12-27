package me.lucko.luckperms.extension.legacyapi.impl.permissionholders;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Lists;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.DataMutateResult;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.LocalizedNode;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.NodeEqualityPredicate;
import me.lucko.luckperms.api.PermissionHolder;
import me.lucko.luckperms.api.StandardNodeEquality;
import me.lucko.luckperms.api.TemporaryDataMutateResult;
import me.lucko.luckperms.api.TemporaryMergeBehaviour;
import me.lucko.luckperms.api.Tristate;
import me.lucko.luckperms.api.context.ContextSet;
import me.lucko.luckperms.api.context.ImmutableContextSet;
import me.lucko.luckperms.extension.legacyapi.impl.cacheddata.CachedDataProxy;
import me.lucko.luckperms.extension.legacyapi.impl.contextset.ContextSetProxyUtil;
import me.lucko.luckperms.extension.legacyapi.impl.contextset.ContextsProxyUtil;
import me.lucko.luckperms.extension.legacyapi.impl.misc.DataMutateResultProxyUtil;
import me.lucko.luckperms.extension.legacyapi.impl.misc.FakeSortedSet;
import me.lucko.luckperms.extension.legacyapi.impl.misc.TemporaryDataMutateResultProxy;
import me.lucko.luckperms.extension.legacyapi.impl.node.NodeProxy;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.query.QueryOptions;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PermissionHolderProxy implements PermissionHolder {
    private final net.luckperms.api.model.PermissionHolder permissionHolder;

    public PermissionHolderProxy(net.luckperms.api.model.PermissionHolder permissionHolder) {
        this.permissionHolder = permissionHolder;
    }

    @Override
    public @NonNull String getObjectName() {
        return this.permissionHolder.getIdentifier().getName();
    }

    @Override
    public @NonNull String getFriendlyName() {
        return this.permissionHolder.getFriendlyName();
    }

    @Override
    public @NonNull CachedDataProxy getCachedData() {
        return new CachedDataProxy(this.permissionHolder.getCachedData());
    }

    @Override
    public @NonNull CompletableFuture<Void> refreshCachedData() {
        return CompletableFuture.runAsync(() -> this.permissionHolder.getCachedData().invalidate());
    }

    @Override
    public @NonNull ImmutableSetMultimap<ImmutableContextSet, Node> getNodes() {
        ImmutableSetMultimap.Builder<ImmutableContextSet, Node> builder = ImmutableSetMultimap.builder();
        this.permissionHolder.data().toMap().forEach((legacyContextSet, nodes) -> {
            ImmutableContextSet contextSet = ContextSetProxyUtil.legacyImmutable(legacyContextSet);
            nodes.forEach(node -> builder.put(contextSet, new NodeProxy(node)));
        });
        return builder.build();
    }

    @Override
    public @NonNull ImmutableSetMultimap<ImmutableContextSet, Node> getTransientNodes() {
        ImmutableSetMultimap.Builder<ImmutableContextSet, Node> builder = ImmutableSetMultimap.builder();
        this.permissionHolder.transientData().toMap().forEach((legacyContextSet, nodes) -> {
            ImmutableContextSet contextSet = ContextSetProxyUtil.legacyImmutable(legacyContextSet);
            nodes.forEach(node -> builder.put(contextSet, new NodeProxy(node)));
        });
        return builder.build();
    }

    @Override
    public @NonNull List<Node> getOwnNodes() {
        return Lists.transform(collectionToList(this.permissionHolder.getNodes()), NodeProxy::new);
    }

    @Override
    public @NonNull SortedSet<? extends Node> getPermissions() {
        return this.permissionHolder.getDistinctNodes().stream().map(NodeProxy::new).collect(Collectors.toCollection(FakeSortedSet::backedByLinkedHashSet));
    }

    @Override
    public @NonNull Set<? extends Node> getEnduringPermissions() {
        return this.permissionHolder.data().toCollection().stream().map(NodeProxy::new).collect(Collectors.toSet());
    }

    @Override
    public @NonNull Set<? extends Node> getTransientPermissions() {
        return this.permissionHolder.transientData().toCollection().stream().map(NodeProxy::new).collect(Collectors.toSet());
    }

    @Override
    public @NonNull Set<Node> getPermanentPermissionNodes() {
        return this.permissionHolder.getNodes().stream().filter(n -> !n.hasExpiry()).map(NodeProxy::new).collect(Collectors.toSet());
    }

    @Override
    public @NonNull Set<Node> getTemporaryPermissionNodes() {
        return this.permissionHolder.getNodes().stream().filter(n -> n.hasExpiry()).map(NodeProxy::new).collect(Collectors.toSet());
    }

    @Override
    public @NonNull List<LocalizedNode> resolveInheritances(@NonNull Contexts contexts) {
        return Lists.transform(collectionToList(this.permissionHolder.resolveInheritedNodes(ContextsProxyUtil.modernContexts(contexts))), NodeProxy::new);
    }

    @Override
    public @NonNull List<LocalizedNode> resolveInheritances() {
        return Lists.transform(collectionToList(this.permissionHolder.resolveInheritedNodes(QueryOptions.nonContextual())), NodeProxy::new);
    }

    @Override
    public @NonNull SortedSet<LocalizedNode> getAllNodes(@NonNull Contexts contexts) {
        return this.permissionHolder.resolveDistinctInheritedNodes(ContextsProxyUtil.modernContexts(contexts)).stream()
                .map(NodeProxy::new)
                .collect(Collectors.toCollection(FakeSortedSet::backedByLinkedHashSet));
    }

    @Override
    public @NonNull SortedSet<LocalizedNode> getAllNodes() {
        return this.permissionHolder.resolveDistinctInheritedNodes(QueryOptions.nonContextual()).stream()
                .map(NodeProxy::new)
                .collect(Collectors.toCollection(FakeSortedSet::backedByLinkedHashSet));
    }

    @Override
    public @NonNull Set<LocalizedNode> getAllNodesFiltered(@NonNull Contexts contexts) {
        Set<String> seen = new HashSet<>();
        List<NodeProxy> set = this.permissionHolder.resolveDistinctInheritedNodes(ContextsProxyUtil.modernContexts(contexts)).stream()
                .map(NodeProxy::new)
                .collect(Collectors.toList());
        set.removeIf(p -> !seen.add(p.getUnderlyingNode().getKey()));
        //noinspection unchecked
        return (Set) new FakeSortedSet<>(set);
    }

    @Override
    public @NonNull Map<String, Boolean> exportNodes(@NonNull Contexts contexts, boolean convertToLowercase) {
        return this.permissionHolder.resolveDistinctInheritedNodes(ContextsProxyUtil.modernContexts(contexts)).stream()
                .collect(Collectors.toMap(n -> convertToLowercase ? n.getKey().toLowerCase() : n.getKey(), net.luckperms.api.node.Node::getValue));
    }

    @Override
    public void auditTemporaryPermissions() {
        this.permissionHolder.auditTemporaryNodes();
    }

    @Override
    public @NonNull Tristate hasPermission(@NonNull Node node, @NonNull NodeEqualityPredicate equalityPredicate) {
        return this.permissionHolder.data().toCollection().stream()
                .filter(n -> node.equals(node, equalityPredicate))
                .findFirst()
                .map(n -> Tristate.fromBoolean(n.getValue()))
                .orElse(Tristate.UNDEFINED);
    }

    @Override
    public @NonNull Tristate hasTransientPermission(@NonNull Node node, @NonNull NodeEqualityPredicate equalityPredicate) {
        return this.permissionHolder.transientData().toCollection().stream()
                .filter(n -> node.equals(node, equalityPredicate))
                .findFirst()
                .map(n -> Tristate.fromBoolean(n.getValue()))
                .orElse(Tristate.UNDEFINED);
    }

    @Override
    public @NonNull Tristate inheritsPermission(@NonNull Node node, @NonNull NodeEqualityPredicate equalityPredicate) {
        return this.permissionHolder.resolveInheritedNodes(QueryOptions.nonContextual()).stream()
                .filter(n -> node.equals(node, equalityPredicate))
                .findFirst()
                .map(n -> Tristate.fromBoolean(n.getValue()))
                .orElse(Tristate.UNDEFINED);
    }

    @Override
    public @NonNull Tristate hasPermission(@NonNull Node node) {
        return hasPermission(node, StandardNodeEquality.IGNORE_VALUE_OR_IF_TEMPORARY);
    }

    @Override
    public @NonNull Tristate hasTransientPermission(@NonNull Node node) {
        return hasTransientPermission(node, StandardNodeEquality.IGNORE_VALUE_OR_IF_TEMPORARY);
    }

    @Override
    public @NonNull Tristate inheritsPermission(@NonNull Node node) {
        return inheritsPermission(node, StandardNodeEquality.IGNORE_VALUE_OR_IF_TEMPORARY);
    }

    @Override
    public boolean inheritsGroup(@NonNull Group group) {
        return this.permissionHolder.resolveInheritedNodes(QueryOptions.nonContextual()).stream()
                .filter(NodeType.INHERITANCE::matches)
                .map(NodeType.INHERITANCE::cast)
                .filter(net.luckperms.api.node.Node::getValue)
                .anyMatch(n -> n.getGroupName().equalsIgnoreCase(group.getName()));
    }

    @Override
    public boolean inheritsGroup(@NonNull Group group, @NonNull ContextSet contextSet) {
        return this.permissionHolder.resolveInheritedNodes(QueryOptions.contextual(ContextSetProxyUtil.modernImmutable(contextSet))).stream()
                .filter(NodeType.INHERITANCE::matches)
                .map(NodeType.INHERITANCE::cast)
                .filter(net.luckperms.api.node.Node::getValue)
                .anyMatch(n -> n.getGroupName().equalsIgnoreCase(group.getName()));
    }

    @Override
    public @NonNull DataMutateResult setPermission(@NonNull Node node) {
        return DataMutateResultProxyUtil.legacy(this.permissionHolder.data().add(((NodeProxy) node).getUnderlyingNode()));
    }

    @Override
    public @NonNull TemporaryDataMutateResult setPermission(@NonNull Node node, @NonNull TemporaryMergeBehaviour temporaryMergeBehaviour) {
        return new TemporaryDataMutateResultProxy(this.permissionHolder.data().add(((NodeProxy) node).getUnderlyingNode(), TemporaryNodeMergeStrategyProxyUtil.modern(temporaryMergeBehaviour)));
    }

    @Override
    public @NonNull DataMutateResult setTransientPermission(@NonNull Node node) {
        return DataMutateResultProxyUtil.legacy(this.permissionHolder.transientData().add(((NodeProxy) node).getUnderlyingNode()));
    }

    @Override
    public @NonNull TemporaryDataMutateResult setTransientPermission(@NonNull Node node, @NonNull TemporaryMergeBehaviour temporaryMergeBehaviour) {
        return new TemporaryDataMutateResultProxy(this.permissionHolder.transientData().add(((NodeProxy) node).getUnderlyingNode(), TemporaryNodeMergeStrategyProxyUtil.modern(temporaryMergeBehaviour)));
    }

    @Override
    public @NonNull DataMutateResult unsetPermission(@NonNull Node node) {
        return DataMutateResultProxyUtil.legacy(this.permissionHolder.data().remove(((NodeProxy) node).getUnderlyingNode()));
    }

    @Override
    public @NonNull DataMutateResult unsetTransientPermission(@NonNull Node node) {
        return DataMutateResultProxyUtil.legacy(this.permissionHolder.transientData().remove(((NodeProxy) node).getUnderlyingNode()));
    }

    @Override
    public void clearMatching(@NonNull Predicate<Node> test) {
        this.permissionHolder.data().clear(node -> test.test(new NodeProxy(node)));
    }

    @Override
    public void clearMatchingTransient(@NonNull Predicate<Node> test) {
        this.permissionHolder.transientData().clear(node -> test.test(new NodeProxy(node)));
    }

    @Override
    public void clearNodes() {
        this.permissionHolder.data().clear();
    }

    @Override
    public void clearNodes(@NonNull ContextSet contextSet) {
        this.permissionHolder.data().clear(ContextSetProxyUtil.modernImmutable(contextSet));
    }

    @Override
    public void clearParents() {
        this.permissionHolder.data().clear(NodeType.INHERITANCE::matches);
    }

    @Override
    public void clearParents(@NonNull ContextSet contextSet) {
        this.permissionHolder.data().clear(ContextSetProxyUtil.modernImmutable(contextSet), NodeType.INHERITANCE::matches);
    }

    @Override
    public void clearMeta() {
        this.permissionHolder.data().clear(NodeType.META_OR_CHAT_META::matches);
    }

    @Override
    public void clearMeta(@NonNull ContextSet contextSet) {
        this.permissionHolder.data().clear(ContextSetProxyUtil.modernImmutable(contextSet), NodeType.META_OR_CHAT_META::matches);
    }

    @Override
    public void clearTransientNodes() {
        this.permissionHolder.transientData().clear();
    }

    private static <T> List<T> collectionToList(Collection<T> collection) {
        if (collection instanceof List<?>) {
            return ((List<T>) collection);
        } else {
            return ImmutableList.copyOf(collection);
        }
    }
}
