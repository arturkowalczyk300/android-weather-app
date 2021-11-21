package com.arturkowalczyk300.mvvmlivedataexampleproject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://api.openweathermap.org";
    private static Retrofit retrofitInstance;
    private static WeatherReadingApi weatherReadingApiInstance;

    private static Retrofit getRetrofitInstance()
    {
        if(retrofitInstance == null)
        {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitInstance;
    }

    public static WeatherReadingApi getWeatherReadingApiInstance()
    {
        if(weatherReadingApiInstance == null)
        {
            weatherReadingApiInstance = getRetrofitInstance().create(WeatherReadingApi.class);
        }
        return weatherReadingApiInstance;
    }

}
