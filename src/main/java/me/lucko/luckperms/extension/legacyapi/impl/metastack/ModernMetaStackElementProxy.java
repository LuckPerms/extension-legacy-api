package me.lucko.luckperms.extension.legacyapi.impl.metastack;

import com.google.common.collect.Maps;
import me.lucko.luckperms.extension.legacyapi.impl.node.ChatMetaTypeProxyUtil;
import me.lucko.luckperms.extension.legacyapi.impl.node.NodeProxy;
import net.luckperms.api.metastacking.MetaStackElement;
import net.luckperms.api.node.ChatMetaType;
import net.luckperms.api.node.types.ChatMetaNode;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ModernMetaStackElementProxy implements MetaStackElement {
    private final me.lucko.luckperms.api.metastacking.MetaStackElement element;

    public ModernMetaStackElementProxy(me.lucko.luckperms.api.metastacking.MetaStackElement element) {
        this.element = element;
    }

    @Override
    public boolean shouldAccumulate(@NonNull ChatMetaType type, @NonNull ChatMetaNode<?, ?> node, @Nullable ChatMetaNode<?, ?> current) {
        return this.element.shouldAccumulate(new NodeProxy(node), ChatMetaTypeProxyUtil.legacyType(type), Maps.immutableEntry(current.getPriority(), current.getMetaValue()));
    }

}
