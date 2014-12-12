package jriapplications.phunweather;

import android.app.Application;

import java.util.HashMap;

import jriapplications.phunweather.Models.Weather;

/**
 * Created by jisturiz on 12/11/14.
 *
 */
public class PhunWeather extends Application {

    HashMap<String, Weather> mWeatherMap;

    public HashMap<String, Weather> getApplicationWeatherMap(){
        if(mWeatherMap == null){
            mWeatherMap = new HashMap<String, Weather>();
        }

        return mWeatherMap;
    }
}
