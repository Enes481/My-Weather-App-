package com.enestigli.WeatherAPP.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enestigli.WeatherAPP.City;
import com.enestigli.WeatherAPP.Main.MainActivity;
import com.enestigli.WeatherAPP.R;
import com.enestigli.WeatherAPP.databinding.RecyclerviewRowBinding;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>  {

    ArrayList<City> arrayList;
    Context context;
    SQLiteDatabase db ;
    Cursor cursor;
    int dbId;



    public Adapter(ArrayList<City> arrayList ,Context context ){

        this.arrayList = arrayList;
        this.context = context;

        db = context.openOrCreateDatabase("City",MODE_PRIVATE,null);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerviewRowBinding recyclerviewRowBinding = RecyclerviewRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyViewHolder(recyclerviewRowBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, int position) {

        holder.binding.MytxtCities.setText(arrayList.get(position).cityName);


        holder.itemView.setOnLongClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setIcon(R.drawable.warningicon);
            builder.setMessage("Are you sure that you want to delete "+arrayList.get(position).cityName);
            builder.setPositiveButton("Ok", (dialog, which) -> {



                /*----- We got the id of the city to be deleted from the database -----*/

                dbId = arrayList.get(position).id;

             /*-------- we deleted in arraylist ------*/

                arrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,arrayList.size());


                cursor = db.rawQuery("SELECT * FROM city WHERE id=?",new String[]{String.valueOf(dbId)});
                Result(cursor);


            }).setNegativeButton("Cancel", (dialog, which) -> {

                //doing nothing

            }).show();

            return true;
        });


        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(holder.itemView.getContext(),MainActivity.class);
            intent.putExtra("citId",arrayList.get(position).id);
            holder.itemView.getContext().startActivity(intent);
        });
    }


    private void Result(Cursor cursor){

            /*---- We deleted the city from SQL lite database ----*/

        if(cursor.getCount() > 0){

            db.delete("city","id=?",new String[]{String.valueOf(dbId)});
            notifyDataSetChanged();
        }
        else{
            Toast.makeText(context,"something went wrong !",Toast.LENGTH_LONG).show();
        }


    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Mytxt_cities;
        private RecyclerviewRowBinding binding;

        public MyViewHolder(@NonNull RecyclerviewRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            Mytxt_cities = itemView.findViewById(R.id.Mytxt_cities);



        }


    }
}
