
package com.arturkowalczyk300.mvvmlivedataexampleproject.ModelApi;

import com.google.gson.annotations.Expose;

@SuppressWarnings("unused") //NON-NLS
public class Coord {

    @Expose
    private Double lat;
    @Expose
    private Double lon;

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

}
