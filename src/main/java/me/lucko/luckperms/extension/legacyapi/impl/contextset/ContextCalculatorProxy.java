package me.lucko.luckperms.extension.legacyapi.impl.contextset;

import me.lucko.luckperms.api.context.MutableContextSet;
import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ContextCalculatorProxy<T> implements ContextCalculator<T> {
    private final me.lucko.luckperms.api.context.ContextCalculator<T> calculator;

    public ContextCalculatorProxy(me.lucko.luckperms.api.context.ContextCalculator<T> calculator) {
        this.calculator = calculator;
    }

    @Override
    public void calculate(@NonNull T subject, @NonNull ContextConsumer contextConsumer) {
        MutableContextSet set = MutableContextSet.create();
        this.calculator.giveApplicableContext(subject, set);
        contextConsumer.accept(ContextSetProxyUtil.modernImmutable(set));
    }
}
