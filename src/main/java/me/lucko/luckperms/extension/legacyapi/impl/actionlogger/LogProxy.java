package me.lucko.luckperms.extension.legacyapi.impl.actionlogger;

import me.lucko.luckperms.api.Log;
import me.lucko.luckperms.api.LogEntry;
import me.lucko.luckperms.extension.legacyapi.impl.misc.FakeSortedSet;
import net.luckperms.api.actionlog.ActionLog;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.SortedSet;
import java.util.UUID;
import java.util.stream.Collectors;

public class LogProxy implements Log {
    private final ActionLog actionLog;

    public LogProxy(ActionLog actionLog) {
        this.actionLog = actionLog;
    }

    @Override
    public @NonNull SortedSet<LogEntry> getContent() {
        return this.actionLog.getContent().stream().map(LogEntryProxy::new).collect(Collectors.toCollection(FakeSortedSet::backedByLinkedHashSet));
    }

    @Override
    public @NonNull SortedSet<LogEntry> getContent(@NonNull UUID actor) {
        return this.actionLog.getContent(actor).stream().map(LogEntryProxy::new).collect(Collectors.toCollection(FakeSortedSet::backedByLinkedHashSet));
    }

    @Override
    public @NonNull SortedSet<LogEntry> getUserHistory(@NonNull UUID uuid) {
        return this.actionLog.getUserHistory(uuid).stream().map(LogEntryProxy::new).collect(Collectors.toCollection(FakeSortedSet::backedByLinkedHashSet));
    }

    @Override
    public @NonNull SortedSet<LogEntry> getGroupHistory(@NonNull String name) {
        return this.actionLog.getGroupHistory(name).stream().map(LogEntryProxy::new).collect(Collectors.toCollection(FakeSortedSet::backedByLinkedHashSet));
    }

    @Override
    public @NonNull SortedSet<LogEntry> getTrackHistory(@NonNull String name) {
        return this.actionLog.getTrackHistory(name).stream().map(LogEntryProxy::new).collect(Collectors.toCollection(FakeSortedSet::backedByLinkedHashSet));
    }
}
