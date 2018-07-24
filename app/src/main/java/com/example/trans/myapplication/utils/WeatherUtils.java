package com.example.trans.myapplication.utils;

import android.util.Log;

import com.example.trans.myapplication.R;
import com.example.trans.myapplication.weatherEntity.WeatherCity;
import com.google.gson.Gson;

public class WeatherUtils {

    private static final String TAG = "WeatherUtils";
    public static final String BUNDLE_WERTHERCITY="weatherCity";


    public static WeatherCity getWeatherCityByScuessJson(String jsonData) {
        Gson gson = new Gson();
        try {
            WeatherCity weatherCity = gson.fromJson(jsonData, WeatherCity.class);
            Log.d(TAG, "getWeatherCityByJson: " + weatherCity.toString());
            return weatherCity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param path img
     * @return 返回图像地址
     */
    public static int getDrawbleByImgPath(String path) {
        int index = Integer.valueOf(path);
        switch (index) {
            case 0:
                return R.drawable.weather_0;
            case 1:
                return R.drawable.weather_1;
            case 2:
                return R.drawable.weather_2;
            case 3:
                return R.drawable.weather_3;
            case 4:
                return R.drawable.weather_4;
            case 5:
                return R.drawable.weather_5;
            case 6:
                return R.drawable.weather_6;
            case 7:
                return R.drawable.weather_7;
            case 8:
                return R.drawable.weather_8;
            case 9:
                return R.drawable.weather_9;
            case 10:
                return R.drawable.weather_10;
            case 11:
                return R.drawable.weather_11;
            case 12:
                return R.drawable.weather_12;
            case 13:
                return R.drawable.weather_13;
            case 14:
                return R.drawable.weather_14;
            case 15:
                return R.drawable.weather_15;
            case 16:
                return R.drawable.weather_16;
            case 17:
                return R.drawable.weather_17;
            case 18:
                return R.drawable.weather_18;
            case 19:
                return R.drawable.weather_19;
            case 20:
                return R.drawable.weather_20;
            case 21:
                return R.drawable.weather_21;
            case 22:
                return R.drawable.weather_22;
            case 23:
                return R.drawable.weather_23;
            case 24:
                return R.drawable.weather_24;
            case 25:
                return R.drawable.weather_25;
            case 26:
                return R.drawable.weather_26;
            case 27:
                return R.drawable.weather_27;
            case 28:
                return R.drawable.weather_28;
            case 29:
                return R.drawable.weather_29;
            case 30:
                return R.drawable.weather_30;
            case 31:
                return R.drawable.weather_31;
            case 32:
                return R.drawable.weather_32;
            case 49:
                return R.drawable.weather_49;
            case 53:
                return R.drawable.weather_53;
            case 54:
                return R.drawable.weather_54;
            case 55:
                return R.drawable.weather_55;
            case 56:
                return R.drawable.weather_56;
            case 57:
                return R.drawable.weather_57;
            case 58:
                return R.drawable.weather_58;
            case 99:
                return R.drawable.weather_99;
            case 301:
                return R.drawable.weather_301;
            case 302:
                return R.drawable.weather_302;
        }
        return 0;
    }

}
