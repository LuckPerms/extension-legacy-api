package me.lucko.luckperms.extension.legacyapi.impl.contextset;

import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.context.MutableContextSet;

public enum ContextSetProxyUtil {
    ;

    public static ImmutableContextSet modernImmutable(me.lucko.luckperms.api.context.ContextSet contextSet) {
        ImmutableContextSet.Builder builder = ImmutableContextSet.builder();
        contextSet.forEach(c -> builder.add(c.getKey(), c.getValue()));
        return builder.build();
    }

    public static MutableContextSet modernMutable(me.lucko.luckperms.api.context.ContextSet contextSet) {
        MutableContextSet set = MutableContextSet.create();
        contextSet.forEach(c -> set.add(c.getKey(), c.getValue()));
        return set;
    }

    public static me.lucko.luckperms.api.context.ImmutableContextSet legacyImmutable(ContextSet contextSet) {
        return me.lucko.luckperms.api.context.ImmutableContextSet.fromEntries(Iterables.transform(contextSet, c -> Maps.immutableEntry(c.getKey(), c.getValue())));
    }

    public static me.lucko.luckperms.api.context.MutableContextSet legacyMutable(ContextSet contextSet) {
        return me.lucko.luckperms.api.context.MutableContextSet.fromEntries(Iterables.transform(contextSet, c -> Maps.immutableEntry(c.getKey(), c.getValue())));
    }

}
