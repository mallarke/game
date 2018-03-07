package com.shadowcoder.courtneyscorner.generator;

import android.support.annotation.NonNull;

import com.shadowcoder.courtneyscorner.notification.EventBus;

public abstract class Generator<DATA> {

    public abstract DATA execute() throws Error;

    public void onPost(@NonNull  Result<DATA> result) {
        EventBus.getSingleton().post(result);
    }
}
