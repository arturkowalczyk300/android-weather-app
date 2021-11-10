package com.arturkowalczyk300.mvvmlivedataexampleproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WeatherReadingsViewModel weatherReadingsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton buttonAddWeatherReading = findViewById(R.id.button_add_weather_reading);
        buttonAddWeatherReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent()
                //todo: add AddEditWeatherReadingActivity here
            }
        });

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


    }
}