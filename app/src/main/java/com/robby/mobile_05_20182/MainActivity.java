package com.robby.mobile_05_20182;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.robby.mobile_05_20182.adapter.CityAdapter;
import com.robby.mobile_05_20182.entity.City;
import com.robby.mobile_05_20182.entity.WeatherResponse;
import com.robby.mobile_05_20182.entity.WeatherResponse2;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Robby
 */
public class MainActivity extends AppCompatActivity implements CityAdapter.DataClickListener {

    @BindView(R.id.rv_data)
    RecyclerView rvCity;
    private CityAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        DividerItemDecoration decoration = new DividerItemDecoration(this, manager.getOrientation());
        rvCity.addItemDecoration(decoration);
        rvCity.setLayoutManager(manager);
        rvCity.setAdapter(getCityAdapter());
        populateCityData();
    }

    public CityAdapter getCityAdapter() {
        if (cityAdapter == null) {
            cityAdapter = new CityAdapter();
            cityAdapter.setListener(this);
        }
        return cityAdapter;
    }

    private void populateCityData() {
        try {
            InputStream inputStream = getAssets().open("city.list.json");
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            Gson gson = new Gson();
            City[] cityArr = gson.fromJson(reader, City[].class);
            ArrayList<City> cities = new ArrayList<>(Arrays.asList(cityArr));
            Collections.sort(cities, new Comparator<City>() {
                @Override
                public int compare(City city, City t1) {
                    return city.getName().compareTo(t1.getName());
                }
            });
            getCityAdapter().setCities(cities);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void collectWeatherData(City city) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Uri uri = Uri.parse("https://api.openweathermap.org/data/2.5/weather").buildUpon()
                .appendQueryParameter("id", String.valueOf(city.getId()))
                .appendQueryParameter("appid", BuildConfig.ApiKey)
                .build();
        StringRequest request = new StringRequest(Request.Method.GET, uri.toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //  Using class inside another class (MainWeatherResponse inside WeatherResponse)
                    Gson gson = new Gson();
                    WeatherResponse weatherResponse = gson.fromJson(jsonObject.toString(), WeatherResponse.class);
                    Toast.makeText(MainActivity.this, weatherResponse.getName() + " " + weatherResponse.getMain().getHumidity(), Toast.LENGTH_SHORT).show();

                    //  With Deserialize class
                    Gson gson2 = new GsonBuilder()
                            .registerTypeAdapter(WeatherResponse2.class, new MyDeserializer<WeatherResponse2>())
                            .create();
                    WeatherResponse2 weatherResponse2 = gson2.fromJson(jsonObject.toString(), WeatherResponse2.class);
                    Toast.makeText(MainActivity.this, weatherResponse2.getName() + " " + weatherResponse2.getMainContent(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, R.string.response_error, Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }

    @Override
    public void onCityClickedListener(City city) {
        collectWeatherData(city);
    }

    static class MyDeserializer<T> implements JsonDeserializer<T> {
        @Override
        public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
                throws JsonParseException {
            JsonElement content = je.getAsJsonObject();
            return new Gson().fromJson(content, type);

        }
    }
}
