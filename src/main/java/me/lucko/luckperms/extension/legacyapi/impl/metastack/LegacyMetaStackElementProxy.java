package me.lucko.luckperms.extension.legacyapi.impl.metastack;

import me.lucko.luckperms.api.ChatMetaType;
import me.lucko.luckperms.api.LocalizedNode;
import me.lucko.luckperms.api.metastacking.MetaStackElement;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

public class LegacyMetaStackElementProxy implements MetaStackElement {
    private final net.luckperms.api.metastacking.MetaStackElement element;

    public LegacyMetaStackElementProxy(net.luckperms.api.metastacking.MetaStackElement element) {
        this.element = element;
    }

    public net.luckperms.api.metastacking.MetaStackElement getUnderlyingElement() {
        return this.element;
    }

    @Override
    public boolean shouldAccumulate(@NonNull LocalizedNode node, @NonNull ChatMetaType type, Map.@Nullable Entry<Integer, String> current) {
        throw new UnsupportedOperationException();
    }
}
