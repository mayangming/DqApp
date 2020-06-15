package com.wd.daquan.model.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.wd.daquan.model.ModelConfig;

public class BaseSharedPreference {
    private String fileName;


    public BaseSharedPreference(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Retrieve the package shared preferences object.
     *
     * @return
     */
    private SharedPreferences getSharedPreferences() {
        try {
            return ModelConfig.getContext().getSharedPreferences(fileName, Context.MODE_WORLD_READABLE);
        }catch (Exception e){
            return ModelConfig.getContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
    }

    public void saveBoolean(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defvalue) {
        return getSharedPreferences().getBoolean(key, defvalue);
    }

    public void saveAtState(String key, boolean value) {
        getSharedPreferences().edit().putBoolean(key, value).apply();
    }

    public boolean getAtState(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }

    /**
     * Save a string value to the shared preference.
     *
     * @param key   to mark the store value.
     * @param value to saved value.
     */
    public void saveString(String key, String value) {
        getSharedPreferences().edit().putString(key, value).commit();
    }

    /**
     * Get the specified value through the KEY value.
     *
     * @param key to retrieve the value.
     * @return the string value returned.
     */
    public String getString(String key, String def) {
        return getSharedPreferences().getString(key, def);
    }

    /**
     * Save a integer value to the shared preference.
     *
     * @param key   to mark the store value.
     * @param value to saved value.
     */
    public void saveInt(String key, int value) {
        getSharedPreferences().edit().putInt(key, value).commit();

    }
    public void clear() {
        getSharedPreferences().edit().clear().commit();
    }
    /**
     * Get the specified value through the KEY value.
     *
     * @param key to retrieve the value.
     * @return the integer value returned.
     */
    public int getInt(String key, int def) {
        return getSharedPreferences().getInt(key, def);
    }

    /**
     * Save a Long value to the shared preference.
     *
     * @param key   to mark the store value.
     * @param value to saved value.
     */
    public void saveLong(String key, long value) {
        getSharedPreferences().edit().putLong(key, value).commit();
    }

    /**
     * Get the specified value through the KEY value.
     *
     * @param key to retrieve the value.
     * @return the integer value returned.
     */
    public long getLong(String key, long def) {
        return getSharedPreferences().getLong(key, def);
    }
}
