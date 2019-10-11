package me.lucko.luckperms.extension.legacyapi.impl.contextset;

import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.LookupSetting;
import me.lucko.luckperms.api.caching.MetaContexts;
import me.lucko.luckperms.extension.legacyapi.impl.metastack.MetaStackDefinitionProxy;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.metastacking.MetaStackDefinition;
import net.luckperms.api.query.Flag;
import net.luckperms.api.query.OptionKey;
import net.luckperms.api.query.QueryMode;
import net.luckperms.api.query.QueryOptions;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public enum ContextsProxyUtil {
    ;

    public static final OptionKey<Boolean> OP_OPTION = OptionKey.of("op", Boolean.class);

    public static Contexts legacyContexts(QueryOptions queryOptions) {
        if (queryOptions.mode() == QueryMode.NON_CONTEXTUAL) {
            return Contexts.allowAll();
        }

        me.lucko.luckperms.api.context.ImmutableContextSet contextSet = ContextSetProxyUtil.legacyImmutable(queryOptions.context());
        Set<LookupSetting> settings = legacyFlags(queryOptions.flags());
        if (queryOptions.option(OP_OPTION).orElse(false)) {
            settings.add(LookupSetting.IS_OP);
        }
        return Contexts.of(contextSet, settings);
    }

    public static MetaContexts legacyMetaContexts(QueryOptions queryOptions) {
        Contexts contexts = legacyContexts(queryOptions);
        MetaStackDefinition prefixStack = queryOptions.option(MetaStackDefinition.PREFIX_STACK_KEY).orElseThrow(() -> new IllegalStateException("query options do not specify a metastack definition"));
        MetaStackDefinition suffixStack = queryOptions.option(MetaStackDefinition.SUFFIX_STACK_KEY).orElseThrow(() -> new IllegalStateException("query options do not specify a metastack definition"));
        return MetaContexts.of(contexts, new MetaStackDefinitionProxy(prefixStack), new MetaStackDefinitionProxy(suffixStack));
    }

    public static QueryOptions modernContexts(Contexts contexts) {
        if (contexts == Contexts.allowAll()) {
            return QueryOptions.nonContextual();
        }

        ImmutableContextSet contextSet = ContextSetProxyUtil.modernImmutable(contexts.getContexts());
        AtomicBoolean op = new AtomicBoolean(false);
        Set<Flag> flags = contexts.getSettings().stream()
                .map(ContextsProxyUtil::modernFlag)
                .peek(flag -> {
                    if (flag == null) { // special case for op flag
                        op.set(true);
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return QueryOptions.builder(QueryMode.CONTEXTUAL)
                .context(contextSet)
                .flags(flags)
                .option(OP_OPTION, op.get())
                .build();
    }

    public static QueryOptions modernContexts(MetaContexts contexts) {
        QueryOptions.Builder queryOptions = modernContexts(contexts.getContexts()).toBuilder();
        queryOptions.option(MetaStackDefinition.PREFIX_STACK_KEY, ((MetaStackDefinitionProxy) contexts.getPrefixStackDefinition()).getUnderlyingDefinition());
        queryOptions.option(MetaStackDefinition.SUFFIX_STACK_KEY, ((MetaStackDefinitionProxy) contexts.getSuffixStackDefinition()).getUnderlyingDefinition());
        return queryOptions.build();
    }

    private static LookupSetting legacyFlag(Flag flag) {
        switch (flag) {
            case RESOLVE_INHERITANCE:
                return LookupSetting.RESOLVE_INHERITANCE;
            case INCLUDE_NODES_WITHOUT_SERVER_CONTEXT:
                return LookupSetting.INCLUDE_NODES_SET_WITHOUT_SERVER;
            case INCLUDE_NODES_WITHOUT_WORLD_CONTEXT:
                return LookupSetting.INCLUDE_NODES_SET_WITHOUT_WORLD;
            case APPLY_INHERITANCE_NODES_WITHOUT_SERVER_CONTEXT:
                return LookupSetting.APPLY_PARENTS_SET_WITHOUT_SERVER;
            case APPLY_INHERITANCE_NODES_WITHOUT_WORLD_CONTEXT:
                return LookupSetting.APPLY_PARENTS_SET_WITHOUT_WORLD;
            default:
                throw new AssertionError();
        }
    }

    private static Set<LookupSetting> legacyFlags(Set<Flag> flags) {
        return flags.stream().map(ContextsProxyUtil::legacyFlag).collect(Collectors.toSet());
    }

    private static @Nullable Flag modernFlag(LookupSetting lookupSetting) {
        switch (lookupSetting) {
            case RESOLVE_INHERITANCE:
                return Flag.RESOLVE_INHERITANCE;
            case INCLUDE_NODES_SET_WITHOUT_SERVER:
                return Flag.INCLUDE_NODES_WITHOUT_SERVER_CONTEXT;
            case INCLUDE_NODES_SET_WITHOUT_WORLD:
                return Flag.INCLUDE_NODES_WITHOUT_WORLD_CONTEXT;
            case APPLY_PARENTS_SET_WITHOUT_SERVER:
                return Flag.APPLY_INHERITANCE_NODES_WITHOUT_SERVER_CONTEXT;
            case APPLY_PARENTS_SET_WITHOUT_WORLD:
                return Flag.APPLY_INHERITANCE_NODES_WITHOUT_WORLD_CONTEXT;
            case IS_OP:
                return null; // special case for op flag
            default:
                throw new AssertionError();
        }
    }
}
