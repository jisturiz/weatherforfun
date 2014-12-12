package jriapplications.phunweather;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jriapplications.phunweather.Models.Conditions;
import jriapplications.phunweather.Models.Forecast;
import jriapplications.phunweather.Models.ForecastWrapper;
import jriapplications.phunweather.Models.Weather;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by jisturiz on 12/10/14.
 *
 */
public class DetailsActivity extends Activity {
    public static final String ZIP_EXTRA = "zip_extra";

    @InjectView(R.id.progress_bar) ProgressBar mProgressBar;

    @InjectView(R.id.today) TextView mToday;
    @InjectView(R.id.date) TextView mDate;
    @InjectView(R.id.icon) ImageView mIcon;
    @InjectView(R.id.temperature) TextView mTemperature;
    @InjectView(R.id.highLow) TextView mHighLow;

    @InjectView(R.id.tomorrow_day) TextView mTomorrowDay;
    @InjectView(R.id.tomorrow_icon) ImageView mTomorrowIcon;
    @InjectView(R.id.tomorrow_highlow) TextView mTomorrowHighLow;

    @InjectView(R.id.day_two_day) TextView mDayTwoDay;
    @InjectView(R.id.day_two_icon) ImageView mDayTwoIcon;
    @InjectView(R.id.day_two_highlow) TextView mDayTwoHighLow;

    @InjectView(R.id.day_three_day) TextView mDayThreeDay;
    @InjectView(R.id.day_three_icon) ImageView mDayThreeIcon;
    @InjectView(R.id.day_three_highlow) TextView mDayThreeHighLow;

    @InjectView(R.id.city) TextView mCity;
    @InjectView(R.id.zipCode) TextView mZipCode;

    HashMap<String, Weather> mWeatherMap;
    Weather mWeather;
    WeatherUndergroundInteractor mWeatherUndergroundInteractor;

    boolean mBadQuery = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.inject(this);

        mWeatherMap = ((PhunWeather) getApplication()).getApplicationWeatherMap();
        mWeatherUndergroundInteractor = new WeatherUndergroundInteractor(this);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey(ZIP_EXTRA)){
            String zipCode = extras.getString(ZIP_EXTRA);

            getWeather(zipCode);
            showOrGetConditions();
            showOrGetForecast();
        }
    }

    private void getWeather(String zipCode) {
        if(mWeatherMap.containsKey(zipCode)) {
            mWeather = mWeatherMap.get(zipCode);
        } else {
            mWeather = new Weather(zipCode);
        }
    }

    private void showOrGetConditions() {
        if(mWeather.hasConditions()){
            showConditions();
        } else {
            getConditions();
        }
    }

    private void showConditions(){
        mZipCode.setText(mWeather.getZipCode());
        mCity.setText(mWeather.getCity());
        mTemperature.setText(mWeather.getTemperature() + getString(R.string.degree));

        showOrGetForecast();
    }

    private void showOrGetForecast() {
        if(mWeather.hasForecast()){
            showForecast();
        } else {
            getForecast(mWeather.getZipCode());
        }
    }

    private void showForecast(){
        mToday.setText(mWeather.getDay());
        mDate.setText(mWeather.getDateString());
        Picasso.with(this).load(mWeather.getIconUrl(Forecast.When.TODAY)).into(mIcon);

        mHighLow.setText(mWeather.getHighLow(Forecast.When.TODAY));

        mTomorrowDay.setText(mWeather.getDayShort(Forecast.When.TOMORROW));
        Picasso.with(this).load(mWeather.getIconUrl(Forecast.When.TOMORROW)).into(mTomorrowIcon);
        mTomorrowHighLow.setText(mWeather.getHighLow(Forecast.When.TOMORROW));

        mDayTwoDay.setText(mWeather.getDayShort(Forecast.When.TWO_DAYS_FROM_NOW));
        Picasso.with(this).load(mWeather.getIconUrl(Forecast.When.TWO_DAYS_FROM_NOW)).into(mDayTwoIcon);
        mDayTwoHighLow.setText(mWeather.getHighLow(Forecast.When.TWO_DAYS_FROM_NOW));

        mDayThreeDay.setText(mWeather.getDayShort(Forecast.When.THREE_DAYS_FROM_NOW));
        Picasso.with(this).load(mWeather.getIconUrl(Forecast.When.THREE_DAYS_FROM_NOW)).into(mDayThreeIcon);
        mDayThreeHighLow.setText(mWeather.getHighLow(Forecast.When.THREE_DAYS_FROM_NOW));
    }

    private void getConditions() {
        mWeatherUndergroundInteractor.getZipCodeConditions(mWeather.getZipCode(), new Callback<Conditions>() {
            @Override
            public void success(Conditions conditions, Response response) {
                mProgressBar.setVisibility(View.GONE);

                if(conditions.responseOk()){
                    mWeather.setConditions(conditions);
                    mWeatherMap.put(mWeather.getZipCode(), mWeather);

                    showConditions();
                } else {
                    Toast.makeText(DetailsActivity.this, conditions.getError(), Toast.LENGTH_LONG).show();
                    markBadQuery();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                //TODO: Handle error
                markBadQuery();
            }
        });
    }

    private void getForecast(final String zipCode){
        mWeatherUndergroundInteractor.getZipCodeForecast(zipCode, new Callback<ForecastWrapper>() {
            @Override
            public void success(ForecastWrapper forecastWrapper, Response response) {
                mProgressBar.setVisibility(View.GONE);

                if(forecastWrapper.responseOk()){
                    mWeather.setForecast(forecastWrapper.forecast);
                    mWeatherMap.put(mWeather.getZipCode(), mWeather);

                    showForecast();
                } else {
                    markBadQuery();
                    Toast.makeText(DetailsActivity.this, forecastWrapper.getError(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                //TODO: Handle Error
                markBadQuery();
            }
        });
    }

    private void markBadQuery(){
        if(mBadQuery) {
            finish(); //Both queries were faulty, go back to zipcode list
        } else {
            mBadQuery = true;
        }

    }
}
