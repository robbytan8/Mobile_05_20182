package com.robby.mobile_05_20182.entity;

import com.google.gson.annotations.SerializedName;

/**
 * @author Robby
 */
public class WeatherResponse2<T> {

    @SerializedName("name")
    private String name;
    @SerializedName("main")
    private T mainContent;

    public String getName() {
        return name;
    }

    public T getMainContent() {
        return mainContent;
    }
}
