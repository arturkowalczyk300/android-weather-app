package com.arturkowalczyk300.mvvmlivedataexampleproject.Model;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelApi.WeatherReadingApi;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom.WeatherDAO;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelApi.WeatherReadingFromApi;
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

    private MutableLiveData<Boolean> inDataLoadingStateObservable;
    private MutableLiveData<Boolean> dataLoadingFromApiSuccessObservable;

    private static String CITY_NAME = "Wolow";
    private static String API_KEY = "to_be_written";
    private static String UNITS = "metric";

    public static String getRecyclerNormalItemColor() {
        return RECYCLER_NORMAL_ITEM_COLOR;
    }

    private static String RECYCLER_NORMAL_ITEM_COLOR = "#0E3C51";
    private static String RECYCLER_NEW_ITEM_COLOR = "#043901";

    private static int MAX_COUNT = 10;

    private static final int ERROR_VALUE_INT = -1;
    private static final float ERROR_VALUE_FLOAT = -1;

    private Date readTime;
    private float temperature;
    private int pressure;
    private int humidity;

    private WeatherReading getAndInsertWeatherReadingFromApi() {
        readTime = Calendar.getInstance().getTime();
        temperature = ERROR_VALUE_FLOAT;
        pressure = ERROR_VALUE_INT;
        humidity = ERROR_VALUE_INT;


        WeatherReadingApi api = RetrofitClient.getWeatherReadingApiInstance();
        Call<WeatherReadingFromApi> call;
        inDataLoadingStateObservable = new MutableLiveData<>(false);
        dataLoadingFromApiSuccessObservable = new MutableLiveData<>(false);
        inDataLoadingStateObservable.setValue(true);

        try {
            call = api.getReading(CITY_NAME, API_KEY, UNITS);
            call.enqueue(new Callback<WeatherReadingFromApi>() {
                @Override
                public void onResponse(Call<WeatherReadingFromApi> call,
                                       Response<WeatherReadingFromApi> response) {
                    inDataLoadingStateObservable.setValue(false);

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
                    dataLoadingFromApiSuccessObservable.setValue(true);
                }

                @Override
                public void onFailure(Call<WeatherReadingFromApi> call, Throwable t) {
                    inDataLoadingStateObservable.setValue(false);
                    Log.e("ERROR", "Call enqueue failure!, url:" + call.request().url().toString());
                }
            });

        } catch (Exception ex) {
            inDataLoadingStateObservable.setValue(false);
            Log.e("ERROR", ex.toString());
        }


        return new WeatherReading(readTime, temperature, pressure, humidity);
    }

    public static String getCityName() {
        return CITY_NAME;
    }

    public static void setCityName(String cityName) {
        CITY_NAME = cityName;
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public static void setApiKey(String apiKey) {
        API_KEY = apiKey;
    }

    public static String getUNITS() {
        return UNITS;
    }

    public static void setUNITS(String UNITS) {
        WeatherReadingsRepository.UNITS = UNITS;
    }

    public static int getMaxCount() {
        return MAX_COUNT;
    }

    public static void setMaxCount(int maxCount) {
        MAX_COUNT = maxCount;
    }

    public WeatherReadingsRepository(Application application) {
        //get readings from local database
        WeatherReadingsDatabase database = WeatherReadingsDatabase.getInstance(application);
        weatherDAO = database.weatherDAO();
        allWeatherReadings = weatherDAO.getAllWeatherReadings();

        getAndInsertWeatherReadingFromApi();
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

    public void deleteExcessWeatherReadings() {
        new DeleteExcessWeatherReadingsAsyncTask(weatherDAO).execute(MAX_COUNT);
    }

    public void deleteAllWeatherReadings() {
        new DeleteAllWeatherReadingsAsyncTask(weatherDAO).execute();
    }

    public LiveData<List<WeatherReading>> getAllWeatherReadings() {
        return allWeatherReadings;
    }

    public LiveData<Integer> getCount() {
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
            weatherDAO.delete(weatherReadings[0]); //delete given object
            return null;
        }
    }

    private static class DeleteExcessWeatherReadingsAsyncTask extends AsyncTask<Integer, Void, Void> {
        private WeatherDAO weatherDAO;

        private DeleteExcessWeatherReadingsAsyncTask(WeatherDAO weatherDAO) {
            this.weatherDAO = weatherDAO;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            weatherDAO.deleteExcess(integers[0]);
            return null;
        }

        ;
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

    public MutableLiveData<Boolean> getInDataLoadingStateObservable() {
        return inDataLoadingStateObservable;
    }
    public MutableLiveData<Boolean> getDataLoadingFromApiSuccessObservable() {
        return dataLoadingFromApiSuccessObservable;
    }

    public static String getRecyclerNewItemColor() {
        return RECYCLER_NEW_ITEM_COLOR;
    }
}
