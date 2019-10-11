package me.lucko.luckperms.extension.legacyapi.impl.misc;

import me.lucko.luckperms.api.UuidCache;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

@SuppressWarnings("deprecation")
public enum DummyUuidCache implements UuidCache {
    INSTANCE;

    @Override
    public @NonNull UUID getUUID(@NonNull UUID mojangUuid) {
        return mojangUuid;
    }

    @Override
    public @NonNull UUID getExternalUUID(@NonNull UUID internalUuid) {
        return internalUuid;
    }
}
