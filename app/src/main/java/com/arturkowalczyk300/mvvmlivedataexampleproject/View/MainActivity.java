package com.arturkowalczyk300.mvvmlivedataexampleproject.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelApi.ConnectionLiveData;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelApi.ConnectionModel;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom.Units;
import com.arturkowalczyk300.mvvmlivedataexampleproject.Preferences.MainPreferencesConstants;
import com.arturkowalczyk300.mvvmlivedataexampleproject.R;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ViewModel.WeatherReadingAdapter;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ViewModel.WeatherReadingsViewModel;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom.WeatherReading;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pranavpandey.android.dynamic.toasts.DynamicHint;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_WEATHER_READING_REQUEST = 1;
    public static final int EDIT_WEATHER_READING_REQUEST = 2;
    public static final int SETTINGS_ACTIVITY_REQUEST = 3;

    private WeatherReadingsViewModel weatherReadingsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddWeatherReading = findViewById(R.id.button_add_weather_reading);
        buttonAddWeatherReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditWeatherReading.class);
                startActivityForResult(intent, ADD_WEATHER_READING_REQUEST);
            }
        });

        TextView textViewRecordsCount = findViewById(R.id.textView_records_count);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        WeatherReadingAdapter adapter = new WeatherReadingAdapter();
        recyclerView.setAdapter(adapter);

        weatherReadingsViewModel = ViewModelProviders.of(this).get(WeatherReadingsViewModel.class);
        weatherReadingsViewModel.getAllWeatherReadings().observe(this, new Observer<List<WeatherReading>>() {
            @Override
            public void onChanged(List<WeatherReading> weatherReadings) {
                //update RecyclerView
                adapter.submitList(weatherReadings);
                adapter.setDataLoadedFromApiFlag(weatherReadingsViewModel.
                        getDataLoadingFromApiSuccessObservable().getValue());
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                weatherReadingsViewModel.delete(adapter.getWeatherReadingAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Weather reading deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        //Dynamic toast config
        DynamicToast.Config.getInstance().
                setSuccessBackgroundColor(Color.GREEN).
                setErrorBackgroundColor(Color.RED).
                apply();

        //handle auto-scrolling, when new item will be added (and showed!) to recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                recyclerView.scrollToPosition(0);
                //int firstVisibleItemPosition = findFirstVisibleItemPosition();
            }
        };
        recyclerView.setLayoutManager(layoutManager);

        //handle click on item
        adapter.setOnItemClickListener(new WeatherReadingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(WeatherReading weatherReading) {
                Intent intent = new Intent(MainActivity.this, AddEditWeatherReading.class);
                try {
                    intent.putExtra(AddEditWeatherReading.EXTRA_ID, weatherReading.getId());
                    intent.putExtra(AddEditWeatherReading.EXTRA_READTIME, weatherReading.getReadTimeLong());
                    intent.putExtra(AddEditWeatherReading.EXTRA_TEMPERATURE, weatherReading.getTemperature());
                    intent.putExtra(AddEditWeatherReading.EXTRA_PRESSURE, weatherReading.getPressure());
                    intent.putExtra(AddEditWeatherReading.EXTRA_HUMIDITY, weatherReading.getHumidity());

                } catch (NullPointerException ex) {
                    Toast.makeText(getApplicationContext(), "Null pointer exception, class=" + ex.getStackTrace()[0].getClassName()
                            + ", method=" + ex.getStackTrace()[0].getMethodName()
                            + ", line=" + ex.getStackTrace()[0].getLineNumber(), Toast.LENGTH_LONG).show();
                }
                startActivityForResult(intent, EDIT_WEATHER_READING_REQUEST);
            }
        });

        //set observable for getting rows count
        weatherReadingsViewModel.getCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textViewRecordsCount.setText(String.valueOf(integer));

                if (integer.intValue() > weatherReadingsViewModel.getMaxCount()) {
                    weatherReadingsViewModel.deleteExcessWeatherReadings();
                }
            }
        });

        //set observable for internet connection state
        TextView textViewNoInternetConnection = findViewById(R.id.textView_no_internet_connection);

        //set observables to toasts messages from ViewModel
        weatherReadingsViewModel.getMutableLiveDataToastDefault().observe(this, new Observer<Pair<Boolean, String>>() {
            @Override
            public void onChanged(Pair<Boolean, String> booleanStringPair) {
                if(booleanStringPair.first.booleanValue() && weatherReadingsViewModel.getDisplayDebugToasts())
                    DynamicToast.make(getApplicationContext(), booleanStringPair.second).show();
            }
        });

        weatherReadingsViewModel.getMutableLiveDataToastSuccess().observe(this, new Observer<Pair<Boolean, String>>() {
            @Override
            public void onChanged(Pair<Boolean, String> booleanStringPair) {
                if(booleanStringPair.first.booleanValue() && weatherReadingsViewModel.getDisplayDebugToasts())
                    DynamicToast.makeSuccess(getApplicationContext(), booleanStringPair.second).show();
            }
        });

        weatherReadingsViewModel.getMutableLiveDataToastError().observe(this, new Observer<Pair<Boolean, String>>() {
            @Override
            public void onChanged(Pair<Boolean, String> booleanStringPair) {
                if(booleanStringPair.first.booleanValue() && weatherReadingsViewModel.getDisplayDebugToasts())
                    DynamicToast.makeError(getApplicationContext(), booleanStringPair.second).show();
            }
        });

        ConnectionLiveData connectionLiveData = new ConnectionLiveData(getApplicationContext());
        connectionLiveData.observe(this, new Observer<ConnectionModel>() {
            @Override
            public void onChanged(ConnectionModel connectionModel) {
                if (connectionModel.isConnected()) {
                    textViewNoInternetConnection.setVisibility(View.GONE);
                } else {
                    textViewNoInternetConnection.setVisibility(View.VISIBLE);
                }
            }
        });


        //observe data loading from API state and bind it with progress bar visibility
        ProgressBar progressBar = findViewById(R.id.progress_bar_loading);
        weatherReadingsViewModel.getInDataLoadingStateObservable().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean inLoadingState) {
                progressBar.setVisibility(inLoadingState ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_WEATHER_READING_REQUEST && resultCode == RESULT_OK) {

            long readTimeLong = data.getLongExtra(AddEditWeatherReading.EXTRA_READTIME, -1);
            Date readTime = new Date(readTimeLong);
            String temperatureStr = data.getStringExtra(AddEditWeatherReading.EXTRA_TEMPERATURE);
            String pressureStr = data.getStringExtra(AddEditWeatherReading.EXTRA_PRESSURE);
            String humidityStr = data.getStringExtra(AddEditWeatherReading.EXTRA_HUMIDITY);

            WeatherReading weatherReading = new WeatherReading(readTime,
                    Float.parseFloat(temperatureStr),
                    Integer.parseInt(pressureStr),
                    Integer.parseInt(humidityStr),
                    Units.valueOf(weatherReadingsViewModel.getUnits()));
            weatherReadingsViewModel.insert(weatherReading);

            Toast.makeText(this, "Weather reading saved!", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_WEATHER_READING_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditWeatherReading.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Weather reading can't be updated!", Toast.LENGTH_SHORT).show();
                return;
            }

            long readTimeLong = data.getLongExtra(AddEditWeatherReading.EXTRA_READTIME, -1);
            Date readTime = new Date(readTimeLong);
            String temperatureStr = data.getStringExtra(AddEditWeatherReading.EXTRA_TEMPERATURE);
            String pressureStr = data.getStringExtra(AddEditWeatherReading.EXTRA_PRESSURE);
            String humidityStr = data.getStringExtra(AddEditWeatherReading.EXTRA_HUMIDITY);

            float temperature = Float.parseFloat(temperatureStr);
            int pressure = (int) Float.parseFloat(pressureStr);
            int humidity = (int) Float.parseFloat(humidityStr);

            WeatherReading weatherReading = new WeatherReading(readTime,
                    temperature,
                    pressure,
                    humidity,
                    Units.valueOf(weatherReadingsViewModel.getUnits()));
            weatherReading.setId(id);
            weatherReadingsViewModel.update(weatherReading);

            Toast.makeText(this, "Weather reading updated!", Toast.LENGTH_SHORT).show();
        } else if ((requestCode == ADD_WEATHER_READING_REQUEST || requestCode == EDIT_WEATHER_READING_REQUEST)
                && resultCode != RESULT_OK) {
            Toast.makeText(this, "Weather reading not saved!", Toast.LENGTH_SHORT).show();
        } else if (requestCode == SETTINGS_ACTIVITY_REQUEST) {
            try {
                String city = data.getStringExtra(MainPreferencesConstants.CITY);
                String units = data.getStringExtra(MainPreferencesConstants.UNITS);
                int maxCount = data.getIntExtra(MainPreferencesConstants.MAX_COUNT, 10);
                boolean displayDebugToasts = data.getBooleanExtra(
                        MainPreferencesConstants.DISPLAY_DEBUG_TOASTS, false);

                weatherReadingsViewModel.setCityName(city);
                weatherReadingsViewModel.setUNITS(units);
                weatherReadingsViewModel.setMaxCount(maxCount);
                weatherReadingsViewModel.setDisplayDebugToasts(displayDebugToasts);
                Toast.makeText(this, "settings updated!", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
            }

        }
        Toast.makeText(this, "req=" + String.valueOf(requestCode) + ", res=" + String.valueOf(resultCode), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete_all_weather_readings:
                weatherReadingsViewModel.deleteAllWeatherReadings();
                Toast.makeText(this, "All weather readings deleted!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_delete_excess_weather_readings:
                weatherReadingsViewModel.deleteExcessWeatherReadings();
                Toast.makeText(this, "Excess records deleted!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_settings:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                //put settings to bundle
                settingsIntent.putExtra(MainPreferencesConstants.CITY, weatherReadingsViewModel.getCityName());
                settingsIntent.putExtra(MainPreferencesConstants.UNITS, weatherReadingsViewModel.getUnits());
                settingsIntent.putExtra(MainPreferencesConstants.MAX_COUNT, weatherReadingsViewModel.getMaxCount());
                settingsIntent.putExtra(MainPreferencesConstants.DISPLAY_DEBUG_TOASTS, weatherReadingsViewModel.getDisplayDebugToasts());
                startActivityForResult(settingsIntent, SETTINGS_ACTIVITY_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}