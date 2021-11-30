package com.arturkowalczyk300.mvvmlivedataexampleproject.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom.WeatherReading;
import com.arturkowalczyk300.mvvmlivedataexampleproject.Model.WeatherReadingsRepository;

import java.util.List;

public class WeatherReadingsViewModel extends androidx.lifecycle.AndroidViewModel {
    private WeatherReadingsRepository repository;
    private LiveData<List<WeatherReading>> allWeatherReadings;

    public WeatherReadingsViewModel(@NonNull Application application) {
        super(application);
        repository = new WeatherReadingsRepository(application);
        allWeatherReadings = repository.getAllWeatherReadings();
    }

    public void insert(WeatherReading weatherReading) {
        repository.insert(weatherReading);
    }

    public void update(WeatherReading weatherReading) {
        repository.update(weatherReading);
    }

    public void delete(WeatherReading weatherReading) {
        repository.delete(weatherReading);
    }

    public void deleteAllWeatherReadings() {
        repository.deleteAllWeatherReadings();
    }

    public LiveData<List<WeatherReading>> getAllWeatherReadings() {
        return allWeatherReadings;
    }

    public LiveData<Integer> getCount() {
        return repository.getCount();
    }
}
