package me.lucko.luckperms.extension.legacyapi.impl.metastack;

import com.google.common.collect.Lists;

import me.lucko.luckperms.api.metastacking.DuplicateRemovalFunction;
import me.lucko.luckperms.api.metastacking.MetaStackDefinition;
import me.lucko.luckperms.api.metastacking.MetaStackElement;
import me.lucko.luckperms.api.metastacking.MetaStackFactory;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Optional;

public class MetaStackFactoryProxy implements MetaStackFactory {
    private final net.luckperms.api.metastacking.MetaStackFactory factory;

    public MetaStackFactoryProxy(net.luckperms.api.metastacking.MetaStackFactory factory) {
        this.factory = factory;
    }

    @Override
    public @NonNull Optional<MetaStackElement> fromString(@NonNull String definition) {
        return this.factory.fromString(definition).map(MetaStackElementProxyUtil::legacyElement);
    }

    @Override
    public @NonNull List<MetaStackElement> fromStrings(@NonNull List<String> definitions) {
        return Lists.transform(this.factory.fromStrings(definitions), MetaStackElementProxyUtil::legacyElement);
    }

    @Override
    public @NonNull MetaStackDefinition createDefinition(@NonNull List<MetaStackElement> elements, @NonNull DuplicateRemovalFunction duplicateRemovalFunction, @NonNull String startSpacer, @NonNull String middleSpacer, @NonNull String endSpacer) {
        return new MetaStackDefinitionProxy(this.factory.createDefinition(
                Lists.transform(elements, MetaStackElementProxyUtil::modernElement),
                DuplicateRemovalFunctionProxyUtil.modern(duplicateRemovalFunction),
                startSpacer, middleSpacer, endSpacer
        ));
    }

}
