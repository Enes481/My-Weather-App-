package com.enestigli.WeatherAPP.Main;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.enestigli.WeatherAPP.AddCity.addcity;
import com.enestigli.WeatherAPP.Forecast.forecastCity;
import com.enestigli.WeatherAPP.Model.Model;
import com.enestigli.WeatherAPP.R;
import com.enestigli.WeatherAPP.RecyclerView.cities;
import com.enestigli.WeatherAPP.Forecast.forecast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    private Context context;
    String API_KEY;
    String URL;

    SQLiteDatabase database;
    RecyclerView recyclerView;

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    List<Address> addresses;
    String id;

    ActivityResultLauncher<String> permissionLauncher;
    public LocationManager locationManager;
    public LocationListener locationListener;
    public Location location;
    public LocationRequest locationRequest;
    public Button btn_find_location;
    public TextView txt_description;
    public TextView txt_temperature;
    public TextView txt_feels_like;
    public TextView txt_wind;
    public TextView txt_humadity;
    public TextView txt_pressure;
    public TextView txt_visibilty;
    public TextView txt_dew_point;
    public TextView txt_low_temp;
    public TextView txt_high_temp;
    public TextView txt_country;
    public TextView txt_addressline;
    ImageView img_weather;
    public double Longitude;
    public double Latitude;
    public ProgressBar progressBar;
    private FusedLocationProviderClient mFusedLocationClient;
    forecast forecastclass;

    public Geocoder gc;
    private Double Lat;
    private Double Long;
    List<Address> addresses2;
    String cityname;


    public void init() {

        gc = new Geocoder(getApplicationContext(), Locale.getDefault());

        this.context = MainActivity.this;

        database = openOrCreateDatabase("City", MODE_PRIVATE, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS city(id INTEGER PRIMARY KEY,cityname VARCHAR)");

        img_weather = findViewById(R.id.img_dew_point);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        btn_find_location = findViewById(R.id.btn_find_location);
        txt_temperature = findViewById(R.id.txt_temperature);
        txt_feels_like = findViewById(R.id.txt_feels_like_temp);
        txt_wind = findViewById(R.id.txt_wind_speed);
        txt_humadity = findViewById(R.id.txt_humadity_ratio);
        txt_pressure = findViewById(R.id.txt_pressure_ratio);
        txt_visibilty = findViewById(R.id.txt_visibility_ratio);
        txt_dew_point = findViewById(R.id.txt_dew_point);
        txt_low_temp = findViewById(R.id.txt_low_temp);
        txt_high_temp = findViewById(R.id.txt_high_temp);
        txt_addressline = findViewById(R.id.txt_addressline);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        btn_set_location();

        registerLauncher();

        get_cityname();


    }

    public void get_cityname() {

        try {
            Intent intent = getIntent();
            int id = intent.getIntExtra("citId", 0);
            Cursor cursor = database.rawQuery("SELECT * FROM city WHERE id = ?", new String[]{String.valueOf(id)});
            int cityIx = cursor.getColumnIndex("cityname");


            while (cursor.moveToNext()) {
                cityname = cursor.getString(cityIx);
                System.out.println(cityname);
                getLatlong(cityname);
                //get_Data_recyclerview(cityname);

            }
            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("where id ? error");
        }
    }

    private void getLatlong(String cityname){

        try {

            addresses2 =  gc.getFromLocationName(cityname,1);

            for(Address adr:addresses2){
                Lat  = adr.getLatitude();
                Long = adr.getLongitude();

            }
            if(Lat !=null && Long != null){
                get_Data_recyclerview(cityname);
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("warning !");
                builder.setIcon(R.drawable.warningicon);
                builder.setMessage("Please Enter corrext city name.");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "something went wrong", Toast.LENGTH_LONG).show();
        }


    }

    public void get_Data_recyclerview(String cityname) {

        Model model = new Model();
        //EXAMPLE URL
        //https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API%20key}
        //Balikesir 39.64917, 27.88611
        API_KEY = "c29ecfafd4a70caad8fee38d6054bfc7";
        URL = "https://api.openweathermap.org/data/2.5/weather?q=" + cityname + "&appid=" + API_KEY;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject object = response.getJSONObject("main");
                    model.setTemperature(object.getString("temp"));
                    model.setFeels_like(object.getString("feels_like"));
                    model.setHumadity(object.getString("humidity"));
                    model.setMax_temp(object.getString("temp_max"));
                    model.setMin_temp(object.getString("temp_min"));
                    model.setPressure(object.getString("pressure"));

                    Double temperature = Double.parseDouble(model.getTemperature()) - 273.15;
                    Double feels_like = Double.parseDouble(model.getFeels_like()) - 273.15;
                    Double temp_max = Double.parseDouble(model.getMax_temp()) - 273.15;
                    Double temp_min = Double.parseDouble(model.getMin_temp()) - 273.15;

                    txt_temperature.setText(temperature.toString().substring(0, 4) + "°");
                    txt_feels_like.setText(feels_like.toString().substring(0, 4) + "°");
                    txt_high_temp.setText(temp_max.toString().substring(0, 4) + "°");
                    txt_low_temp.setText(temp_min.toString().substring(0, 4) + "°");
                    txt_humadity.setText("%" + model.getHumadity());
                    txt_pressure.setText(model.getPressure() + "hPa");


                    txt_visibilty.setText(response.getString("visibility") + "m");
                    txt_addressline.setText(cityname);

                    JSONObject object_wind = response.getJSONObject("wind");
                    model.setWind_speed(object_wind.getString("speed"));
                    txt_wind.setText(model.getWind_speed() + "km/h");

                    JSONArray weather = response.getJSONArray("weather");
                    JSONObject weatherObject = weather.getJSONObject(0);
                    model.setMain(weatherObject.getString("description"));
                    txt_dew_point.setText(model.getMain());
                    model.setId(weatherObject.getString("id"));
                    id = model.getId();
                    load_weather_icon(id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        });
        queue.add(request);

    }


    private void btn_set_location() {

        btn_find_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestLocation();

            }
        });
    }


    private void requestLocation() {

            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                    dlg.setTitle("Permission");
                    dlg.setMessage("Permission Needed For GPS!");
                    dlg.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                        }
                    }).create().show();

                } else {
                    permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                }

            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 3000, MainActivity.this/*locationListener*/);

            }
        }

    private void registerLauncher(){
        permissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if(result) {
                            //permission granted
                            if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,3000,MainActivity.this/*locationListener*/);

                            }

                        } else {
                            //permission denied
                           Toast.makeText(MainActivity.this,"Permisson needed!",Toast.LENGTH_LONG).show();


                        }
                    }

                });
    }

    public void get_Data(double Latitude,double Longitude,List<Address> addresses) {

        Model model = new Model();
        //EXAMPLE URL
        //https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API%20key}
        //Balikesir 39.64917, 27.88611
        API_KEY = "c29ecfafd4a70caad8fee38d6054bfc7";
        URL = "https://api.openweathermap.org/data/2.5/weather?lat=" + Latitude + "&lon=" + Longitude + "&appid=" + API_KEY;
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject object = response.getJSONObject("main");
                    model.setTemperature(object.getString("temp"));
                    System.out.println("temp--->>>>"+model.getTemperature());
                    model.setFeels_like(object.getString("feels_like"));
                    model.setHumadity(object.getString("humidity"));
                    model.setMax_temp(object.getString("temp_max"));
                    model.setMin_temp(object.getString("temp_min"));
                    model.setPressure(object.getString("pressure"));

                    Double temperature = Double.parseDouble(model.getTemperature()) - 273.15;
                    Double feels_like = Double.parseDouble(model.getFeels_like()) - 273.15;
                    Double temp_max = Double.parseDouble(model.getMax_temp()) - 273.15;
                    Double temp_min = Double.parseDouble(model.getMin_temp()) - 273.15;

                    txt_temperature.setText(temperature.toString().substring(0, 4) + "°");
                    txt_feels_like.setText(feels_like.toString().substring(0, 4) + "°");
                    txt_high_temp.setText(temp_max.toString().substring(0, 4) + "°");
                    txt_low_temp.setText(temp_min.toString().substring(0, 4) + "°");
                    txt_humadity.setText("%" + model.getHumadity());
                    txt_pressure.setText(model.getPressure() + "hPa");


                    txt_visibilty.setText(response.getString("visibility") + "m");
                    txt_addressline.setText(addresses.get(0).getAddressLine(0));

                    JSONObject object_wind = response.getJSONObject("wind");
                    model.setWind_speed(object_wind.getString("speed"));
                    txt_wind.setText(model.getWind_speed() + "km/h");


                    JSONArray weather = response.getJSONArray("weather");
                    JSONObject weatherObject = weather.getJSONObject(0);
                    model.setMain(weatherObject.getString("description"));
                    txt_dew_point.setText(model.getMain());
                    model.setId(weatherObject.getString("id"));
                    id = model.getId();
                    load_weather_icon(id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
            }
        });
        queue.add(request);

    }

    public void load_weather_icon(String id){

        int id1 = Integer.valueOf(id);

        if(id1>=200 && id1 <= 232){
            img_weather.setBackgroundResource(R.drawable.thunderstorm);
        }
        else if(id1>=300 && id1<= 321){
            img_weather.setBackgroundResource(R.drawable.showerrain);
        }
        else if(id1>=500 && id1<= 504){
            img_weather.setBackgroundResource(R.drawable.rain);
        }
        else if(id1 == 511){
            img_weather.setBackgroundResource(R.drawable.snow);
        }
        else if(id1>=520 && id1<= 531){
            img_weather.setBackgroundResource(R.drawable.showerrain);
        }
        else if(id1>=600 && id1<= 622){
            img_weather.setBackgroundResource(R.drawable.snow);
        }
        else if(id1>=701 && id1<= 781){
            img_weather.setBackgroundResource(R.drawable.mist);
        }
        else if(id1 == 800 ){
            img_weather.setBackgroundResource(R.drawable.clearsky);
        }
        else if(id1 == 801){
            img_weather.setBackgroundResource(R.drawable.fewclouds);
        }
        else if(id1 == 802){
            img_weather.setBackgroundResource(R.drawable.scatteredclouds);
        }
        else if(id1 == 803){
            img_weather.setBackgroundResource(R.drawable.brokenclouds);
        }
        else if(id1 == 804){
            img_weather.setBackgroundResource(R.drawable.brokenclouds);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.addcity:
                Intent intent = new Intent(MainActivity.this, addcity.class);
                startActivity(intent);
                return true;

            case R.id.showCities:
                Intent intentTocities = new Intent(MainActivity.this, cities.class);
                startActivity(intentTocities);
                return true;

            case R.id.show_5dayforecast:

                    Double latitude = Latitude;
                    Double longitude = Longitude;
                    Intent intentToShow5DayForecast = new Intent(MainActivity.this,forecast.class);
                    intentToShow5DayForecast.putExtra("lat",latitude);
                    intentToShow5DayForecast.putExtra("long",longitude);
                    startActivity(intentToShow5DayForecast);
                    return true;

            case R.id.show5daycity:
                try {

                    if(cityname==null){
                        AlertDialog2();
                    }
                    else{
                        Intent intentTocityForecast = new Intent(MainActivity.this, forecastCity.class);
                        intentTocityForecast.putExtra("cityname",cityname);
                        startActivity(intentTocityForecast);
                    }
                }catch (Exception e){
                    Toast.makeText(MainActivity.this,"something went wrong",Toast.LENGTH_LONG).show();
                }




            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void AlertDialog(){
        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
        dlg.setMessage("You can show cities after adding the city!");
        dlg.setTitle("WARNING");
        dlg.setIcon(R.drawable.warningicon);
        dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this,"There are no city in the list ",Toast.LENGTH_LONG).show();
            }
        }).create().show();
    }

    public void AlertDialog2(){
        AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
        dlg.setMessage("Please select a city from the list");
        dlg.setTitle("EROR!");
        dlg.setIcon(R.drawable.warningicon);
        dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();

    }





    @Override
    public void onLocationChanged(@NonNull Location location) {
        Latitude = location.getLatitude();
        Longitude = location.getLongitude();

        Geocoder geocoder = new Geocoder(MainActivity.this);
        try {
            addresses = geocoder.getFromLocation(Latitude,Longitude,1);
            get_Data(Latitude,Longitude,addresses);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}