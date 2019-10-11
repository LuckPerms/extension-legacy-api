package me.lucko.luckperms.extension.legacyapi.impl.actionlogger;

import me.lucko.luckperms.api.ActionLogger;
import me.lucko.luckperms.api.Log;
import me.lucko.luckperms.api.LogEntry;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.CompletableFuture;

public class ActionLoggerProxy implements ActionLogger {
    private final net.luckperms.api.actionlog.ActionLogger actionLogger;

    public ActionLoggerProxy(net.luckperms.api.actionlog.ActionLogger actionLogger) {
        this.actionLogger = actionLogger;
    }

    @Override
    public LogEntry.@NonNull Builder newEntryBuilder() {
        return new LogEntryBuilderProxy();
    }

    @Override
    public @NonNull CompletableFuture<Log> getLog() {
        return this.actionLogger.getLog().thenApply(LogProxy::new);
    }

    @Override
    public @NonNull CompletableFuture<Void> submit(@NonNull LogEntry entry) {
        return this.actionLogger.submit(new LogEntryBuilderProxy().copyFrom(entry).buildModern());
    }

    @Override
    public @NonNull CompletableFuture<Void> submitToStorage(@NonNull LogEntry entry) {
        return this.actionLogger.submitToStorage(new LogEntryBuilderProxy().copyFrom(entry).buildModern());
    }

    @Override
    public @NonNull CompletableFuture<Void> broadcastAction(@NonNull LogEntry entry) {
        return this.actionLogger.broadcastAction(new LogEntryBuilderProxy().copyFrom(entry).buildModern());
    }
}
