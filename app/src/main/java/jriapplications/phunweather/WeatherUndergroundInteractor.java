package jriapplications.phunweather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;

import jriapplications.phunweather.Models.Conditions;
import jriapplications.phunweather.Models.Forecast;
import jriapplications.phunweather.Models.ForecastWrapper;
import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 * Created by jisturiz on 12/10/14.
 *
 */
public class WeatherUndergroundInteractor {
    private static final String BASE_URL = "http://api.wunderground.com/api/4dedb2d8c85705f2";

    private Context mContext;
    private WeatherUndergroundService mWeatherUndergroundService;

    public WeatherUndergroundInteractor(Context context){
        mContext = context;

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setRequestInterceptor(getRequestInterceptor())
                .setConverter(new GsonConverter(new Gson()))
                .setClient(getClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mWeatherUndergroundService = restAdapter.create(WeatherUndergroundService.class);
    }

    private OkClient getClient() {
        File httpCacheDirectory = new File(mContext.getCacheDir(), "responses");

        Cache httpResponseCache = null;
        try {
            httpResponseCache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        } catch (IOException e) {
            Log.e("Retrofit", "Could not create http cache", e);
        }

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setCache(httpResponseCache);

        return new OkClient(okHttpClient);
    }

    private RequestInterceptor getRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                if(isNetworkActive()) {
                    int maxAge = 60; // read from cache for 1 minute
                    request.addHeader("Cache-Control", "public, max-age=" + maxAge);
                } else {
                    int maxStale = 60 * 60 * 24; // tolerate 1-day stale
                    request.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
                }
            }
        };
    }

    private boolean isNetworkActive() {
        if(mContext != null) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    public void getZipCodeConditions(String zipCode, Callback<Conditions> callback){
        mWeatherUndergroundService.getZipCodeConditions(zipCode, callback);
    }

    public void getZipCodeForecast(String zipCode, Callback<ForecastWrapper> callback){
        mWeatherUndergroundService.getZipCodeForecast(zipCode, callback);
    }
}
