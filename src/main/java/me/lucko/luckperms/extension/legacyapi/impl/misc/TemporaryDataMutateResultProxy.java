package me.lucko.luckperms.extension.legacyapi.impl.misc;

import me.lucko.luckperms.api.DataMutateResult;
import me.lucko.luckperms.api.Node;
import me.lucko.luckperms.api.TemporaryDataMutateResult;
import me.lucko.luckperms.extension.legacyapi.impl.node.NodeProxy;
import org.checkerframework.checker.nullness.qual.NonNull;

public class TemporaryDataMutateResultProxy implements TemporaryDataMutateResult {
    private final net.luckperms.api.model.data.DataMutateResult.WithMergedNode result;

    public TemporaryDataMutateResultProxy(net.luckperms.api.model.data.DataMutateResult.WithMergedNode result) {
        this.result = result;
    }

    @Override
    public @NonNull DataMutateResult getResult() {
        return DataMutateResultProxyUtil.legacy(this.result.getResult());
    }

    @Override
    public @NonNull Node getMergedNode() {
        return new NodeProxy(this.result.getMergedNode());
    }
}
