package com.enestigli.WeatherAPP.AddCity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.enestigli.WeatherAPP.Adapter.Adapter;
import com.enestigli.WeatherAPP.Main.MainActivity;
import com.enestigli.WeatherAPP.R;

public class addcity extends AppCompatActivity {

    SQLiteDatabase database;
    ImageView imageView;
    Button btn_add_city;
    EditText txt_add_city;
    String cityName;
    Adapter adapter;

    private void init(){

        imageView    = findViewById(R.id.img_backicon);
        txt_add_city = findViewById(R.id.txt_add_city);
        btn_add_city = findViewById(R.id.btn_add_city);
        database = openOrCreateDatabase("City",MODE_PRIVATE,null);
        iconback_register();
        btnaddcity_register();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcity);
        init();


    }

    private void btnaddcity_register(){

        btn_add_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityName = txt_add_city.getText().toString();
                if(cityName.equals("")){
                    AlertDialog.Builder dlg = new AlertDialog.Builder(addcity.this);
                    dlg.setMessage("You can not enter a empty value!");
                    dlg.setTitle("WARNING");
                    dlg.setIcon(R.drawable.warningicon);
                    dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(addcity.this,"Please enter city name",Toast.LENGTH_LONG).show();
                        }
                    }).create().show();
                }
                else{

                   try {

                       String sqlString = "INSERT INTO city(cityname) VALUES(?)";
                       SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
                       sqLiteStatement.bindString(1,cityName);
                       sqLiteStatement.execute();
                   }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(addcity.this,"Something went wrong!",Toast.LENGTH_LONG).show();
                   }

                   Intent intent = new Intent(addcity.this,MainActivity.class);
                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   Toast.makeText(addcity.this,"City has added succesfully",Toast.LENGTH_LONG).show();
                   startActivity(intent);


                }

            }
        });


    }

    private void iconback_register(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addcity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}