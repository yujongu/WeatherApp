package com.yujongu.weatherapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Firstpage_Adapter extends RecyclerView.Adapter<Firstpage_Adapter.CustomViewHolder> {

    private ArrayList<Firstpage_Data> firstpage_dataArrayList;

    int presentInt = -1;


    public class CustomViewHolder extends RecyclerView.ViewHolder{
        protected ImageView imageview_weather;
        protected TextView textview_city;
        protected TextView textview_temperature;
        protected TextView textview_country;
        protected TextView textview_main;
        protected TextView textview_doubletemp;
        protected TextView textview_humidity;
        protected TextView textview_windspeed;
        protected TextView textview_max;
        protected TextView textview_min;


        public CustomViewHolder(View v){
            super(v);
            this.imageview_weather = v.findViewById(R.id.weatherImage);
            this.textview_city = v.findViewById(R.id.textview_city);
            this.textview_temperature = v.findViewById(R.id.textview_temperature);
            this.textview_country = v.findViewById(R.id.textview_country);
            this.textview_main = v.findViewById(R.id.textview_main);
            this.textview_doubletemp = v.findViewById(R.id.textview_doubletemp);
            this.textview_humidity = v.findViewById(R.id.textview_humidity);
            this.textview_windspeed = v.findViewById(R.id.textview_windspeed);
            this.textview_max = v.findViewById(R.id.textview_max);
            this.textview_min = v.findViewById(R.id.textview_min);

        }
    }

    public Firstpage_Adapter(ArrayList <Firstpage_Data> firstpage_dataArrayList){
        this.firstpage_dataArrayList = firstpage_dataArrayList;
    }


    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.firstpage_list,viewGroup,false);
        CustomViewHolder viewHolder = new CustomViewHolder(v);
        viewHolder.itemView.setBackgroundColor(Color.WHITE);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder viewholder, int position) {

        String url = "http://openweathermap.org/img/wn/" + firstpage_dataArrayList.get(position).getImageview_weather() + "@2x.png";
        Picasso.get().load(url).into(viewholder.imageview_weather);

        viewholder.itemView.setElevation(2);

        viewholder.textview_city.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewholder.textview_city.setGravity(Gravity.CENTER);

        viewholder.textview_city.setText(firstpage_dataArrayList.get(position).getCity());

        viewholder.textview_temperature.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewholder.textview_temperature.setGravity(Gravity.CENTER);

        viewholder.textview_temperature.setText(String.valueOf((int)firstpage_dataArrayList.get(position).getTemperature()));

        viewholder.textview_doubletemp.setText(String.valueOf(firstpage_dataArrayList.get(position).getTemperature()));

        viewholder.textview_temperature.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewholder.textview_temperature.setGravity(Gravity.CENTER);

        viewholder.textview_country.setText(String.valueOf(firstpage_dataArrayList.get(position).getCountry()));

        viewholder.textview_main.setText(String.valueOf(firstpage_dataArrayList.get(position).getMain()));

        viewholder.textview_humidity.setText(String.valueOf(firstpage_dataArrayList.get(position).getHumidity()));

        viewholder.textview_windspeed.setText(String.valueOf(firstpage_dataArrayList.get(position).getWindspeed()));

        viewholder.textview_max.setText(String.valueOf(firstpage_dataArrayList.get(position).getMax()));

        viewholder.textview_min.setText(String.valueOf(firstpage_dataArrayList.get(position).getMin()));



        if (position == presentInt){
            System.out.println(presentInt);
            ObjectAnimator anim = ObjectAnimator.ofObject(viewholder.itemView, "backgroundColor", new ArgbEvaluator(),
                    Color.LTGRAY, Color.WHITE);
            anim.setDuration(2000);
            anim.start();
            presentInt = -1;
        }

        viewholder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailsActivity.class);
                String city = viewholder.textview_city.getText().toString();
                String country = viewholder.textview_country.getText().toString();
                String temperature = viewholder.textview_doubletemp.getText().toString();
                String main = viewholder.textview_main.getText().toString();
                String humidity = viewholder.textview_humidity.getText().toString();
                String windspeed = viewholder.textview_windspeed.getText().toString();
                String max = viewholder.textview_max.getText().toString();
                String min = viewholder.textview_min.getText().toString();

                intent.putExtra("city", city);
                intent.putExtra("country", country);
                intent.putExtra("temperature", temperature);
                intent.putExtra("main", main);
                intent.putExtra("humidity", humidity);
                intent.putExtra("windspeed", windspeed);
                intent.putExtra("temp_max", max);
                intent.putExtra("temp_min", min);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != firstpage_dataArrayList ? firstpage_dataArrayList.size() : 0);
    }
}
