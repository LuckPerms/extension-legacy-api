package me.lucko.luckperms.extension.legacyapi.impl.track;

import me.lucko.luckperms.api.Track;
import me.lucko.luckperms.api.manager.TrackManager;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TrackManagerProxy implements TrackManager {
    private final net.luckperms.api.track.TrackManager trackManager;

    public TrackManagerProxy(net.luckperms.api.track.TrackManager trackManager) {
        this.trackManager = trackManager;
    }

    @Override
    public @NonNull CompletableFuture<Track> createAndLoadTrack(@NonNull String name) {
        return this.trackManager.createAndLoadTrack(name).thenApply(TrackProxy::new);
    }

    @Override
    public @NonNull CompletableFuture<Optional<Track>> loadTrack(@NonNull String name) {
        return this.trackManager.loadTrack(name).thenApply(opt -> opt.map(TrackProxy::new));
    }

    @Override
    public @NonNull CompletableFuture<Void> saveTrack(@NonNull Track track) {
        return this.trackManager.saveTrack(((TrackProxy) track).getUnderlyingTrack());
    }

    @Override
    public @NonNull CompletableFuture<Void> deleteTrack(@NonNull Track track) {
        return this.trackManager.deleteTrack(((TrackProxy) track).getUnderlyingTrack());
    }

    @Override
    public @NonNull CompletableFuture<Void> loadAllTracks() {
        return this.trackManager.loadAllTracks();
    }

    @Override
    public @Nullable Track getTrack(@NonNull String name) {
        net.luckperms.api.track.Track track = this.trackManager.getTrack(name);
        return track == null ? null : new TrackProxy(track);
    }

    @Override
    public @NonNull Set<Track> getLoadedTracks() {
        return this.trackManager.getLoadedTracks().stream().map(TrackProxy::new).collect(Collectors.toSet());
    }

    @Override
    public boolean isLoaded(@NonNull String name) {
        return this.trackManager.isLoaded(name);
    }
}
