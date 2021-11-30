package com.arturkowalczyk300.mvvmlivedataexampleproject.ModelApi;

import com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom.WeatherReadingFromApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherReadingApi {
    @GET("/data/2.5/weather")
    Call<WeatherReadingFromApi> getReading(@Query("q") String cityName, @Query("appid") String APIkey, @Query("units") String units);
}
