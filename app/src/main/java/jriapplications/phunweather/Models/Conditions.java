package jriapplications.phunweather.Models;

/**
 * Created by jisturiz on 12/10/14.
 *
 */
public class Conditions extends WeatherUnderground{
    public CurrentObservation current_observation;

    public float getTemperature(){
        return current_observation.temp_f;
    }

    public String getCity(){
        return current_observation.display_location.city;
    }

    public String getIconUrl(){
        return current_observation.icon_url;
    }

    public String getWeather(){
        return current_observation.weather;
    }
}
