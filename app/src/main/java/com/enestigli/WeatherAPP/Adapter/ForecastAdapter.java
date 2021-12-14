package com.enestigli.WeatherAPP.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.enestigli.WeatherAPP.Model.RecyclerviewModel;
import com.enestigli.WeatherAPP.R;
import com.enestigli.WeatherAPP.databinding.RecyclerviewForecastRowBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.MyForecastViewHolder> {

    ArrayList<RecyclerviewModel> ForecastArraylist;
    int count = 0;

    public ForecastAdapter(ArrayList<RecyclerviewModel> ForecastArraylist){
        this.ForecastArraylist = ForecastArraylist;

    }

    @NonNull
    @Override
    public MyForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerviewForecastRowBinding recyclerviewForecastRowBinding = RecyclerviewForecastRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MyForecastViewHolder(recyclerviewForecastRowBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyForecastViewHolder holder, int position) {


        Double temperature = Double.parseDouble(ForecastArraylist.get(position).getTemperature()) - 273.15;
        Double feels_like = Double.parseDouble(ForecastArraylist.get(position).getFeels_like()) - 273.15;

        Date date = new Date(ForecastArraylist.get(position).getDate()*1000);
        DateFormat dateFormat = new SimpleDateFormat("EEE, MMM yy", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone(ForecastArraylist.get(position).getTimeZone()));

        holder.binding.txtRecyclerviewDay.setText(dateFormat.format(date));
        holder.binding.txtRecyclerviewTemp.setText("temperature:" + temperature.toString().substring(0, 4) + "°");
        holder.binding.txtRecyclerviewFeelslike.setText("feels like:" + feels_like.toString().substring(0, 4) + "°");
        holder.binding.txtRecyclerviewHumidity.setText("humidity:%" + ForecastArraylist.get(position).getHumadity());
        holder.binding.txtRecyclerviewPressure.setText("pressure:" + ForecastArraylist.get(position).getPressure() + "hPa");
        holder.binding.txtRecyclerviewWindSpeed.setText("wind speed:" + ForecastArraylist.get(position).getWind_speed() + "km/h");
        holder.binding.txtRecyclerviewCloud.setText(ForecastArraylist.get(position).getDescription());

        int icon_id = ForecastArraylist.get(position).getId();
        load_weather_icon(icon_id, holder);


    }

    public void load_weather_icon(int id1,MyForecastViewHolder holder){

         if(id1>=200 && id1 <= 232){
            holder.binding.recyclerviewCloudImg.setBackgroundResource(R.drawable.thunderstorm);
        }
        else if(id1>=300 && id1<= 321){
            holder.binding.recyclerviewCloudImg.setBackgroundResource(R.drawable.showerrain);
        }
        else if(id1>=500 && id1<= 504){
            holder.binding.recyclerviewCloudImg.setBackgroundResource(R.drawable.rain);
        }
        else if(id1 == 511){
            holder.binding.recyclerviewCloudImg.setBackgroundResource(R.drawable.snow);
        }
        else if(id1>=520 && id1<= 531){
             holder.binding.recyclerviewCloudImg.setBackgroundResource(R.drawable.showerrain);
        }
        else if(id1>=600 && id1<= 622){
           holder.binding.recyclerviewCloudImg.setBackgroundResource(R.drawable.snow);
        }
        else if(id1>=701 && id1<= 781){
            holder.binding.recyclerviewCloudImg.setBackgroundResource(R.drawable.mist);
        }
        else if(id1 == 800 ){
            holder.binding.recyclerviewCloudImg.setBackgroundResource(R.drawable.clearsky);
        }
        else if(id1 == 801){
            holder.binding.recyclerviewCloudImg.setBackgroundResource(R.drawable.fewclouds);
        }
        else if(id1 == 802){
            holder.binding.recyclerviewCloudImg.setBackgroundResource(R.drawable.scatteredclouds);
        }
        else if(id1 == 803){
            holder.binding.recyclerviewCloudImg.setBackgroundResource(R.drawable.brokenclouds);
        }
        else if(id1 == 804){
            holder.binding.recyclerviewCloudImg.setBackgroundResource(R.drawable.brokenclouds);
        }

    }

    @Override
    public int getItemCount() {
        return ForecastArraylist.size();
    }

    public class MyForecastViewHolder extends RecyclerView.ViewHolder{
        private RecyclerviewForecastRowBinding binding;

        public MyForecastViewHolder(@NonNull RecyclerviewForecastRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }





}
