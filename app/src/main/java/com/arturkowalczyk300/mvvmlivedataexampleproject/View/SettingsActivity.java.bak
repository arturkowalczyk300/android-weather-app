package com.arturkowalczyk300.mvvmlivedataexampleproject.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.arturkowalczyk300.mvvmlivedataexampleproject.Model.WeatherReadingsRepository;
import com.arturkowalczyk300.mvvmlivedataexampleproject.R;

public class SettingsActivity extends AppCompatActivity {

    EditText editTextCityName;
    EditText editTextApiKey;
    EditText editTextUnits;
    EditText editTextMaxCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //get bindings to views
        editTextCityName = findViewById(R.id.input_city);
        editTextApiKey = findViewById(R.id.input_api_key);
        editTextUnits = findViewById(R.id.input_units);
        editTextMaxCount = findViewById(R.id.input_max_records);

        //fill layout views with values
        editTextCityName.setText(WeatherReadingsRepository.getCityName());
        editTextApiKey.setText(WeatherReadingsRepository.getApiKey());
        editTextUnits.setText(WeatherReadingsRepository.getUNITS());
        editTextMaxCount.setText(String.valueOf(WeatherReadingsRepository.getMaxCount()));
    }

    @Override
    protected void onPause() {
        this.saveSettings();
        super.onPause();
    }

    private void saveSettings() {
        WeatherReadingsRepository.setCityName(editTextCityName.getText().toString());
        WeatherReadingsRepository.setApiKey(editTextApiKey.getText().toString());
        WeatherReadingsRepository.setUNITS(editTextUnits.getText().toString());
        WeatherReadingsRepository.setMaxCount(Integer.
                parseInt(editTextMaxCount.
                getText().toString()));
    }
}