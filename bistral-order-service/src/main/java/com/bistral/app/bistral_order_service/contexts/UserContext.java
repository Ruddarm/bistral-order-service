package com.bistral.app.bistral_order_service.contexts;


import java.util.UUID;

public class UserContext {
    private static ThreadLocal<UUID> userContext;

    public static void setUserContext(UUID userId) {
        userContext.set(userId);
    }

    public static UUID getUserContext() {
        return userContext.get();
    }

    public static void clearContext() {
        userContext.remove();
    }
}
