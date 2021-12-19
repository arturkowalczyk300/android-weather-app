package com.arturkowalczyk300.mvvmlivedataexampleproject.View;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.arturkowalczyk300.mvvmlivedataexampleproject.Model.WeatherReadingsRepository;
import com.arturkowalczyk300.mvvmlivedataexampleproject.Preferences.MainPreferencesConstants;
import com.arturkowalczyk300.mvvmlivedataexampleproject.R;

public class SettingsActivity extends AppCompatActivity {

    EditText editTextCityName;
    EditText editTextApiKey;
    EditText editTextUnits;
    EditText editTextMaxCount;
    Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //get stored settings from bundle



        //get bindings to views
        editTextCityName = findViewById(R.id.input_city);
        editTextApiKey = findViewById(R.id.input_api_key);
        editTextUnits = findViewById(R.id.input_units);
        editTextMaxCount = findViewById(R.id.input_max_records);
        buttonSave = findViewById(R.id.button_settings_save);

        //fill layout views with values
        String apiKey = getIntent().getExtras().getString(MainPreferencesConstants.API_KEY);
        String cityName = getIntent().getExtras().getString(MainPreferencesConstants.CITY);
        String units = getIntent().getExtras().getString(MainPreferencesConstants.UNITS);
        int maxCount = getIntent().getExtras().getInt(MainPreferencesConstants.MAX_COUNT);

        editTextCityName.setText(cityName);
        editTextApiKey.setText(apiKey);
        editTextUnits.setText(units);
        editTextMaxCount.setText(String.valueOf(maxCount));

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

        Intent data = new Intent();
        data.putExtra(MainPreferencesConstants.API_KEY, apiKey);
        data.putExtra(MainPreferencesConstants.CITY, city);
        data.putExtra(MainPreferencesConstants.UNITS, units);
        data.putExtra(MainPreferencesConstants.MAX_COUNT, maxCount);
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