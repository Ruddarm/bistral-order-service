package com.bistral.app.bistral_order_service.enums;

public enum ItemUnit {
    PIECE("piece"),
    PLATE("plate"),
    ML("ml"),
    LITRE("litre"),
    G("g"),
    KG("kg");

    private final String displayName;

    ItemUnit(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
