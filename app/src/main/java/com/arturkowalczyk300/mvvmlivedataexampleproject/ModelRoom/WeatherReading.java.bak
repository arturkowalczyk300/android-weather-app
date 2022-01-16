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
    private float pressure;
    private float humidity;
    private String city;

    private Units unit;
    public WeatherReading(Date readTime, float temperature, float pressure, float humidity, Units unit, String city) {
        this.readTime = readTime;
        this.temperature = temperature;
        this.pressure = pressure;
        this.humidity = humidity;
        this.unit = unit;
        this.city= city;
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

    public float getPressure() {
        return pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public Units getUnit() {
        return unit;
    }

    public void setUnit(Units unit) {
        this.unit = unit;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
