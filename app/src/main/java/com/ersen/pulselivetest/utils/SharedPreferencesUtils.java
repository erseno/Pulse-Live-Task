package com.ersen.pulselivetest.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ersen.pulselivetest.application.PulseLiveApplication;

public class SharedPreferencesUtils {

    /**
     * Write to shared preferences
     */
    public static void store(@NonNull String key, @NonNull String value) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PulseLiveApplication.getInstance().getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * Read from shared preferences
     */
    @Nullable
    public static String getString(@NonNull String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PulseLiveApplication.getInstance().getApplicationContext());
        return sharedPreferences.getString(key, null);
    }

    /**
     * Remove from shared preferences
     */
    public static void remove(@NonNull String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PulseLiveApplication.getInstance().getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * Checks whether the default preferences contains a preference. Returns true if the preference exists in the preferences, otherwise false.
     */
    public static boolean check(@NonNull String key){
        return PreferenceManager.getDefaultSharedPreferences(PulseLiveApplication.getInstance().getApplicationContext()).contains(key);
    }



}
