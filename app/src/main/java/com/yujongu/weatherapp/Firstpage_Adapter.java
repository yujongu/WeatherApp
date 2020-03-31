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
        protected TextView textView_temperature;

        public CustomViewHolder(View v){
            super(v);
            this.imageview_weather = v.findViewById(R.id.weatherImage);
            this.textview_city = v.findViewById(R.id.textview_city);
            this.textView_temperature = v.findViewById(R.id.textview_temperature);
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

        viewholder.textView_temperature.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewholder.textView_temperature.setGravity(Gravity.CENTER);

        viewholder.textView_temperature.setText(String.valueOf(firstpage_dataArrayList.get(position).getTemperature()));

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
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != firstpage_dataArrayList ? firstpage_dataArrayList.size() : 0);
    }
}
