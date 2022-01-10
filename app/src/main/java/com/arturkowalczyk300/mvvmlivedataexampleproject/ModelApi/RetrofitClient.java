package com.arturkowalczyk300.mvvmlivedataexampleproject.ModelApi;

import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelApi.WeatherReadingApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://api.openweathermap.org";
    private static Retrofit retrofitInstance;
    private static OkHttpClient okHttpClientInstance;
    private static WeatherReadingApi weatherReadingApiInstance;

    private static OkHttpClient getOkHttpClientInstance() {
        if (okHttpClientInstance == null) {
            okHttpClientInstance = new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.SECONDS)
                    .readTimeout(2, TimeUnit.SECONDS)
                    .writeTimeout(2, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClientInstance;
    }

    private static Retrofit getRetrofitInstance() {
        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOkHttpClientInstance())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitInstance;
    }

    public static WeatherReadingApi getWeatherReadingApiInstance() {
        if (weatherReadingApiInstance == null) {
            weatherReadingApiInstance = getRetrofitInstance().create(WeatherReadingApi.class);
        }
        return weatherReadingApiInstance;
    }

}
