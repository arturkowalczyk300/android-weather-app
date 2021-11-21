
package com.arturkowalczyk300.mvvmlivedataexampleproject;

import java.util.List;
import com.google.gson.annotations.Expose;

@SuppressWarnings("unused")
public class WeatherReadingFromApi {

    @Expose
    private String base;
    @Expose
    private Clouds clouds;
    @Expose
    private Long cod;
    @Expose
    private Coord coord;
    @Expose
    private Long dt;
    @Expose
    private Long id;
    @Expose
    private Main main;
    @Expose
    private String name;
    @Expose
    private Sys sys;
    @Expose
    private Long timezone;
    @Expose
    private Long visibility;
    @Expose
    private List<Weather> weather;
    @Expose
    private Wind wind;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Long getCod() {
        return cod;
    }

    public void setCod(Long cod) {
        this.cod = cod;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public Long getTimezone() {
        return timezone;
    }

    public void setTimezone(Long timezone) {
        this.timezone = timezone;
    }

    public Long getVisibility() {
        return visibility;
    }

    public void setVisibility(Long visibility) {
        this.visibility = visibility;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

}
