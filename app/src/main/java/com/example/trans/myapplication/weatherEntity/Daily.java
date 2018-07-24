package com.example.trans.myapplication.weatherEntity;

import java.io.Serializable;

public class Daily implements Serializable {

    private String date;
    private String week;
    private String sunrise;
    private NightWeather night;
    private DayWeather day;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public NightWeather getNight() {
        return night;
    }

    public void setNight(NightWeather night) {
        this.night = night;
    }

    public DayWeather getDay() {
        return day;
    }

    public void setDay(DayWeather day) {
        this.day = day;
    }







}

