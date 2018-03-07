package com.shadowcoder.courtneyscorner.generator;

import com.shadowcoder.courtneyscorner.notification.Notification;

public class Result<DATA> extends Notification {

    public final boolean successful;
    public final DATA result;
    public final Error error;

    public Result(DATA result) {
        this.successful = true;
        this.result = result;
        this.error = null;
    }

    public Result(Error error) {
        this.successful = false;
        this.result = null;
        this.error = error;
    }
}
