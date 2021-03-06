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
        return sharedPreferences.getString(MainPreferencesConstants.API_KEY, MainPreferencesConstants.DEFAULT_API_KEY);
    }

    public void setApiKey(String apiKey) {
        editor.putString(MainPreferencesConstants.API_KEY, apiKey);
        editor.apply();
    }

    public String getCity() {
        return sharedPreferences.getString(MainPreferencesConstants.CITY, MainPreferencesConstants.DEFAULT_CITY);
    }

    public void setCity(String city) {
        editor.putString(MainPreferencesConstants.CITY, city);
        editor.apply();
    }

    public String getUnits() {
        return sharedPreferences.getString(MainPreferencesConstants.UNITS, MainPreferencesConstants.DEFAULT_UNITS);
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

    public boolean getDisplayDebugToasts()
    {
        return sharedPreferences.getBoolean(MainPreferencesConstants.DISPLAY_DEBUG_TOASTS, false);
    }

    public void setDisplayDebugToasts(boolean displayDebugToasts)
    {
        editor.putBoolean(MainPreferencesConstants.DISPLAY_DEBUG_TOASTS, displayDebugToasts);
        editor.apply();
    }

}
