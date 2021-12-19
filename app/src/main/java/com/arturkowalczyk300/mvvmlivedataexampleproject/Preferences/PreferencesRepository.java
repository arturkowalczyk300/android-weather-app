package com.arturkowalczyk300.mvvmlivedataexampleproject.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesRepository {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public PreferencesRepository(Context context) {
        sharedPreferences = context.getSharedPreferences(MainPreferencesConstants.PREFERENCES_ID, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public String getApiKey() {
        return sharedPreferences.getString(MainPreferencesConstants.API_KEY, "to_be_written");
    }

    public void setApiKey(String apiKey) {
        editor.putString(MainPreferencesConstants.API_KEY, apiKey);
        editor.apply();
    }

    public String getCity() {
        return sharedPreferences.getString(MainPreferencesConstants.CITY, "Wolow");
    }

    public void setCity(String city) {
        editor.putString(MainPreferencesConstants.CITY, city);
        editor.apply();
    }

    public String getUnits() {
        return sharedPreferences.getString(MainPreferencesConstants.UNITS, "metric");
    }

    public void setUnits(String units) {
        editor.putString(MainPreferencesConstants.UNITS, units);
        editor.apply();
    }

    public int getMaxCount() {
        return sharedPreferences.getInt(MainPreferencesConstants.MAX_COUNT, 10);
    }

    public void setMaxCount(int maxCount) {
        editor.putInt(MainPreferencesConstants.MAX_COUNT, maxCount);
        editor.apply();
    }

}