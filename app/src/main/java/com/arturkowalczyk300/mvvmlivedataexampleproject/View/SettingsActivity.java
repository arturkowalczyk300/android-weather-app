package com.arturkowalczyk300.mvvmlivedataexampleproject.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.arturkowalczyk300.mvvmlivedataexampleproject.Preferences.MainPreferencesConstants;
import com.arturkowalczyk300.mvvmlivedataexampleproject.R;

public class SettingsActivity extends AppCompatActivity {

    EditText editTextCityName;
    EditText editTextApiKey;
    EditText editTextUnits;
    EditText editTextMaxCount;
    Button buttonSave;
    Switch switchDisplayDebugToasts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //get stored settings from bundle



        //get bindings to views
        editTextCityName = findViewById(R.id.settings_textInput_city);
        editTextApiKey = findViewById(R.id.settings_textInput_api_key);
        editTextUnits = findViewById(R.id.settings_textInput_units);
        editTextMaxCount = findViewById(R.id.settings_textInput_max_records);
        switchDisplayDebugToasts = findViewById(R.id.settings_switch_display_debug_toasts);
        buttonSave = findViewById(R.id.settings_button_save);

        //fill layout views with values
        String apiKey = getIntent().getExtras().getString(MainPreferencesConstants.API_KEY);
        String cityName = getIntent().getExtras().getString(MainPreferencesConstants.CITY);
        String units = getIntent().getExtras().getString(MainPreferencesConstants.UNITS);
        int maxCount = getIntent().getExtras().getInt(MainPreferencesConstants.MAX_COUNT);
        boolean displayDebugToasts = getIntent().getExtras().getBoolean(MainPreferencesConstants.DISPLAY_DEBUG_TOASTS);

        editTextCityName.setText(cityName);
        editTextApiKey.setText(apiKey);
        editTextUnits.setText(units);
        editTextMaxCount.setText(String.valueOf(maxCount));
        switchDisplayDebugToasts.setChecked(displayDebugToasts);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               saveSettings();
            }
        });


    }

    @Override
    protected void onPause() {
        this.saveSettings();
        super.onPause();
    }

    private void saveSettings() {
        String apiKey = editTextApiKey.getText().toString();
        String city = editTextCityName.getText().toString();
        String units = editTextUnits.getText().toString();
        int maxCount = Integer.parseInt(editTextMaxCount.getText().toString());
        boolean displayDebugToasts = switchDisplayDebugToasts.isChecked();

        Intent data = new Intent();
        data.putExtra(MainPreferencesConstants.API_KEY, apiKey);
        data.putExtra(MainPreferencesConstants.CITY, city);
        data.putExtra(MainPreferencesConstants.UNITS, units);
        data.putExtra(MainPreferencesConstants.MAX_COUNT, maxCount);
        data.putExtra(MainPreferencesConstants.DISPLAY_DEBUG_TOASTS, displayDebugToasts);
        setResult(2048, data);
        finish();

        /*WeatherReadingsRepository.setCityName(editTextCityName.getText().toString()); //TODO fix this!
        WeatherReadingsRepository.setApiKey(editTextApiKey.getText().toString());
        WeatherReadingsRepository.setUNITS(editTextUnits.getText().toString());
        WeatherReadingsRepository.setMaxCount(Integer.
                parseInt(editTextMaxCount.
                        getText().toString()));*/
    }
}