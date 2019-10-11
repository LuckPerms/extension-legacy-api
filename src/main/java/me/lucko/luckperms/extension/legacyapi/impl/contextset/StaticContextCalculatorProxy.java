package me.lucko.luckperms.extension.legacyapi.impl.contextset;

import me.lucko.luckperms.api.context.MutableContextSet;
import net.luckperms.api.context.ContextConsumer;
import net.luckperms.api.context.StaticContextCalculator;
import org.checkerframework.checker.nullness.qual.NonNull;

public class StaticContextCalculatorProxy implements StaticContextCalculator {
    private final me.lucko.luckperms.api.context.StaticContextCalculator calculator;

    public StaticContextCalculatorProxy(me.lucko.luckperms.api.context.StaticContextCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public void calculate(@NonNull ContextConsumer contextConsumer) {
        MutableContextSet set = MutableContextSet.create();
        this.calculator.giveApplicableContext(set);
        contextConsumer.accept(ContextSetProxyUtil.modernImmutable(set));
    }
}
