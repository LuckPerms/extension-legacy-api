package me.lucko.luckperms.extension.legacyapi.impl.actionlogger;

import me.lucko.luckperms.api.LogEntry;
import net.luckperms.api.actionlog.Action;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Optional;
import java.util.UUID;

public class LogEntryProxy implements LogEntry {
    private final Action action;

    public LogEntryProxy(Action action) {
        this.action = action;
    }

    @Override
    public long getTimestamp() {
        return this.action.getTimestamp().getEpochSecond();
    }

    @Override
    public @NonNull UUID getActor() {
        return this.action.getSource().getUniqueId();
    }

    @Override
    public @NonNull String getActorName() {
        return this.action.getSource().getName();
    }

    @Override
    public @NonNull Type getType() {
        return LogEntryTypeProxyUtil.legacyType(this.action.getTarget().getType());
    }

    @Override
    public @NonNull Optional<UUID> getActed() {
        return this.action.getTarget().getUniqueId();
    }

    @Override
    public @NonNull String getActedName() {
        return this.action.getTarget().getName();
    }

    @Override
    public @NonNull String getAction() {
        return this.action.getDescription();
    }

    @Override
    public int compareTo(LogEntry o) {
        return this.action.compareTo(((LogEntryProxy) o).action);
    }
}
