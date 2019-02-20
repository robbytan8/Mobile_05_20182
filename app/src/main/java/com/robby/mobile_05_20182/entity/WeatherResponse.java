package com.robby.mobile_05_20182.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author Robby
 */
public class WeatherResponse {

    @SerializedName("name")
    private String name;
    @SerializedName("main")
    private MainWeatherResponse main;

    public String getName() {
        return name;
    }

    public MainWeatherResponse getMain() {
        return main;
    }
}
