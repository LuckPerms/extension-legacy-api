package me.lucko.luckperms.extension.legacyapi.impl.contextset;

import me.lucko.luckperms.api.Contexts;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.context.ContextCalculator;
import me.lucko.luckperms.api.context.ContextManager;
import me.lucko.luckperms.api.context.ImmutableContextSet;
import me.lucko.luckperms.api.context.StaticContextCalculator;
import me.lucko.luckperms.extension.legacyapi.impl.permissionholders.UserProxy;
import net.luckperms.api.query.QueryOptions;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

public class ContextManagerProxy implements ContextManager {
    private final net.luckperms.api.context.ContextManager contextManager;
    private final Map<ContextCalculator<?>, ContextCalculatorProxy<?>> registeredCalculators = new IdentityHashMap<>();
    private final Map<StaticContextCalculator, StaticContextCalculatorProxy> registeredStaticCalculators = new IdentityHashMap<>();

    public ContextManagerProxy(net.luckperms.api.context.ContextManager contextManager) {
        this.contextManager = contextManager;
    }

    @Override
    public @NonNull ImmutableContextSet getApplicableContext(@NonNull Object subject) {
        return ContextSetProxyUtil.legacyImmutable(this.contextManager.getContext(subject));
    }

    @Override
    public @NonNull Contexts getApplicableContexts(@NonNull Object subject) {
        return ContextsProxyUtil.legacyContexts(this.contextManager.getQueryOptions(subject));
    }

    @Override
    public @NonNull Optional<ImmutableContextSet> lookupApplicableContext(@NonNull User user) {
        return this.contextManager.getContext(((UserProxy) user).getUnderlyingUser()).map(ContextSetProxyUtil::legacyImmutable);
    }

    @Override
    public @NonNull Optional<Contexts> lookupApplicableContexts(@NonNull User user) {
        return this.contextManager.getQueryOptions(((UserProxy) user).getUnderlyingUser()).map(ContextsProxyUtil::legacyContexts);
    }

    @Override
    public @NonNull ImmutableContextSet getStaticContext() {
        return ContextSetProxyUtil.legacyImmutable(this.contextManager.getStaticContext());
    }

    @Override
    public @NonNull Contexts getStaticContexts() {
        return ContextsProxyUtil.legacyContexts(this.contextManager.getStaticQueryOptions());
    }

    @Override
    public @NonNull Contexts formContexts(@NonNull Object subject, @NonNull ImmutableContextSet contextSet) {
        QueryOptions options = this.contextManager.getQueryOptions(subject).toBuilder().context(ContextSetProxyUtil.modernImmutable(contextSet)).build();
        return ContextsProxyUtil.legacyContexts(options);
    }

    @Override
    public @NonNull Contexts formContexts(@NonNull ImmutableContextSet contextSet) {
        QueryOptions options = this.contextManager.getStaticQueryOptions().toBuilder().context(ContextSetProxyUtil.modernImmutable(contextSet)).build();
        return ContextsProxyUtil.legacyContexts(options);
    }

    @Override
    public void registerCalculator(@NonNull ContextCalculator<?> calculator) {
        if (calculator instanceof StaticContextCalculator) {
            StaticContextCalculator staticCalculator = (StaticContextCalculator) calculator;

            StaticContextCalculatorProxy contextCalculatorProxy = new StaticContextCalculatorProxy(staticCalculator);
            this.registeredStaticCalculators.put(staticCalculator, contextCalculatorProxy);
            this.contextManager.registerCalculator(contextCalculatorProxy);
        } else {
            ContextCalculatorProxy<?> contextCalculatorProxy = new ContextCalculatorProxy<>(calculator);
            this.registeredCalculators.put(calculator, contextCalculatorProxy);
            this.contextManager.registerCalculator(contextCalculatorProxy);
        }
    }

    @Override
    public void unregisterCalculator(@NonNull ContextCalculator<?> calculator) {
        if (calculator instanceof StaticContextCalculator) {
            StaticContextCalculatorProxy proxy = this.registeredStaticCalculators.remove(calculator);
            if (proxy != null) {
                this.contextManager.unregisterCalculator(proxy);
            }
        } else {
            ContextCalculatorProxy<?> proxy = this.registeredCalculators.remove(calculator);
            if (proxy != null) {
                this.contextManager.unregisterCalculator(proxy);
            }
        }
    }

    @Override
    public void invalidateCache(@NonNull Object subject) {

    }
}
