package jriapplications.phunweather.Models;

/**
 * Created by jisturiz on 12/11/14.
 *
 */
public class WeatherUnderground {
    public WeatherUndergroundResponse response;

    public boolean responseOk(){
        return response.error == null;
    }

    public String getError(){
        return response.error.description;
    }
}
