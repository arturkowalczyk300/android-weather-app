package com.arturkowalczyk300.mvvmlivedataexampleproject.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class WeatherReadingsViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    Application app;

    public WeatherReadingsViewModelFactory(Application app) {
        this.app = app;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WeatherReadingsViewModel.class))
            return (T) new WeatherReadingsViewModel(this.app);
        return null;
    }
}
