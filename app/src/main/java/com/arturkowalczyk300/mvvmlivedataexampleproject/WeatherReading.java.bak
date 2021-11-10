package com.arturkowalczyk300.mvvmlivedataexampleproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "weather_table")
public class WeatherReading {


    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date readTime;
    private float temperature;
    private int pressure;
    private int humidity;

    public WeatherReading(Date readTime, float temperature, int pressure, int humidity) {
        this.readTime = readTime;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Date getReadTime() {
        return readTime;
    }

    public float getTemperature() {
        return temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }
}
