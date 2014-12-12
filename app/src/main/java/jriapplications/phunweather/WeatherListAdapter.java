package jriapplications.phunweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import butterknife.ButterKnife;
import butterknife.InjectView;
import jriapplications.phunweather.Models.Weather;

/**
 * Created by jisturiz on 12/10/14.
 *
 */
public class WeatherListAdapter extends BaseAdapter {
    private ArrayList<Weather> mWeatherList = new ArrayList<Weather>();
    private LayoutInflater mLayoutInflater;

    public WeatherListAdapter(Context context, Collection<Weather> weatherList){
        mWeatherList.addAll(weatherList);
        Collections.sort(mWeatherList);

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mWeatherList.size();
    }

    @Override
    public Weather getItem(int position) {
        return mWeatherList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Integer.valueOf(mWeatherList.get(position).getZipCode());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ConditionsHolder conditionsHolder;
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item, parent, false);
            conditionsHolder = new ConditionsHolder(convertView);

            if (convertView != null) {
                convertView.setTag(conditionsHolder);
            }
        } else {
            conditionsHolder = (ConditionsHolder) convertView.getTag();
        }

        Weather weather = mWeatherList.get(position);

        conditionsHolder.zipCode.setText(weather.getZipCode());

        if(weather.hasConditions()){
            conditionsHolder.progressBar.setVisibility(View.GONE);

            conditionsHolder.city.setText(weather.getCity());
        }

        return convertView;
    }

    public class ConditionsHolder {
        public ConditionsHolder(View view) {
            ButterKnife.inject(this, view);
        }
        @InjectView(R.id.progress_bar) ProgressBar progressBar;

        @InjectView(R.id.city) TextView city;
        @InjectView(R.id.zipCode) TextView zipCode;
    }
}
