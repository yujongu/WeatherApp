package com.yujongu.weatherapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class Firstpage_Adapter extends RecyclerView.Adapter<Firstpage_Adapter.CustomViewHolder> {

    private ArrayList<Firstpage_Data> firstpage_dataArrayList;

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        protected TextView textview_city;

        public CustomViewHolder(View v){
            super(v);
            this.textview_city = v.findViewById(R.id.textview_city);
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

    }

    @Override
    public int getItemCount() {
        return (null != firstpage_dataArrayList ? firstpage_dataArrayList.size() : 0);
    }
}
