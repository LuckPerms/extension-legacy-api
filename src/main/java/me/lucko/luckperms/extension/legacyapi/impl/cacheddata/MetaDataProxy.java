package me.lucko.luckperms.extension.legacyapi.impl.cacheddata;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.caching.MetaContexts;
import me.lucko.luckperms.api.caching.MetaData;
import me.lucko.luckperms.api.metastacking.MetaStackDefinition;
import me.lucko.luckperms.extension.legacyapi.impl.contextset.ContextsProxyUtil;
import me.lucko.luckperms.extension.legacyapi.impl.metastack.MetaStackDefinitionProxy;
import net.luckperms.api.cacheddata.CachedMetaData;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class MetaDataProxy implements MetaData {
    private final CachedMetaData data;

    public MetaDataProxy(CachedMetaData data) {
        this.data = data;
    }

    @Override
    public @NonNull Contexts getContexts() {
        return ContextsProxyUtil.legacyContexts(this.data.getQueryOptions());
    }

    @Override
    public @NonNull MetaContexts getMetaContexts() {
        return ContextsProxyUtil.legacyMetaContexts(this.data.getQueryOptions());
    }

    @Override
    public @NonNull ListMultimap<String, String> getMetaMultimap() {
        ArrayListMultimap<String, String> multimap = ArrayListMultimap.create();
        for (Map.Entry<String, List<String>> e : this.data.getMeta().entrySet()) {
            multimap.putAll(e.getKey(), e.getValue());
        }
        return multimap;
    }

    @Override
    public @NonNull Map<String, String> getMeta() {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, List<String>> e : this.data.getMeta().entrySet()) {
            map.put(e.getKey(), e.getValue().get(0));
        }
        return map;
    }

    @Override
    public @NonNull SortedMap<Integer, String> getPrefixes() {
        return this.data.getPrefixes();
    }

    @Override
    public @NonNull SortedMap<Integer, String> getSuffixes() {
        return this.data.getSuffixes();
    }

    @Override
    public @Nullable String getPrefix() {
        return this.data.getPrefix();
    }

    @Override
    public @Nullable String getSuffix() {
        return this.data.getSuffix();
    }

    @Override
    public @NonNull MetaStackDefinition getPrefixStackDefinition() {
        return new MetaStackDefinitionProxy(this.data.getPrefixStackDefinition());
    }

    @Override
    public @NonNull MetaStackDefinition getSuffixStackDefinition() {
        return new MetaStackDefinitionProxy(this.data.getSuffixStackDefinition());
    }
}
