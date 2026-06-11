package com.core.identity.enums;

public enum SecurityLimit {
    MAX_REFRESH_TOKEN_DEVICE_COUNT(5);

    private final int value;

    private SecurityLimit(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static int maxRefreshTokenDeviceCount() {
        return MAX_REFRESH_TOKEN_DEVICE_COUNT.getValue();
    }
}