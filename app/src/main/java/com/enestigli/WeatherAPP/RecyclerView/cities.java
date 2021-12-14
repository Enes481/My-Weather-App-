package com.enestigli.WeatherAPP.RecyclerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.enestigli.WeatherAPP.Adapter.Adapter;
import com.enestigli.WeatherAPP.City;
import com.enestigli.WeatherAPP.Main.MainActivity;
import com.enestigli.WeatherAPP.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class cities extends AppCompatActivity {

    RecyclerView recyclerView ;
    ArrayList<City> cityArrayList;
    Adapter cityadapter;
    ImageView cities_back_icon;



    public void init(){

        cities_back_icon = findViewById(R.id.Id_cities_back_icon);
        cities_back_icon_click_register();

        cityArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview_id);

        SQLGet_Data();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        init();


    }

    private void cities_back_icon_click_register(){

        cities_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cities.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void SQLGet_Data(){

        cityadapter = new Adapter(cityArrayList,this);

        try {

            SQLiteDatabase sqLiteDatabase = this.openOrCreateDatabase("City",MODE_PRIVATE,null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT*FROM city",null);
            int idIx = cursor.getColumnIndex("id");
            int nameIx = cursor.getColumnIndex("cityname");

            while(cursor.moveToNext()){
                String cityname = cursor.getString(nameIx);
                int id = cursor.getInt(idIx);
                City city = new City(cityname,id);
                cityArrayList.add(city);
            }
            cityadapter.notifyDataSetChanged();
            cursor.close();
        }

        catch (Exception e ){
           e.printStackTrace();
        }

        /*---------------------- set recyclerview-----------------------------*/


        recyclerView.setAdapter(cityadapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(cities.this));



    /*-------------------------- We drew a line between the data in the recyclerview ------------------------------------*/

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.custom_divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoration);

    }


}