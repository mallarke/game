package com.shadowcoder.courtneyscorner.notification;

import android.support.annotation.NonNull;

import com.squareup.otto.Bus;

import java.util.ArrayList;

public class EventBus {

    private static EventBus singleton = new EventBus();

    private Bus eventBus = new Bus();
    private ArrayList<Object> registeredItems = new ArrayList<>();

    public static EventBus getSingleton() {
        return singleton;
    }

    private EventBus() {

    }

    public void register(@NonNull Object object) {
        if (this.registeredItems.contains(object)) {
            return;
        }

        this.registeredItems.add(object);
        this.eventBus.register(object);
    }

    public void unregister(@NonNull Object object) {
        this.registeredItems.remove(object);
        this.eventBus.unregister(object);
    }

    public void post(@NonNull Notification notification) {
        this.eventBus.post(notification);
    }
}
