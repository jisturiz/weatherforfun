package jriapplications.phunweather.Models;

import android.util.Log;

/**
 * Created by jisturiz on 12/10/14.
 *
 */
public class Forecast {
    public enum When {
        TODAY, TOMORROW, TWO_DAYS_FROM_NOW, THREE_DAYS_FROM_NOW;
    }

    public SimpleForecast simpleforecast;

    private ForecastDay getToday(){
        return getDay(When.TODAY.ordinal());
    }

    private ForecastDay getDay(int period){
        return simpleforecast.forecastday.get(period);
    }

    private Date getDate(){
        return getToday().date;
    }

    public String getDateString() {
        Date date = getDate();
        return date.monthname + " " + date.day + ", " + date.year;
    }

    public String getWeekday() {
        return getDate().weekday;
    }

    public String getDayShort(int period) {
        Log.d("Hello There", "Period: " + period + " " + getDay(period).date.weekday_short);
        return getDay(period).date.weekday_short;
    }

    public String getIconUrl(int period){
        return getDay(period).icon_url;
    }


    public String getHigh(int period){
        return getDay(period).high.fahrenheit;
    }

    public String getLow(int period){
        return getDay(period).low.fahrenheit;
    }

}
