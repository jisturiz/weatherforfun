package jriapplications.phunweather.Models;

/**
 * Created by jisturiz on 12/10/14.
 *
 */
public class Weather implements Comparable<Weather>{
    private String mZipCode;
    private Conditions mConditions;
    private Forecast mForecast;

    public Weather(String zipCode){
        mZipCode = zipCode;
    }

    public Weather(String zipCode, Conditions conditions){
        mZipCode = zipCode;
        mConditions = conditions;
    }

    public boolean hasConditions(){
        return  mConditions != null;
    }

    public Conditions getConditions(){
        return mConditions;
    }

    public void setConditions(Conditions conditions){
        mConditions = conditions;
    }

    public boolean hasForecast(){
        return mForecast != null;
    }

    public Forecast getForecast(){
        return mForecast;
    }

    public void setForecast(Forecast forecast){
        mForecast = forecast;
    }

    public String getTemperature(){
        return Float.toString(mConditions.getTemperature());
    }

    public String getHighLow(Forecast.When when){
        return mForecast.getHigh(when.ordinal()) + "/" + mForecast.getLow(when.ordinal());
    }

    public String getExtras(){
        return mConditions.getWeather();
    }

    public String getIconUrl(){
        return mConditions.getIconUrl();
    }

    public String getIconUrl(Forecast.When when){
        return mForecast.getIconUrl(when.ordinal());

    }

    public String getZipCode(){
        return mZipCode;
    }

    public String getCity(){
        return mConditions.getCity();
    }


    public String getDay() {
        return mForecast.getWeekday();
    }

    public String getDayShort(Forecast.When when) {
        return mForecast.getDayShort(when.ordinal());
    }

    public String getDateString() {
       return mForecast.getDateString();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Weather && ((Weather) o).getZipCode().equals(mZipCode);
    }

    @Override
    public int compareTo(Weather another) {
        return Integer.valueOf(getZipCode()) - Integer.valueOf(another.getZipCode());
    }
}
