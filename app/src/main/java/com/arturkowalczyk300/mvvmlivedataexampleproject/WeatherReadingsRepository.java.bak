package com.arturkowalczyk300.mvvmlivedataexampleproject;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Update;

import java.util.List;

public class WeatherReadingsRepository {
    private WeatherDAO weatherDAO;
    private LiveData<List<WeatherReading>> allWeatherReadings;

    public WeatherReadingsRepository(Application application)
    {
        WeatherReadingsDatabase database = WeatherReadingsDatabase.getInstance(application);
        weatherDAO = database.weatherDAO();
        allWeatherReadings = weatherDAO.getAllWeatherReadings();
    }

    public void insert(WeatherReading weatherReading)
    {
        new InsertWeatherReadingAsyncTask(weatherDAO).execute(weatherReading);
    }

    public void update(WeatherReading weatherReading)
    {
        new UpdateWeatherReadingAsyncTask(weatherDAO).execute(weatherReading);
    }

    public void delete(WeatherReading weatherReading)
    {
        new DeleteWeatherReadingAsyncTask(weatherDAO).execute(weatherReading);
    }

    public void deleteAllWeatherReadings()
    {
        new DeleteAllWeatherReadingsAsyncTask(weatherDAO).execute();
    }

    public LiveData<List<WeatherReading>> getAllWeatherReadings()
    {
        return allWeatherReadings;
    }

    private static class InsertWeatherReadingAsyncTask extends AsyncTask<WeatherReading, Void, Void>
    {
        private WeatherDAO weatherDAO;

        private InsertWeatherReadingAsyncTask(WeatherDAO weatherDAO){ this.weatherDAO = weatherDAO; }

        @Override
        protected Void doInBackground(WeatherReading... weatherReadings) {
            weatherDAO.insert(weatherReadings[0]);
            return null;
        }
    }

    private static class UpdateWeatherReadingAsyncTask extends AsyncTask<WeatherReading, Void, Void>
    {
        private WeatherDAO weatherDAO;

        private UpdateWeatherReadingAsyncTask(WeatherDAO weatherDAO){ this.weatherDAO = weatherDAO; }

        @Override
        protected Void doInBackground(WeatherReading... weatherReadings) {
            weatherDAO.update(weatherReadings[0]);
            return null;
        }
    }

    private static class DeleteWeatherReadingAsyncTask extends AsyncTask<WeatherReading, Void, Void>
    {
        private WeatherDAO weatherDAO;

        private DeleteWeatherReadingAsyncTask(WeatherDAO weatherDAO){ this.weatherDAO = weatherDAO; }

        @Override
        protected Void doInBackground(WeatherReading... weatherReadings) {
            weatherDAO.delete(weatherReadings[0]);
            return null;
        }
    }

    private static class DeleteAllWeatherReadingsAsyncTask extends AsyncTask<Void, Void, Void>
    {
        private WeatherDAO weatherDAO;

        private DeleteAllWeatherReadingsAsyncTask(WeatherDAO weatherDAO){ this.weatherDAO = weatherDAO; }

        @Override
        protected Void doInBackground(Void... voids) {
            weatherDAO.deleteAllWeatherReadings();
            return null;
        }
    }
}
