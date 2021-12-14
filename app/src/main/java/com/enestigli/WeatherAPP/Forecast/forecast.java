package com.enestigli.WeatherAPP.Forecast;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.enestigli.WeatherAPP.Adapter.ForecastAdapter;
import com.enestigli.WeatherAPP.Main.MainActivity;
import com.enestigli.WeatherAPP.Model.RecyclerviewModel;
import com.enestigli.WeatherAPP.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class forecast extends AppCompatActivity {

    ActivityResultLauncher<String> permissionLauncher;
    LocationManager locationManager;
    LocationListener locationListener;

    ImageView forecast_back_icon;

    RecyclerView recyclerviewforecast;
    String API_KEY_FORECAST;
    String URL_FORECAST;
    Double Latitude,Longitude;
    ArrayList<RecyclerviewModel> arrayList;
    ForecastAdapter forecastAdapter;

    List<Address> listAddress;
    TextView forecast_location;

    String cityname;



   public forecast(){
        super();

    }


    public void init(){
        arrayList = new ArrayList<>();

        forecast_back_icon = findViewById(R.id.forecast_back_icon);
        recyclerviewforecast = findViewById(R.id.RecyclerviewForecast);

        forecast_location = findViewById(R.id.forecast_location);

        get_forecast_data(Latitude,Longitude);

        forecast_back_icon();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        Intent intent = getIntent();
        Latitude = intent.getDoubleExtra("lat",0);
        Longitude = intent.getDoubleExtra("long",0);
        System.out.println("LATİTUDE "+Latitude+" Longitude "+Longitude);


        init();

        Geocoder gc = new Geocoder(forecast.this, Locale.getDefault());

        if(Latitude == 0 && Longitude == 0){
            forecast_location.setText("please click the find my location button to find out your real location!");
        }
        else{
            try {
                listAddress = gc.getFromLocation(Latitude,Longitude,1);
                forecast_location.setText(listAddress.get(0).getAddressLine(0));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }



    }

    public void forecast_back_icon(){
        forecast_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent IntentForecastToMain = new Intent(forecast.this,MainActivity.class);
                startActivity(IntentForecastToMain);
            }
        });
    }

    public void get_forecast_data(Double Lat, Double Long){

        //EXAMPLE URL
        //https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API%20key}
        //Balikesir 39.64917, 27.88611
        API_KEY_FORECAST = "c29ecfafd4a70caad8fee38d6054bfc7";
        URL_FORECAST = "https://api.openweathermap.org/data/2.5/onecall?lat="+Lat+"&lon="+Long+"&exclude=current,minutely,hourly,alerts&appid="+API_KEY_FORECAST;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
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

                forecastAdapter = new ForecastAdapter(arrayList);
                recyclerviewforecast.setAdapter(forecastAdapter);
                recyclerviewforecast.setLayoutManager(new LinearLayoutManager(forecast.this));
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        });
        queue.add(request);

    /*--------------------------------- We drew a line between the data in the recyclerview -----------------------------*/

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerviewforecast.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerviewforecast.addItemDecoration(dividerItemDecoration);

    }




}
