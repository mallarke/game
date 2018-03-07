package com.shadowcoder.courtneyscorner.notification;

import java.util.UUID;

public abstract class Notification {

    private final String id = UUID.randomUUID().toString();

    public String getId() {
        return this.id;
    }
}
