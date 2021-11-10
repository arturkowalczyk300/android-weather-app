package com.arturkowalczyk300.mvvmlivedataexampleproject;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface WeatherDAO {
    @Insert
    void insert(WeatherReading weatherReading);

    @Update
    void update(WeatherReading weatherReading);

    @Delete
    void delete(WeatherReading weatherReading);

    @Query("DELETE FROM weather_table")
    void deleteAllWeatherReadings();

    @Query("SELECT * FROM weather_table ORDER BY readTime DESC")
    LiveData<List<WeatherReading>> getAllWeatherReadings();

}
