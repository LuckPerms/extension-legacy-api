package me.lucko.luckperms.extension.legacyapi.impl.actionlogger;

import me.lucko.luckperms.api.LogEntry;
import net.luckperms.api.actionlog.Action;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.Instant;
import java.util.UUID;

public class LogEntryBuilderProxy implements LogEntry.Builder {
    private final Action.Builder builder = Action.builder();

    public LogEntryBuilderProxy copyFrom(LogEntry logEntry) {
        setTimestamp(logEntry.getTimestamp());
        setActor(logEntry.getActor());
        setActorName(logEntry.getActorName());
        setType(logEntry.getType());
        setActed(logEntry.getActed().orElse(null));
        setActedName(logEntry.getActedName());
        setAction(logEntry.getAction());
        return this;
    }

    @Override
    public LogEntry.@NonNull Builder setTimestamp(long timestamp) {
        this.builder.timestamp(Instant.ofEpochSecond(timestamp));
        return this;
    }

    @Override
    public LogEntry.@NonNull Builder setActor(@NonNull UUID actor) {
        this.builder.source(actor);
        return this;
    }

    @Override
    public LogEntry.@NonNull Builder setActorName(@NonNull String actorName) {
        this.builder.sourceName(actorName);
        return this;
    }

    @Override
    public LogEntry.@NonNull Builder setType(LogEntry.@NonNull Type type) {
        this.builder.targetType(LogEntryTypeProxyUtil.modernType(type));
        return this;
    }

    @Override
    public LogEntry.@NonNull Builder setActed(@Nullable UUID acted) {
        this.builder.target(acted);
        return this;
    }

    @Override
    public LogEntry.@NonNull Builder setActedName(@NonNull String actedName) {
        this.builder.targetName(actedName);
        return this;
    }

    @Override
    public LogEntry.@NonNull Builder setAction(@NonNull String action) {
        this.builder.description(action);
        return this;
    }

    public @NonNull Action buildModern() {
        return this.builder.build();
    }

    @Override
    public @NonNull LogEntry build() {
        return new LogEntryProxy(buildModern());
    }
}
