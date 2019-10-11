package me.lucko.luckperms.extension.legacyapi.impl.actionlogger;

import me.lucko.luckperms.api.LogEntry;
import net.luckperms.api.actionlog.Action;

public enum LogEntryTypeProxyUtil {
    ;

    public static LogEntry.Type legacyType(Action.Target.Type type) {
        switch (type) {
            case USER:
                return LogEntry.Type.USER;
            case GROUP:
                return LogEntry.Type.GROUP;
            case TRACK:
                return LogEntry.Type.TRACK;
            default:
                throw new AssertionError();
        }
    }

    public static Action.Target.Type modernType(LogEntry.Type type) {
        switch (type) {
            case USER:
                return Action.Target.Type.USER;
            case GROUP:
                return Action.Target.Type.GROUP;
            case TRACK:
                return Action.Target.Type.TRACK;
            default:
                throw new AssertionError();
        }
    }
}
