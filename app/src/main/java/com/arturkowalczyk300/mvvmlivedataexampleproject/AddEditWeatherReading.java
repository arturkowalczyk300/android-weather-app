package com.arturkowalczyk300.mvvmlivedataexampleproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditWeatherReading extends AppCompatActivity {
    public static final String EXTRA_ID = "com.arturkowalczyk300.mvvmlivedataexampleproject.EXTRA_ID";
    public static final String EXTRA_READTIME = "com.arturkowalczyk300.mvvmlivedataexampleproject.EXTRA_READTIME";
    public static final String EXTRA_TEMPERATURE = "com.arturkowalczyk300.mvvmlivedataexampleproject.EXTRA_TEMPERATURE";
    public static final String EXTRA_PRESSURE = "com.arturkowalczyk300.mvvmlivedataexampleproject.EXTRA_PRESSURE";
    public static final String EXTRA_HUMIDITY = "com.arturkowalczyk300.mvvmlivedataexampleproject.EXTRA_HUMIDITY";

    private com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker dateTimePickerReadTime;
    private EditText editTextTemperature;
    private EditText editTextPressure;
    private EditText editTextHumidity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_weather_reading);

        dateTimePickerReadTime = findViewById(R.id.datePickerReadTime);
        editTextTemperature = findViewById(R.id.editTextTemperature);
        editTextPressure = findViewById(R.id.editTextPressure);
        editTextHumidity = findViewById(R.id.editTextHumidity);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit weather reading");
            //dateTimePickerReadTime. TODO: add handle of this
            float temperature = intent.getFloatExtra(EXTRA_TEMPERATURE, -1);
            int pressure = intent.getIntExtra(EXTRA_PRESSURE, -1);
            int humidity = intent.getIntExtra(EXTRA_HUMIDITY, -1);

            editTextTemperature.setText(Float.toString(temperature));
            editTextPressure.setText(Integer.toString(pressure));
            editTextHumidity.setText(Integer.toString(humidity));
        } else {
            setTitle("Add weather reading");
        }
    }

    private void saveWeatherReading() {
        //String readTime TODO:add readtime handle
        String temperatureStr = editTextTemperature.getText().toString();
        String pressureStr = editTextPressure.getText().toString();
        String humidityStr = editTextHumidity.getText().toString();

        if (temperatureStr.trim().isEmpty() || pressureStr.trim().isEmpty() || humidityStr.trim().isEmpty()) {
            Toast.makeText(this, "Please insert values!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TEMPERATURE, temperatureStr);
        data.putExtra(EXTRA_PRESSURE, pressureStr);
        data.putExtra(EXTRA_HUMIDITY, humidityStr);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_weather_reading_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_weatherReading:
                saveWeatherReading();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}