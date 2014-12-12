package jriapplications.phunweather;

import com.google.gson.JsonObject;

import jriapplications.phunweather.Models.Conditions;
import jriapplications.phunweather.Models.ForecastWrapper;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by jisturiz on 12/10/14.
 *
 */
public interface WeatherUndergroundService {
    @GET("/conditions/q/{zipCode}.json")
    void getZipCodeConditions(@Path("zipCode") String zipCode, Callback<Conditions> cb);

    @GET("/forecast/q/{zipCode}.json")
    void getZipCodeForecast(@Path("zipCode") String zipCode, Callback<ForecastWrapper> cb);
}
