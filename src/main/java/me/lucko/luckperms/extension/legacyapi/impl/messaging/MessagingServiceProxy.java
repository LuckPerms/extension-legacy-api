package me.lucko.luckperms.extension.legacyapi.impl.messaging;

import me.lucko.luckperms.api.MessagingService;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.extension.legacyapi.impl.permissionholders.UserProxy;
import org.checkerframework.checker.nullness.qual.NonNull;

public class MessagingServiceProxy implements MessagingService {
    private final net.luckperms.api.messaging.MessagingService messagingService;

    public MessagingServiceProxy(net.luckperms.api.messaging.MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @Override
    public String getName() {
        return this.messagingService.getName();
    }

    @Override
    public void pushUpdate() {
        this.messagingService.pushUpdate();
    }

    @Override
    public void pushUserUpdate(@NonNull User user) {
        this.messagingService.pushUserUpdate(((UserProxy) user).getUnderlyingUser());
    }
}
