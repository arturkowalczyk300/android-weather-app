package com.arturkowalczyk300.mvvmlivedataexampleproject.ViewModel;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom.WeatherReading;
import com.arturkowalczyk300.mvvmlivedataexampleproject.Model.WeatherReadingsRepository;
import com.arturkowalczyk300.mvvmlivedataexampleproject.R;

import java.util.List;

public class WeatherReadingsViewModel extends androidx.lifecycle.ViewModel {
    private WeatherReadingsRepository repository;
    private LiveData<List<WeatherReading>> allWeatherReadings;
    private MutableLiveData<Pair<Boolean, Pair<Integer, String>>> mutableLiveDataToastError; //[true, "error!"] -> displays error toast with text "error!"
    private MutableLiveData<Pair<Boolean, Integer>> mutableLiveDataToastDefault;
    private MutableLiveData<Pair<Boolean, Integer>> mutableLiveDataToastSuccess;
    private MutableLiveData<Boolean> mutableLiveDataRefreshRequest;

    public WeatherReadingsViewModel(@NonNull Application application) {
        repository = new WeatherReadingsRepository(application);
        mutableLiveDataToastDefault = new MutableLiveData<>();
        mutableLiveDataToastSuccess = new MutableLiveData<>();
        mutableLiveDataToastError = new MutableLiveData<>();
        mutableLiveDataRefreshRequest = new MutableLiveData<>();

        allWeatherReadings = repository.getAllWeatherReadings();

        //set observables to display toasts with results of Repository working
        repository.getGetReadingFromApiCorrectResponse().observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mutableLiveDataToastSuccess.setValue(Pair.create(Boolean.TRUE, R.string.viewModel_correctApiResponse));
            }
        });

        repository.getGetReadingFromApiNullBodyResponse().observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mutableLiveDataToastError.setValue(Pair.create(Boolean.TRUE, Pair.create(R.string.viewModel_nullBodyApiResponse, "")));
            }
        });

        repository.getGetReadingFromApiFailure().observeForever(new Observer<Pair<Boolean, Pair<Integer, String>>>() {
            @Override
            public void onChanged(Pair<Boolean, Pair<Integer, String>> booleanStringPair) {
                mutableLiveDataToastError.setValue(Pair.create(Boolean.TRUE, Pair.create(R.string.viewModel_apiReadFailure, booleanStringPair.second.toString())));
            }
        });
        repository.getGetReadingFromApiExceptionThrown().observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                mutableLiveDataToastError.setValue(Pair.create(Boolean.TRUE, Pair.create(R.string.viewModel_exceptionThrownApiResponse, "")));
            }
        });

        mutableLiveDataRefreshRequest.observeForever(new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean)
                    repository.getNewestReadingFromApi();
                    //mutableLiveDataRefreshRequest.setValue(false);
            }
        });
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

    public void deleteExcessWeatherReadings() {
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

    public boolean getDisplayDebugToasts()
    {
        return repository.getDisplayDebugToasts();
    }

    public void setDisplayDebugToasts(boolean displayDebugToasts)
    {
        repository.setDisplayDebugToasts(displayDebugToasts);
    }

    public MutableLiveData<Pair<Boolean, Pair<Integer,String>>> getMutableLiveDataToastError() {
        return mutableLiveDataToastError;
    }

    public MutableLiveData<Pair<Boolean, Integer>> getMutableLiveDataToastDefault() {
        return mutableLiveDataToastDefault;
    }

    public MutableLiveData<Pair<Boolean, Integer>> getMutableLiveDataToastSuccess() {
        return mutableLiveDataToastSuccess;
    }

    public MutableLiveData<Boolean> getMutableLiveDataRefreshRequest() {
        return mutableLiveDataRefreshRequest;
    }
}
