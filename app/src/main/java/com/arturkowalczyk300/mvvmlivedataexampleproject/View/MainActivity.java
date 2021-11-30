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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom.WeatherReadingFromApi;
import com.arturkowalczyk300.mvvmlivedataexampleproject.R;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ViewModel.WeatherReadingAdapter;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ViewModel.WeatherReadingsViewModel;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom.WeatherReading;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_WEATHER_READING_REQUEST = 1;
    public static final int EDIT_WEATHER_READING_REQUEST = 2;

    private WeatherReadingsViewModel weatherReadingsViewModel;

    WeatherReadingFromApi weatherReadingFromApi;


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
        weatherReadingsViewModel.getCount().observe(this, new Observer<Integer>(){
            @Override
            public void onChanged(Integer integer) {
                textViewRecordsCount.setText(String.valueOf(integer));
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

            WeatherReading weatherReading = new WeatherReading(readTime, Float.parseFloat(temperatureStr), Integer.parseInt(pressureStr), Integer.parseInt(humidityStr));
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

            WeatherReading weatherReading = new WeatherReading(readTime, temperature, pressure, humidity);
            weatherReading.setId(id);
            weatherReadingsViewModel.update(weatherReading);

            Toast.makeText(this, "Weather reading updated!", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(this, "Weather reading not saved!", Toast.LENGTH_SHORT).show();
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
            case R.id.delete_all_weather_readings:
                weatherReadingsViewModel.deleteAllWeatherReadings();
                Toast.makeText(this, "All weather readings deleted!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}