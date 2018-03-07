package com.shadowcoder.courtneyscorner.notification;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {

    private static final int MAX_STORED_NOTIFICATIONS = 10;

    private static EventBus singleton = new EventBus();

    private Bus eventBus = new Bus();
    private List<Object> registeredItems = new ArrayList<>();
    private Map<String, Notification> notificationLookup = new HashMap<>();
    private List<String> notificationRegistry = new ArrayList<>();

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
        this.addNotification(notification);
        this.eventBus.post(notification);
    }

    @Nullable
    public Notification get(@NonNull String notificationId) {
        return this.notificationLookup.get(notificationId);
    }

    private void addNotification(@NonNull Notification notification) {
        this.notificationLookup.put(notification.getId(), notification);
        this.notificationRegistry.add(notification.getId());

        if (this.notificationRegistry.size() > MAX_STORED_NOTIFICATIONS) {
            String notificationId = this.notificationRegistry.get(0);

            this.notificationRegistry.remove(0);
            this.notificationLookup.remove(notificationId);
        }
    }
}
