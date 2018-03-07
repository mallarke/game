package com.shadowcoder.courtneyscorner;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shadowcoder.courtneyscorner.generator.crossword.BoardConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    public int convertDp(int dp, @NonNull Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return this.convertDp(dp, metrics);
    }

    @SuppressWarnings("WeakerAccess")
    public int convertDp(int dp, @NonNull DisplayMetrics metrics) {
        return Math.round(dp * (metrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int[] convert(List<Integer> list) {
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        return array;
    }

    public static List<BoardConfiguration> readBoardConfigs() {
        BufferedReader reader = null;
        List<BoardConfiguration> configs = null;

        try {
            AssetManager manager = MyApplication.getSingleton().getAssets();
            reader = new BufferedReader(new InputStreamReader(manager.open("crosswordConfigs.json")));

            String line;
            StringBuilder contents = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                contents.append(line);
            }

            Gson gson = new Gson();
            Type type = new TypeToken<List<BoardConfiguration>>() {}.getType();

            configs = gson.fromJson(contents.toString(), type);
        }
        catch (IOException e) {
            Log.i(TAG, "error found while reading readBoardConfigs");
        }
        finally {
            if (reader != null) {
                try {
                    reader.close();
                }
                catch (IOException e) {
                    Log.i(TAG, "unable to close the reader in readBoardConfigs");
                }
            }
        }

        if (configs == null || configs.isEmpty()) {
            throw new IllegalStateException("unable to read the configs from crosswordConfigs");
        }

        return configs;
    }
}
