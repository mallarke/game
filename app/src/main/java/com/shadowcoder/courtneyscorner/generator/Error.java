package com.shadowcoder.courtneyscorner.generator;

import android.support.annotation.NonNull;

public class Error extends Exception {

    public static Error getUnknownError(@NonNull Exception exception) {
        return new Error();
    }
}
