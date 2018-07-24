package com.example.trans.myapplication.weatherEntity;

import java.io.Serializable;

public class Yin implements Serializable {
    private String lunar_calendar;
    private String branch;
    private String zodiac;
    private String suitable;
    private String avoid;

    public String getLunar_calendar() {
        return lunar_calendar;
    }

    public void setLunar_calendar(String lunar_calendar) {
        this.lunar_calendar = lunar_calendar;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getZodiac() {
        return zodiac;
    }

    public void setZodoac(String zodiac) {
        this.zodiac = zodiac;
    }

    public String getSuitable() {
        return suitable;
    }

    public void setSuitable(String suitable) {
        this.suitable = suitable;
    }

    public String getAvoid() {
        return avoid;
    }

    public void setAvoid(String avoid) {
        this.avoid = avoid;
    }
}