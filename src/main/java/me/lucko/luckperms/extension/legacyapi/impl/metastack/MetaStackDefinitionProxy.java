package me.lucko.luckperms.extension.legacyapi.impl.metastack;

import com.google.common.collect.Lists;

import me.lucko.luckperms.api.metastacking.DuplicateRemovalFunction;
import me.lucko.luckperms.api.metastacking.MetaStackDefinition;
import me.lucko.luckperms.api.metastacking.MetaStackElement;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class MetaStackDefinitionProxy implements MetaStackDefinition {
    private final net.luckperms.api.metastacking.MetaStackDefinition definition;

    public MetaStackDefinitionProxy(net.luckperms.api.metastacking.MetaStackDefinition definition) {
        this.definition = definition;
    }

    public net.luckperms.api.metastacking.MetaStackDefinition getUnderlyingDefinition() {
        return this.definition;
    }

    @Override
    public @NonNull List<MetaStackElement> getElements() {
        return Lists.transform(this.definition.getElements(), MetaStackElementProxyUtil::legacyElement);
    }

    @Override
    public @NonNull DuplicateRemovalFunction getDuplicateRemovalFunction() {
        return DuplicateRemovalFunctionProxyUtil.legacy(this.definition.getDuplicateRemovalFunction());
    }

    @Override
    public @NonNull String getStartSpacer() {
        return this.definition.getStartSpacer();
    }

    @Override
    public @NonNull String getMiddleSpacer() {
        return this.definition.getMiddleSpacer();
    }

    @Override
    public @NonNull String getEndSpacer() {
        return this.definition.getEndSpacer();
    }
}
