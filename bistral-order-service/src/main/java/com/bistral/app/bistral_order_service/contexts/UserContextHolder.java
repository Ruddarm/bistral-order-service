package com.bistral.app.bistral_order_service.contexts;

public class UserContextHolder {

    private static final ThreadLocal<AuthContext> currentUserIdContext = new ThreadLocal<>();

    public static AuthContext getAuthContext() {
        return currentUserIdContext.get();
    }

    public static void setAuthContext(AuthContext authContext) {
        currentUserIdContext.set(authContext);
    }

    public static void clear() {
        currentUserIdContext.remove();
    }
}
