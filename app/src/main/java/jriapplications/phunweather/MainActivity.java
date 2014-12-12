package jriapplications.phunweather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import jriapplications.phunweather.Models.Conditions;
import jriapplications.phunweather.Models.Weather;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends Activity {
    private static final String[] DEFAULT_ZIP_CODES = {"78757", "92660", "33137"};

    @InjectView(R.id.list)
    ListView mListView;

    private WeatherUndergroundInteractor mWeatherUndergroundInteractor;

    WeatherListAdapter mWeatherListAdapter;

    HashMap<String, Weather> mWeatherMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        mWeatherMap = ((PhunWeather) getApplication()).getApplicationWeatherMap();

        loadLatestZipCodes();

        mWeatherUndergroundInteractor = new WeatherUndergroundInteractor(this);

        mWeatherListAdapter = new WeatherListAdapter(this, mWeatherMap.values());

        mListView.setAdapter(mWeatherListAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDetails(Integer.toString((int)id));
            }
        });

        loadConditions();
    }

    private void loadConditions() {
        for(String zipCode: mWeatherMap.keySet()){
            if(!mWeatherMap.get(zipCode).hasConditions()){
                getConditionsForZipCode(zipCode);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadLatestZipCodes(){
        //TODO: Get from DB with City, State
        for(String zipCode : DEFAULT_ZIP_CODES) {
            if(!mWeatherMap.containsKey(zipCode)) {
                mWeatherMap.put(zipCode, new Weather(zipCode));
            }
        }
    }

    private void getConditionsForZipCode(final String zipCode) {
        mWeatherUndergroundInteractor.getZipCodeConditions(zipCode, new Callback<Conditions>() {
            @Override
            public void success(Conditions conditions, Response response) {
                if(conditions.responseOk()){
                    if(mWeatherMap.containsKey(zipCode)) {
                        mWeatherMap.get(zipCode).setConditions(conditions);
                    } else {
                        mWeatherMap.put(zipCode, new Weather(zipCode, conditions));
                        openDetails(zipCode);
                    }
                    mWeatherListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, conditions.getError(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void failure(RetrofitError error) {
                //TODO: Handle error
            }
        });
    }

    @OnClick(R.id.add_button)
    public void onClick() {
        //TODO: Add progress

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Add a new Zip Code")
                .setView(input)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String newZipCode = input.getText().toString();
                        if (newZipCode.length() < 5) {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.valid_zip), Toast.LENGTH_LONG).show();
                        } else {
                            openDetails(newZipCode);
                            //getConditionsForZipCode(newZipCode);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                }).show();
    }

    private void openDetails(String zipCode){
        Intent detailsIntent = new Intent(this, DetailsActivity.class);
        detailsIntent.putExtra(DetailsActivity.ZIP_EXTRA, zipCode);
        startActivity(detailsIntent);
    }
}
