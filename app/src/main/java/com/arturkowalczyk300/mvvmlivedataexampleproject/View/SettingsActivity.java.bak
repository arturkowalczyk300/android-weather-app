package com.arturkowalczyk300.mvvmlivedataexampleproject.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom.Units;
import com.arturkowalczyk300.mvvmlivedataexampleproject.Preferences.MainPreferencesConstants;
import com.arturkowalczyk300.mvvmlivedataexampleproject.R;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.DecimalMin;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @NotEmpty
    @Pattern(regex="[\\p{Lower}\\p{Upper}\\p{Space}]+", message = "City name can contains only letters")
    EditText editTextCityName;

    @NotEmpty
    @DecimalMin(value=1, message = "Value is too low")
    EditText editTextMaxCount;

    Button buttonSave;
    RadioGroup radioGroupUnits;
    RadioButton radioButtonUnitsMetric;
    RadioButton radioButtonUnitsImperial;
    Switch switchDisplayDebugToasts;
    Validator validator;

    boolean validationSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //get bindings to views
        editTextCityName = findViewById(R.id.settings_textInput_city);
        editTextMaxCount = findViewById(R.id.settings_textInput_max_records);
        switchDisplayDebugToasts = findViewById(R.id.settings_switch_display_debug_toasts);
        radioGroupUnits = findViewById(R.id.settings_radio_group_units);
        radioButtonUnitsMetric = findViewById(R.id.settings_radio_button_units_metric);
        radioButtonUnitsImperial = findViewById(R.id.settings_radio_button_units_imperial);
        buttonSave = findViewById(R.id.settings_button_save);

        //get stored settings from bundle
        String apiKey = getIntent().getExtras().getString(MainPreferencesConstants.API_KEY);
        String cityName = getIntent().getExtras().getString(MainPreferencesConstants.CITY);
        String units = getIntent().getExtras().getString(MainPreferencesConstants.UNITS);
        int maxCount = getIntent().getExtras().getInt(MainPreferencesConstants.MAX_COUNT);
        boolean displayDebugToasts = getIntent().getExtras().getBoolean(MainPreferencesConstants.DISPLAY_DEBUG_TOASTS);

        //fill layout views with values
        editTextCityName.setText(cityName);
        setCheckedUnitsRadioButtons(units);
        editTextMaxCount.setText(String.valueOf(maxCount));
        switchDisplayDebugToasts.setChecked(displayDebugToasts);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSaveSettings();
            }
        });

        editTextCityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validator.validate();
            }
        });

        editTextMaxCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validator.validate();
            }
        });

        //set validator listener
        validator = new Validator(this);
        validator.setValidationListener(new Validator.ValidationListener() {
            @Override
            public void onValidationSucceeded() {
                validationSuccess = true;
            }

            @Override
            public void onValidationFailed(List<ValidationError> errors) {
                validationSuccess = false;

                for(ValidationError error : errors)
                {
                    View view = error.getView();
                    String message = error.getCollatedErrorMessage(getApplicationContext());

                    if(view instanceof EditText)
                        ((EditText) view).setError(message);
                    else
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onPause() {
        this.saveSettingsAndExit(true);
        super.onPause();
    }

    private void validateAndSaveSettings()
    {
        validator.validate(false);
        if(validationSuccess)
            saveSettingsAndExit(false); //save settings and exit activity
    }

    private void saveSettingsAndExit(boolean cancelled) {
        String city = editTextCityName.getText().toString();
        String units = getSelectedUnits();
        int maxCount = Integer.parseInt(editTextMaxCount.getText().toString());
        boolean displayDebugToasts = switchDisplayDebugToasts.isChecked();

        Intent data = new Intent();
        data.putExtra(MainPreferencesConstants.CITY, city);
        data.putExtra(MainPreferencesConstants.UNITS, units);
        data.putExtra(MainPreferencesConstants.MAX_COUNT, maxCount);
        data.putExtra(MainPreferencesConstants.DISPLAY_DEBUG_TOASTS, displayDebugToasts);
        if(cancelled)
            setResult(RESULT_CANCELED);
        else
            setResult(RESULT_OK, data);
        finish();
    }

    private String getSelectedUnits() {
        if (radioGroupUnits.getCheckedRadioButtonId() == radioButtonUnitsMetric.getId())
            return Units.METRIC.toString();
        else if (radioGroupUnits.getCheckedRadioButtonId() == radioButtonUnitsImperial.getId())
            return Units.IMPERIAL.toString();
        else
            return Units.UNDEFINED.toString();
    }

    private void setCheckedUnitsRadioButtons(String units) {
        radioButtonUnitsMetric.setChecked(units.toUpperCase(Locale.ROOT)
                .equals(Units.METRIC.toString()));
        radioButtonUnitsImperial.setChecked(units.toUpperCase(Locale.ROOT)
                .equals(Units.IMPERIAL.toString()));
    }
}