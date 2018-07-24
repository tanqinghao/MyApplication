package com.example.trans.myapplication.weatherEntity;

import java.io.Serializable;
import java.util.ArrayList;

public class WeatherCity implements Serializable {

    private String city;
    private String district;
    private String date;
    private String week;
    private String weather;
    private String temp;
    private String img;
    private String humidity;
    private String winddirect;
    private String windpower;
    private String ipm2_5;
    private String quality;
    private ArrayList<Daily> daily;
    private Yin yin;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

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

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWinddirect() {
        return winddirect;
    }

    public void setWinddirect(String winddirect) {
        this.winddirect = winddirect;
    }

    public String getWindpower() {
        return windpower;
    }

    public void setWindpower(String windpower) {
        this.windpower = windpower;
    }

    public String getIpm2_5() {
        return ipm2_5;
    }

    public void setIpm2_5(String ipm2_5) {
        this.ipm2_5 = ipm2_5;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public ArrayList<Daily> getDaily() {
        return daily;
    }

    public void setDaily(ArrayList<Daily> daily) {
        this.daily = daily;
    }

    public Yin getYin() {
        return yin;
    }

    public void setYin(Yin yin) {
        this.yin = yin;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", date='" + date + '\'' +
                ", week='" + week + '\'' +
                ", weather='" + weather + '\'' +
                ", temp='" + temp + '\'' +
                ", img='" + img + '\'' +
                ", humidity='" + humidity + '\'' +
                ", winddirect='" + winddirect + '\'' +
                ", windpower='" + windpower + '\'' +
                ", ipm2_5='" + ipm2_5 + '\'' +
                ", quality='" + quality + '\'' +
                ", daily=" + daily +
                ", yin=" + yin +
                '}';
    }
}
