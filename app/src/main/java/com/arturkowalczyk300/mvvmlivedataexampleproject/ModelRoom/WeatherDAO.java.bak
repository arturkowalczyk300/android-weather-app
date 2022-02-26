package com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom;

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

    @Query("DELETE FROM weather_table") //NON-NLS
    void deleteAllWeatherReadings();

    @Query("SELECT * FROM weather_table ORDER BY readTime DESC") //NON-NLS
    LiveData<List<WeatherReading>> getAllWeatherReadings();

    @Query("SELECT COUNT(*) FROM weather_table") //NON-NLS
    LiveData<Integer> getCount();

    @Query("DELETE FROM weather_table where id NOT IN(SELECT id from weather_table ORDER BY id DESC LIMIT :MAX_COUNT)") //NON-NLS
    void deleteExcess(int MAX_COUNT);

}
