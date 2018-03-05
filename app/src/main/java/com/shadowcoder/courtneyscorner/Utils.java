package com.shadowcoder.courtneyscorner;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

public class Utils {

    public int convertDp(int dp, @NonNull Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return this.convertDp(dp, metrics);
    }

    public int convertDp(int dp, @NonNull DisplayMetrics metrics) {
        return Math.round(dp * (metrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
