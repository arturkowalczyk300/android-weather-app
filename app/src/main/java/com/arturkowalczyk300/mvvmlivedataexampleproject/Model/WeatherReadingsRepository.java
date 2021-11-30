package com.arturkowalczyk300.mvvmlivedataexampleproject.Model;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelApi.WeatherReadingApi;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom.WeatherDAO;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom.WeatherReadingFromApi;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelApi.RetrofitClient;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom.WeatherReading;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom.WeatherReadingsDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherReadingsRepository {
    private WeatherDAO weatherDAO;
    private LiveData<List<WeatherReading>> allWeatherReadings;

    private static final String CITY_NAME = "Wolow";
    private static final String API_KEY = "to_be_written";
    private static final String UNITS = "metric";
    private static final int ERROR_VALUE_INT = -1;
    private static final float ERROR_VALUE_FLOAT = -1;

    private Date readTime;
    private float temperature;
    private int pressure;
    private int humidity;

    private WeatherReading getWeatherReadingFromApi() {
        readTime = Calendar.getInstance().getTime();
        temperature = ERROR_VALUE_FLOAT;
        pressure = ERROR_VALUE_INT;
        humidity = ERROR_VALUE_INT;


        WeatherReadingApi api = RetrofitClient.getWeatherReadingApiInstance();
        Call<WeatherReadingFromApi> call;

        try {
            call = api.getReading(CITY_NAME, API_KEY, UNITS);
            call.enqueue(new Callback<WeatherReadingFromApi>() {
                @Override
                public void onResponse(Call<WeatherReadingFromApi> call, Response<WeatherReadingFromApi> response) {
                    if (response.body() == null) {
                        //Toast.makeText(MainActivity.this, "Response body is null", Toast.LENGTH_LONG).show();
                        Log.e("ERROR", "Response body is null!");
                        return;
                    }

                    if (response.isSuccessful()) {
                        temperature = response.body().getMain().getTemp().floatValue();
                        pressure = response.body().getMain().getPressure().intValue(); // TODO: make conversion safe for sure ; https://stackoverflow.com/questions/42983242/idiomatic-way-of-converting-from-long-object-to-long-primitive-safely-in-java
                        humidity = response.body().getMain().getHumidity().intValue();
                    }

                    WeatherReading currentWeatherReading = new WeatherReading(readTime, temperature, pressure, humidity);
                    insert(currentWeatherReading);
                }

                @Override
                public void onFailure(Call<WeatherReadingFromApi> call, Throwable t) {

                    Log.e("ERROR", "Call enqueue failure!, url:"+call.request().url().toString());
                }
            });

        } catch (Exception ex) {
            Log.e("ERROR", ex.toString());
        }


        return new WeatherReading(readTime, temperature, pressure, humidity);
    }

    public WeatherReadingsRepository(Application application) {
        //get readings from local database
        WeatherReadingsDatabase database = WeatherReadingsDatabase.getInstance(application);
        weatherDAO = database.weatherDAO();
        allWeatherReadings = weatherDAO.getAllWeatherReadings();

        //get current weather reading from API
        getWeatherReadingFromApi();

    }

    public void insert(WeatherReading weatherReading) {
        new InsertWeatherReadingAsyncTask(weatherDAO).execute(weatherReading);
    }

    public void update(WeatherReading weatherReading) {
        new UpdateWeatherReadingAsyncTask(weatherDAO).execute(weatherReading);
    }

    public void delete(WeatherReading weatherReading) {
        new DeleteWeatherReadingAsyncTask(weatherDAO).execute(weatherReading);
    }

    public void deleteAllWeatherReadings() {
        new DeleteAllWeatherReadingsAsyncTask(weatherDAO).execute();
    }

    public LiveData<List<WeatherReading>> getAllWeatherReadings() {
        return allWeatherReadings;
    }

    public LiveData<Integer> getCount()
    {
        return weatherDAO.getCount();
    }

    private static class InsertWeatherReadingAsyncTask extends AsyncTask<WeatherReading, Void, Void> {
        private WeatherDAO weatherDAO;

        private InsertWeatherReadingAsyncTask(WeatherDAO weatherDAO) {
            this.weatherDAO = weatherDAO;
        }

        @Override
        protected Void doInBackground(WeatherReading... weatherReadings) {
            weatherDAO.insert(weatherReadings[0]);
            return null;
        }
    }

    private static class UpdateWeatherReadingAsyncTask extends AsyncTask<WeatherReading, Void, Void> {
        private WeatherDAO weatherDAO;

        private UpdateWeatherReadingAsyncTask(WeatherDAO weatherDAO) {
            this.weatherDAO = weatherDAO;
        }

        @Override
        protected Void doInBackground(WeatherReading... weatherReadings) {
            weatherDAO.update(weatherReadings[0]);
            return null;
        }
    }

    private static class DeleteWeatherReadingAsyncTask extends AsyncTask<WeatherReading, Void, Void> {
        private WeatherDAO weatherDAO;

        private DeleteWeatherReadingAsyncTask(WeatherDAO weatherDAO) {
            this.weatherDAO = weatherDAO;
        }

        @Override
        protected Void doInBackground(WeatherReading... weatherReadings) {
            weatherDAO.delete(weatherReadings[0]);
            return null;
        }
    }

    private static class DeleteAllWeatherReadingsAsyncTask extends AsyncTask<Void, Void, Void> {
        private WeatherDAO weatherDAO;

        private DeleteAllWeatherReadingsAsyncTask(WeatherDAO weatherDAO) {
            this.weatherDAO = weatherDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            weatherDAO.deleteAllWeatherReadings();
            return null;
        }
    }
}