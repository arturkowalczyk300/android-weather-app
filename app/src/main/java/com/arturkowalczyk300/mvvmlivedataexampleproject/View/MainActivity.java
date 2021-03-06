package com.arturkowalczyk300.mvvmlivedataexampleproject.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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
import com.arturkowalczyk300.mvvmlivedataexampleproject.ViewModel.WeatherReadingsViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_WEATHER_READING_REQUEST = 1;
    public static final int EDIT_WEATHER_READING_REQUEST = 2;
    public static final int SETTINGS_ACTIVITY_REQUEST = 3;

    private WeatherReadingsViewModel weatherReadingsViewModel;

    private boolean requestItemAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButtonAddWeatherReading = findViewById(R.id.floating_action_button_add_weather_reading);
        floatingActionButtonAddWeatherReading.setOnClickListener(new View.OnClickListener() {
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

        //hide floatingActionButton when scroll
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    floatingActionButtonAddWeatherReading.show();
                else floatingActionButtonAddWeatherReading.hide();

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                weatherReadingsViewModel.getMutableLiveDataRefreshRequest().setValue(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        WeatherReadingsViewModelFactory factory = new WeatherReadingsViewModelFactory(getApplication());

        weatherReadingsViewModel = new ViewModelProvider(this, factory).get(WeatherReadingsViewModel.class);

        //observable to set request animation flag
        weatherReadingsViewModel.getDataLoadingFromApiSuccessObservable().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    requestItemAnimation = true;
                    weatherReadingsViewModel.getDataLoadingFromApiSuccessObservable().setValue(false);
                }
            }
        });

        //observable to update recyclerView's item list
        weatherReadingsViewModel.getAllWeatherReadings().observe(this, new Observer<List<WeatherReading>>() {
            @Override
            public void onChanged(List<WeatherReading> weatherReadings) {
                //update RecyclerView
                adapter.submitList(weatherReadings);

                //set animation request
                adapter.setDataLoadedFromApiFlag(requestItemAnimation);
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
                Toast.makeText(MainActivity.this, getString(R.string.main_weatherReadingDeleted), Toast.LENGTH_SHORT).show();
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
                    intent.putExtra(AddEditWeatherReading.EXTRA_CITY, weatherReading.getCity());

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
        weatherReadingsViewModel.getMutableLiveDataToastDefault().observe(this, new Observer<Pair<Boolean, Integer>>() {
            @Override
            public void onChanged(Pair<Boolean, Integer> booleanStringPair) {
                if (booleanStringPair.first.booleanValue() && weatherReadingsViewModel.getDisplayDebugToasts())
                    DynamicToast.make(getApplicationContext(), getString(booleanStringPair.second)).show();
            }
        });

        weatherReadingsViewModel.getMutableLiveDataToastSuccess().observe(this, new Observer<Pair<Boolean, Integer>>() {
            @Override
            public void onChanged(Pair<Boolean, Integer> booleanStringPair) {
                if (booleanStringPair.first.booleanValue() && weatherReadingsViewModel.getDisplayDebugToasts())
                    DynamicToast.makeSuccess(getApplicationContext(), getString(booleanStringPair.second)).show();
            }
        });

        weatherReadingsViewModel.getMutableLiveDataToastError().observe(this, new Observer<Pair<Boolean, Pair<Integer,String>>>() {
            @Override
            public void onChanged(Pair<Boolean, Pair<Integer, String>> booleanStringPair) {
                if (booleanStringPair.first.booleanValue()) {
                    if (weatherReadingsViewModel.getDisplayDebugToasts())
                        if(booleanStringPair.second.first != null)
                            DynamicToast.makeError(getApplicationContext(), getString(booleanStringPair.second.first) + booleanStringPair.second.second).show(); //loaded string from context plus custom message
                        else
                            DynamicToast.makeError(getApplicationContext(), booleanStringPair.second.second).show(); //only custom message
                    else
                        DynamicToast.makeError(getApplicationContext(), getString(R.string.main_readingFromApiFailed)).show();
                }
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
            String cityStr = data.getStringExtra(AddEditWeatherReading.EXTRA_CITY);

            WeatherReading weatherReading = new WeatherReading(readTime,
                    Float.parseFloat(temperatureStr),
                    Integer.parseInt(pressureStr),
                    Integer.parseInt(humidityStr),
                    Units.valueOf(weatherReadingsViewModel.getUnits()),
                    cityStr);
            weatherReadingsViewModel.insert(weatherReading);

            Toast.makeText(this, getString(R.string.main_weatherReadingSaved), Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_WEATHER_READING_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditWeatherReading.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, getString(R.string.main_weatherReadingCantBeUpdated), Toast.LENGTH_SHORT).show();
                return;
            }

            long readTimeLong = data.getLongExtra(AddEditWeatherReading.EXTRA_READTIME, -1);
            Date readTime = new Date(readTimeLong);
            String temperatureStr = data.getStringExtra(AddEditWeatherReading.EXTRA_TEMPERATURE);
            String pressureStr = data.getStringExtra(AddEditWeatherReading.EXTRA_PRESSURE);
            String humidityStr = data.getStringExtra(AddEditWeatherReading.EXTRA_HUMIDITY);
            String cityStr = data.getStringExtra(AddEditWeatherReading.EXTRA_CITY);

            float temperature = Float.parseFloat(temperatureStr);
            int pressure = (int) Float.parseFloat(pressureStr);
            int humidity = (int) Float.parseFloat(humidityStr);

            WeatherReading weatherReading = new WeatherReading(readTime,
                    temperature,
                    pressure,
                    humidity,
                    Units.valueOf(weatherReadingsViewModel.getUnits()),
                    cityStr);
            weatherReading.setId(id);
            weatherReadingsViewModel.update(weatherReading);

            Toast.makeText(this, getString(R.string.main_weatherReadingUpdated), Toast.LENGTH_SHORT).show();
        } else if ((requestCode == ADD_WEATHER_READING_REQUEST || requestCode == EDIT_WEATHER_READING_REQUEST)
                && resultCode != RESULT_OK) {
            Toast.makeText(this, getString(R.string.main_weatherReadingNotSaved), Toast.LENGTH_SHORT).show();
        } else if (requestCode == SETTINGS_ACTIVITY_REQUEST) {
            if (resultCode == RESULT_OK) {
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
                    Toast.makeText(this, getString(R.string.main_settingsUpdated), Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == RESULT_CANCELED)
                Toast.makeText(this, getString(R.string.main_settingsNotUpdated), Toast.LENGTH_SHORT).show();
        }
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
                Toast.makeText(this, getString(R.string.main_allWeatherReadingsDeleted), Toast.LENGTH_SHORT).show();
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


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }
}