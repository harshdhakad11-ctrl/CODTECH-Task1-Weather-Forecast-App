package com.dhakad.weatherapp.models;

public class WeeklyModel {

    private String day;
    private String condition;
    private String maxTemp;
    private String minTemp;
    private String icon;

    public WeeklyModel(String day,
                       String condition,
                       String maxTemp,
                       String minTemp,
                       String icon) {

        this.day = day;
        this.condition = condition;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.icon = icon;
    }

    public String getDay() {
        return day;
    }

    public String getCondition() {
        return condition;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public String getIcon() {
        return icon;
    }
}