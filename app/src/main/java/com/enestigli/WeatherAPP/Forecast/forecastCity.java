package com.enestigli.WeatherAPP.Forecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.enestigli.WeatherAPP.Adapter.ForecastAdapter;
import com.enestigli.WeatherAPP.Adapter.ForecastCıtyAdapter;
import com.enestigli.WeatherAPP.Main.MainActivity;
import com.enestigli.WeatherAPP.Model.RecyclerviewModel;
import com.enestigli.WeatherAPP.R;
import com.enestigli.WeatherAPP.databinding.ActivityForecastCityBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class forecastCity extends AppCompatActivity {

    private ActivityForecastCityBinding binding;
    private String cityname;
    private Double Lat;
    private Double Long;
    private String API_KEY_FORECAST;
    private String URL_FORECAST;
    private ArrayList<RecyclerviewModel> arrayList;
    private ForecastCıtyAdapter forecastCıtyAdapter;
    private List<Address> addresses;
    private  Geocoder gc;


    public void init(){

        Intent intent = getIntent();
        cityname = intent.getStringExtra("cityname");
        arrayList = new ArrayList<>();
        gc = new Geocoder(getApplicationContext(),Locale.getDefault());
        getLatlong(cityname);
        Back_icon_CITY();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForecastCityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        init();

    }

    private void Back_icon_CITY(){

        binding.ForecastBackIconCITY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(forecastCity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    private void getLatlong(String cityname){


        try {

            addresses = gc.getFromLocationName(cityname,1);

            for(Address adr:addresses){
                Lat  = adr.getLatitude();
                Long = adr.getLongitude();

            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(forecastCity.this, "something went wrong", Toast.LENGTH_LONG).show();
        }

        GetCityForecastData(Lat,Long);
    }


    private void GetCityForecastData(Double Lat, Double Long){

        if(Lat == 0 && Long == 0){
            binding.ForecastLocationCITY.setText("please click the find my location button to find out your real location!");
        }
        else{
            try {
                addresses = gc.getFromLocation(Lat,Long,1);
                binding.ForecastLocationCITY.setText(addresses.get(0).getAddressLine(0));
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Toast.makeText(forecastCity.this, "something went wrong", Toast.LENGTH_LONG).show();

            }
        }

        API_KEY_FORECAST = "c29ecfafd4a70caad8fee38d6054bfc7";
        URL_FORECAST = "https://api.openweathermap.org/data/2.5/onecall?lat="+Lat+"&lon="+Long+"&exclude=current,minutely,hourly,alerts&appid="+API_KEY_FORECAST;

        RequestQueue queue = Volley.newRequestQueue(forecastCity.this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_FORECAST, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray daily = response.getJSONArray("daily");
                    for(int i=0;i<daily.length();i++){

                        String TimeZone = response.getString("timezone");
                        Long date = daily.getJSONObject(i).getLong("dt");
                        String  temp = daily.getJSONObject(i).getJSONObject("temp").getString("day");
                        String feels_like = daily.getJSONObject(i).getJSONObject("feels_like").getString("day");
                        String pressure = daily.getJSONObject(i).getString("pressure");
                        String humidity = daily.getJSONObject(i).getString("humidity");
                        String wind_speed = daily.getJSONObject(i).getString("wind_speed");
                        int icon = daily.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getInt("id");
                        String description = daily.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description");
                        arrayList.add(new RecyclerviewModel(temp,humidity,feels_like,pressure,description,wind_speed,icon,date,TimeZone));

                    }

                }

                catch (JSONException e) {
                    e.printStackTrace();

                }

                /*----------------- recyclerview set -----------------*/

                forecastCıtyAdapter = new ForecastCıtyAdapter(arrayList);
                binding.RecyclerviewForecastCITY.setAdapter(forecastCıtyAdapter);
                binding.RecyclerviewForecastCITY.setLayoutManager(new LinearLayoutManager(forecastCity.this));
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        });
        queue.add(request);

        /*--------------------------------- We drew a line between the data in the recyclerview -----------------------------*/

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.RecyclerviewForecastCITY.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_divider);
        dividerItemDecoration.setDrawable(drawable);
        binding.RecyclerviewForecastCITY.addItemDecoration(dividerItemDecoration);

    }


}



