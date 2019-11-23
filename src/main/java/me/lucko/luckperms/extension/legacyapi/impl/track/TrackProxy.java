package me.lucko.luckperms.extension.legacyapi.impl.track;

import me.lucko.luckperms.api.DataMutateResult;
import me.lucko.luckperms.api.DemotionResult;
import me.lucko.luckperms.api.Group;
import me.lucko.luckperms.api.PromotionResult;
import me.lucko.luckperms.api.Track;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.context.ContextSet;
import me.lucko.luckperms.extension.legacyapi.impl.contextset.ContextSetProxyUtil;
import me.lucko.luckperms.extension.legacyapi.impl.misc.DataMutateResultProxyUtil;
import me.lucko.luckperms.extension.legacyapi.impl.permissionholders.GroupProxy;
import me.lucko.luckperms.extension.legacyapi.impl.permissionholders.UserProxy;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

public class TrackProxy implements Track {
    private final net.luckperms.api.track.Track track;

    public TrackProxy(net.luckperms.api.track.Track track) {
        this.track = track;
    }

    public net.luckperms.api.track.Track getUnderlyingTrack() {
        return this.track;
    }

    @Override
    public @NonNull String getName() {
        return this.track.getName();
    }

    @Override
    public @NonNull List<String> getGroups() {
        return this.track.getGroups();
    }

    @Override
    public int getSize() {
        return this.track.getGroups().size();
    }

    @Override
    public @Nullable String getNext(@NonNull Group current) {
        return this.track.getNext(((GroupProxy) current).getUnderlyingGroup());
    }

    @Override
    public @Nullable String getPrevious(@NonNull Group current) {
        return this.track.getPrevious(((GroupProxy) current).getUnderlyingGroup());
    }

    @Override
    public @NonNull PromotionResult promote(@NonNull User user, @NonNull ContextSet contextSet) {
        return new PromotionResultProxy(this.track.promote(((UserProxy) user).getUnderlyingUser(), ContextSetProxyUtil.modernImmutable(contextSet)));
    }

    @Override
    public @NonNull DemotionResult demote(@NonNull User user, @NonNull ContextSet contextSet) {
        return new DemotionResultProxy(this.track.demote(((UserProxy) user).getUnderlyingUser(), ContextSetProxyUtil.modernImmutable(contextSet)));
    }

    @Override
    public @NonNull DataMutateResult appendGroup(@NonNull Group group) {
        return DataMutateResultProxyUtil.legacy(this.track.appendGroup(((GroupProxy) group).getUnderlyingGroup()));
    }

    @Override
    public @NonNull DataMutateResult insertGroup(@NonNull Group group, int position) throws IndexOutOfBoundsException {
        return DataMutateResultProxyUtil.legacy(this.track.insertGroup(((GroupProxy) group).getUnderlyingGroup(), position));
    }

    @Override
    public @NonNull DataMutateResult removeGroup(@NonNull Group group) {
        return DataMutateResultProxyUtil.legacy(this.track.removeGroup(((GroupProxy) group).getUnderlyingGroup()));
    }

    @Override
    public @NonNull DataMutateResult removeGroup(@NonNull String group) {
        return DataMutateResultProxyUtil.legacy(this.track.removeGroup(group));
    }

    @Override
    public boolean containsGroup(@NonNull Group group) {
        return this.track.containsGroup(((GroupProxy) group).getUnderlyingGroup());
    }

    @Override
    public boolean containsGroup(@NonNull String group) {
        return this.track.containsGroup(group);
    }

    @Override
    public void clearGroups() {
        this.track.clearGroups();
    }
}
