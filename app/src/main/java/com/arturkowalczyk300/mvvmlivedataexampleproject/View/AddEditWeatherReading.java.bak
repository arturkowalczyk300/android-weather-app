package com.arturkowalczyk300.mvvmlivedataexampleproject.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.DecimalMax;
import com.mobsandgeeks.saripaar.annotation.DecimalMin;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

    boolean validationSuccess;
    Validator validator;

    @NotEmpty
    @DecimalMin(value = Double.NEGATIVE_INFINITY, message = "Value should be number")
    @DecimalMax(value = Double.POSITIVE_INFINITY, message = "Value should be number")
    private EditText editTextTemperature;

    @NotEmpty
    @DecimalMin(value = 0, message = "Value should be greater than or equal to 0 hPa")
    @DecimalMax(value = 10000, message = "Value should be less than or equal to 10000 hPa")
    private EditText editTextPressure;

    @NotEmpty
    @DecimalMin(value = 0, message = "Value should be greater than or equal to 0%")
    @DecimalMax(value = 100, message = "Value should be less than or equal to 100%")
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
            float pressure = intent.getFloatExtra(EXTRA_PRESSURE, -1);
            float humidity = intent.getFloatExtra(EXTRA_HUMIDITY, -1);

            int year = readTime.getYear();
            int month = readTime.getMonth();
            int dayOfMonth = readTime.getDate();
            int currentHour = readTime.getHours();
            int currentMinute = readTime.getMinutes();
            databaseDateTime.set(year + DATE_START_YEAR, month, dayOfMonth, currentHour, currentMinute);
            UpdateTextViewReadTime();

            editTextTemperature.setText(Float.toString(temperature));
            editTextPressure.setText(Float.toString(pressure));
            editTextHumidity.setText(Float.toString(humidity));
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

        //validator
        validator = new Validator(this);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                validationSuccess = true;
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                validationSuccess = false;

                for (ValidationError error : errors) {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(getApplicationContext());


                    if (view instanceof EditText){
                        ((EditText) view).setError(message);
                    }
                }
            }
        });

        editTextTemperature.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validator.validate(false);
            }
        });

        editTextPressure.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validator.validate(false);
            }
        });

        editTextHumidity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validator.validate(false);
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
                validator.validate(false);
                if(validationSuccess)
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