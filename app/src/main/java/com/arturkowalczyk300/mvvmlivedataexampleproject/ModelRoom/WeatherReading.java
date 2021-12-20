package com.arturkowalczyk300.mvvmlivedataexampleproject.ModelRoom;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.util.Date;


@Entity(tableName = "weather_table")
public class WeatherReading {


    @PrimaryKey(autoGenerate = true)
    private int id;

    private Date readTime;
    private float temperature;
    private int pressure;
    private int humidity;
    private Units unit;

    public WeatherReading(Date readTime, float temperature, int pressure, int humidity, Units unit) {
        this.readTime = readTime;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.unit = unit;
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

    public long getReadTimeLong() {
        return readTime.getTime();
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

    public Units getUnit() {
        return unit;
    }

    public void setUnit(Units unit) {
        this.unit = unit;
    }
}
