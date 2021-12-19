package com.arturkowalczyk300.mvvmlivedataexampleproject.Model;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelApi.ConnectionLiveData;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelApi.ConnectionModel;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelApi.WeatherReadingApi;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom.WeatherDAO;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelApi.WeatherReadingFromApi;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelApi.RetrofitClient;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom.WeatherReading;
import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom.WeatherReadingsDatabase;
import com.arturkowalczyk300.mvvmlivedataexampleproject.Preferences.PreferencesRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherReadingsRepository {

    private PreferencesRepository preferencesRepository;
    private WeatherDAO weatherDAO;
    private LiveData<List<WeatherReading>> allWeatherReadings;
    private ConnectionLiveData internetConnectionState;

    private MutableLiveData<Boolean> inDataLoadingStateObservable;
    private MutableLiveData<Boolean> dataLoadingFromApiSuccessObservable;

    private boolean internetAvailable = false;

    public static String getRecyclerNormalItemColor() {
        return RECYCLER_NORMAL_ITEM_COLOR;
    }

    private static String RECYCLER_NORMAL_ITEM_COLOR = "#0E3C51";
    private static String RECYCLER_NEW_ITEM_COLOR = "#043901";

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
        inDataLoadingStateObservable.setValue(true);

        try {
            call = api.getReading(getCityName(), getApiKey(), getUnits());
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
                    Log.e("ERROR", "Call enqueue failure!, url:" + call.request().url().toString()); //TODO : display there Toast  too
                }
            });

        } catch (Exception ex) {
            inDataLoadingStateObservable.setValue(false);
            Log.e("ERROR", ex.toString());
        }


        return new WeatherReading(readTime, temperature, pressure, humidity);
    }

    public String getCityName() {
        return preferencesRepository.getCity();
    }

    public void setCityName(String cityName) {
        preferencesRepository.setCity(cityName);
    }

    public String getApiKey() {
        return preferencesRepository.getApiKey();
    }

    public void setApiKey(String apiKey) {
        preferencesRepository.setApiKey(apiKey);
    }

    public String getUnits() {
        return preferencesRepository.getUnits();
    }

    public void setUNITS(String units) {
        preferencesRepository.setUnits(units);
    }

    public int getMaxCount() {
        return preferencesRepository.getMaxCount();
    }

    public void setMaxCount(int maxCount) {
        preferencesRepository.setMaxCount(maxCount);
    }

    public WeatherReadingsRepository(Application application) {
        //init preferences repository
        preferencesRepository = new PreferencesRepository(application.getApplicationContext());

        //observe connection state
        internetConnectionState = new ConnectionLiveData(application);
        internetConnectionState.observeForever (new Observer<ConnectionModel>() {
            @Override
            public void onChanged(ConnectionModel connectionModel) {
                internetAvailable = connectionModel.isConnected();
                if(internetAvailable && !dataLoadingFromApiSuccessObservable.getValue())
                    getAndInsertWeatherReadingFromApi();
            }
        });

        //get readings from local database
        WeatherReadingsDatabase database = WeatherReadingsDatabase.getInstance(application);
        weatherDAO = database.weatherDAO();
        allWeatherReadings = weatherDAO.getAllWeatherReadings();

        inDataLoadingStateObservable = new MutableLiveData<>(false);
        dataLoadingFromApiSuccessObservable = new MutableLiveData<>(false);
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
        new DeleteExcessWeatherReadingsAsyncTask(weatherDAO).execute(getMaxCount());
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
