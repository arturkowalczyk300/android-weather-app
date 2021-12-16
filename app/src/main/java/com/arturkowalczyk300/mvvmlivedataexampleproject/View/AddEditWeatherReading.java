package com.arturkowalczyk300.mvvmlivedataexampleproject.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.arturkowalczyk300.mvvmlivedataexampleproject.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddEditWeatherReading extends AppCompatActivity {
    public static final String EXTRA_ID = "com.arturkowalczyk300.mvvmlivedataexampleproject.EXTRA_ID";
    public static final String EXTRA_READTIME = "com.arturkowalczyk300.mvvmlivedataexampleproject.EXTRA_READTIME";
    public static final String EXTRA_TEMPERATURE = "com.arturkowalczyk300.mvvmlivedataexampleproject.EXTRA_TEMPERATURE";
    public static final String EXTRA_PRESSURE = "com.arturkowalczyk300.mvvmlivedataexampleproject.EXTRA_PRESSURE";
    public static final String EXTRA_HUMIDITY = "com.arturkowalczyk300.mvvmlivedataexampleproject.EXTRA_HUMIDITY";
    public static final String DATE_FORMAT = "HH:mm dd.MM.yyyy";
    public static final int DATE_START_YEAR = 1900;

    Calendar currentSublimeDateTime;
    Calendar databaseDateTime;
    private EditText editTextTemperature;
    private EditText editTextPressure;
    private EditText editTextHumidity;
    private Button buttonEditReadTime;
    private TextView textViewReadTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_weather_reading);

        databaseDateTime = Calendar.getInstance();
        currentSublimeDateTime = Calendar.getInstance();

        textViewReadTime = findViewById(R.id.textViewReadTime);
        buttonEditReadTime = findViewById(R.id.buttonEditReadTime);
        editTextTemperature = findViewById(R.id.editTextTemperature);
        editTextPressure = findViewById(R.id.editTextPressure);
        editTextHumidity = findViewById(R.id.editTextHumidity);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit weather reading");

            long readTimeLong = intent.getLongExtra(EXTRA_READTIME, -1);
            Date readTime = new Date(readTimeLong);


            float temperature = intent.getFloatExtra(EXTRA_TEMPERATURE, -1);
            int pressure = intent.getIntExtra(EXTRA_PRESSURE, -1);
            int humidity = intent.getIntExtra(EXTRA_HUMIDITY, -1);


            int year = readTime.getYear();
            int month = readTime.getMonth();
            int dayOfMonth = readTime.getDate();
            int currentHour = readTime.getHours();
            int currentMinute = readTime.getMinutes();
            databaseDateTime.set(year + DATE_START_YEAR, month, dayOfMonth, currentHour, currentMinute);
            //currentSublimeDateTime = databaseDateTime.clone();
            UpdateTextViewReadTime();
            //timePickerReadTime.setIs24HourView(true);
            //timePickerReadTime.setCurrentHour(currentHour);
            //timePickerReadTime.setCurrentMinute(currentMinute);

            editTextTemperature.setText(Float.toString(temperature));
            editTextPressure.setText(Integer.toString(pressure));
            editTextHumidity.setText(Integer.toString(humidity));
        } else {
            setTitle("Add weather reading");
        }

        buttonEditReadTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SublimePickerDialogFragment sublimePickerDialogFragment = new SublimePickerDialogFragment();
                sublimePickerDialogFragment.setCallback(mFragmentCallback);
                sublimePickerDialogFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

                //options
                SublimeOptions options = new SublimeOptions();
                options.setDateParams(databaseDateTime);
                options.setTimeParams(databaseDateTime.get(Calendar.HOUR_OF_DAY),
                        databaseDateTime.get(Calendar.MINUTE),
                        true);

                //put options to bundle
                Bundle bundle = new Bundle();
                bundle.putParcelable("SUBLIME_OPTIONS", options);
                sublimePickerDialogFragment.setArguments(bundle);

                //show
                sublimePickerDialogFragment.show(getSupportFragmentManager(), "SUBLIME_PICKER");
            }
        });


    }

    private void saveWeatherReading() {
        Calendar calendar = currentSublimeDateTime;

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        String readTimeStr = dateFormat.format(calendar.getTime());
        long readTimeLong = calendar.getTimeInMillis();

        String temperatureStr = editTextTemperature.getText().toString();
        String pressureStr = editTextPressure.getText().toString();
        String humidityStr = editTextHumidity.getText().toString();

        if (temperatureStr.trim().isEmpty() || pressureStr.trim().isEmpty() || humidityStr.trim().isEmpty()) {
            Toast.makeText(this, "Please insert values!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_READTIME, readTimeLong);
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

    SublimePickerDialogFragment.Callback mFragmentCallback = new SublimePickerDialogFragment.Callback() {
        @Override
        public void onCancelled() {
            Toast.makeText(getApplicationContext(), "onCancelled!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate, int hourOfDay, int minute, SublimeRecurrencePicker.RecurrenceOption recurrenceOption, String recurrenceRule) {
            Toast.makeText(getApplicationContext(), "onDateTimeRecurrenceSet!", Toast.LENGTH_SHORT).show();
            currentSublimeDateTime.set(selectedDate.getFirstDate().get(Calendar.YEAR),
                    selectedDate.getFirstDate().get(Calendar.MONTH),
                    selectedDate.getFirstDate().get(Calendar.DATE),
                    hourOfDay,
                    minute);
            UpdateTextViewReadTime();
        }

    };

    private void UpdateTextViewReadTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        textViewReadTime.setText(sdf.format(currentSublimeDateTime.getTime()));

    }
}