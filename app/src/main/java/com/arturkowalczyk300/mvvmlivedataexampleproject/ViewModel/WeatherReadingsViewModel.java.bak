package com.arturkowalczyk300.mvvmlivedataexampleproject.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

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

    public void deleteExcessWeatherReadings()
    {
        repository.deleteExcessWeatherReadings();
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

    public MutableLiveData<Boolean> getInDataLoadingStateObservable() {
        return repository.getInDataLoadingStateObservable();
    }

    public MutableLiveData<Boolean> getDataLoadingFromApiSuccessObservable() {
        return repository.getDataLoadingFromApiSuccessObservable();
    }

    public String getCityName() {
        return repository.getCityName();
    }

    public void setCityName(String cityName) {
        repository.setCityName(cityName);
    }

    public String getApiKey() {
        return repository.getApiKey();
    }

    public void setApiKey(String apiKey) {
        repository.setApiKey(apiKey);
    }

    public String getUnits() {
        return repository.getUnits();
    }

    public void setUNITS(String units) {
        repository.setUNITS(units);
    }

    public int getMaxCount() {
        return repository.getMaxCount();
    }

    public void setMaxCount(int maxCount) {
        repository.setMaxCount(maxCount);
    }

}
