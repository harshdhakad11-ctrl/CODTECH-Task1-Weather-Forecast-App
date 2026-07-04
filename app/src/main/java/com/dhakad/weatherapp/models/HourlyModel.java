package com.dhakad.weatherapp.models;

public class HourlyModel {

    private String time;
    private String temp;
    private String icon;

    public HourlyModel(String time, String temp, String icon) {
        this.time = time;
        this.temp = temp;
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public String getTemp() {
        return temp;
    }

    public String getIcon() {
        return icon;
    }

}