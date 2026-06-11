package com.core.identity.enums;

public enum VerificationLimit {
    MAX_SEND_COUNT(3),
    WAIT_HOURS(1),
    EXPIRY_SECONDS(90),
    TIME_BUFFER_SECONDS(2);

    private final int value;

   private VerificationLimit(int value){
        this.value=value;
    }

    public int getValue(){
        return value;
    }

    public static int maxSendCount(){return MAX_SEND_COUNT.getValue();}
    public static int waitHours(){return WAIT_HOURS.getValue();}
    public static int expirySeconds(){return EXPIRY_SECONDS.getValue();}
    public static int timeBufferSeconds(){return TIME_BUFFER_SECONDS.getValue();}
}
