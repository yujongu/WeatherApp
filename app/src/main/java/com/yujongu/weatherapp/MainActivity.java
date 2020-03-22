package com.yujongu.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.yujongu.weatherapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);

        initInstances();
        eventListeners();

    }

    private void initInstances(){

        setContentView(R.layout.activity_main);


        rq = Volley.newRequestQueue(this);

        textview_name = findViewById(R.id.textview_name);
        textview_country = findViewById(R.id.textview_country);
        textview_main = findViewById(R.id.textview_main);
        textview_description = findViewById(R.id.textview_description);
        textview_icon = findViewById(R.id.textview_icon);
        textview_temp = findViewById(R.id.textview_temp);

        sentJsonRequest();
    }

    public void sentJsonRequest(){
        String url = "http://api.openweathermap.org/data/2.5/weather?q=Seoul&appid=a039162fa2fe09ab61cf60af7145fc65&units=metric";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String name = response.getString("name");
                    jsonData.setName(name);
                    textview_name.setText(jsonData.getName());

                    JSONObject sys = response.getJSONObject(("sys"));
                    String country = sys.getString("country");
                    jsonData.setCountry(country);
                    textview_country.setText(jsonData.getCountry());

                    JSONArray weather = response.getJSONArray("weather");
                    JSONObject weatherObject = weather.getJSONObject(0);
                    String main = weatherObject.getString("main");
                    jsonData.setMain(main);
                    textview_main.setText(jsonData.getMain());

                    String description = weatherObject.getString("description");
                    jsonData.setDescription(description);
                    textview_description.setText(jsonData.getDescription());

                    String icon = weatherObject.getString("icon");
                    jsonData.setIcon(icon);
                    textview_icon.setText(jsonData.getIcon());

                    JSONObject jsonMain = response.getJSONObject("main");
                    double temp = jsonMain.getDouble("temp");
                    jsonData.setTemp(temp);
                    textview_temp.setText(jsonData.getTemp() + "");



                }catch (JSONException e){
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        rq.add(jsonObjectRequest);
    }
    private void eventListeners(){
        binding.btnSearchCity.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btnSearchCity:
                    if (binding.etCitySearch.getVisibility() == View.INVISIBLE){
                        binding.etCitySearch.setVisibility(View.VISIBLE);
                    } else {
                        //search json
                        binding.etCitySearch.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
        }
    };

}
