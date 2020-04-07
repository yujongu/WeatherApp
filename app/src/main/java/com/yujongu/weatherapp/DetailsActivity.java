package com.yujongu.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

public class DetailsActivity extends AppCompatActivity {

    TextView textview_detail_cityname, textview_detail_countryname, textview_detail_temperature,
            textview_detail_humidity, textview_detail_windspeed, textview_detail_visibility,
            textview_detail_max, textview_detail_min;
    String cityname, countryname, temperature, main, humidity, windspeed, min, max;
    ImageView imageview_detail_weather;
    ImageButton imagebutton_menu;
    float x1, x2, y1, y2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        textview_detail_cityname = findViewById(R.id.textview_detail_cityname);
        textview_detail_countryname = findViewById(R.id.textview_detail_countryname);
        textview_detail_temperature = findViewById(R.id.textview_detail_temperature);
        imageview_detail_weather = findViewById(R.id.imageview_detail_weather);
        textview_detail_humidity = findViewById(R.id.textview_detail_humidity);
        textview_detail_windspeed = findViewById(R.id.textview_detail_windspeed);
        textview_detail_max = findViewById(R.id.textview_detail_max);
        textview_detail_min = findViewById(R.id.textview_detail_min);
        imagebutton_menu = findViewById(R.id.imagebutton_menu);

        imagebutton_menu.setClickable(true);
        imagebutton_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();

        cityname = intent.getStringExtra("city");
        textview_detail_cityname.setText(cityname);

        countryname = intent.getStringExtra("country");
        textview_detail_countryname.setText(countryname);

        temperature = intent.getStringExtra("temperature");
        textview_detail_temperature.setText(temperature + "°");

        main = intent.getStringExtra("main");
        if(main.equals("Clouds")){
            imageview_detail_weather.setImageResource(R.drawable.cloudy);
        }else if(main.equals("Rain")){
            imageview_detail_weather.setImageResource(R.drawable.rain);
        }else if(main.equals("Snow")){
            imageview_detail_weather.setImageResource(R.drawable.snow);
        }else{
            imageview_detail_weather.setImageResource(R.drawable.sunny);
        }

        humidity = intent.getStringExtra("humidity");
        textview_detail_humidity.setText(humidity+"%");

        windspeed = intent.getStringExtra("windspeed");
        textview_detail_windspeed.setText(windspeed + "m/s");

        max = intent.getStringExtra("temp_max");
        textview_detail_max.setText(max+ "°");

        min = intent.getStringExtra("temp_min");
        textview_detail_min.setText(min+ "°");
    }

    public boolean onTouchEvent (MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(x1<x2){
                    finish();
                }
                break;
        }
        return false;
    }

}
