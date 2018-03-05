package com.shadowcoder.courtneyscorner.notification;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class TextEntryNotification implements Notification {

    public final @NonNull String uuid;
    public final @Nullable String value;

    public TextEntryNotification(@NonNull String uuid, @Nullable String value) {
        this.uuid = uuid;
        this.value = value;
    }
}
