package com.robby.mobile_05_20182.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author Robby
 */
public class MainWeatherResponse {

    @SerializedName("temp")
    private double temperature;
    @SerializedName("pressure")
    private double pressure;
    @SerializedName("humidity")
    private double humidity;
    @SerializedName("sea_level")
    private double seaLevel;

    public double getTemperature() {
        return temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getSeaLevel() {
        return seaLevel;
    }
}
