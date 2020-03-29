package com.yujongu.weatherapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Firstpage_Adapter extends RecyclerView.Adapter<Firstpage_Adapter.CustomViewHolder> {

    private ArrayList<Firstpage_Data> firstpage_dataArrayList;

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        protected TextView textview_city;
        protected TextView textView_temperature;

        public CustomViewHolder(View v){
            super(v);
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
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.textview_city.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewholder.textview_city.setGravity(Gravity.CENTER);

        viewholder.textview_city.setText(firstpage_dataArrayList.get(position).getCity());

        viewholder.textView_temperature.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewholder.textView_temperature.setGravity(Gravity.CENTER);

        viewholder.textView_temperature.setText(String.valueOf(firstpage_dataArrayList.get(position).getTemperature()));

    }

    @Override
    public int getItemCount() {
        return (null != firstpage_dataArrayList ? firstpage_dataArrayList.size() : 0);
    }
}
