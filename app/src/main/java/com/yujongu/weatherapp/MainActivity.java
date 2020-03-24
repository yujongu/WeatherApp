package com.yujongu.weatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yujongu.weatherapp.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    RequestQueue rq;
    JsonObjectData jsonData = new JsonObjectData();
    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);

        rq = Volley.newRequestQueue(this);

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
                    binding.textviewName.setText(jsonData.getName());

                    JSONObject sys = response.getJSONObject(("sys"));
                    String country = sys.getString("country");
                    jsonData.setCountry(country);
                    binding.textviewCountry.setText(jsonData.getCountry());

                    JSONArray weather = response.getJSONArray("weather");
                    JSONObject weatherObject = weather.getJSONObject(0);
                    String main = weatherObject.getString("main");
                    jsonData.setMain(main);
                    binding.textviewMain.setText(jsonData.getMain());

                    String description = weatherObject.getString("description");
                    jsonData.setDescription(description);
                    binding.textviewDescription.setText(jsonData.getDescription());

                    String icon = weatherObject.getString("icon");
                    jsonData.setIcon(icon);
                    binding.textviewIcon.setText(jsonData.getIcon());

                    JSONObject jsonMain = response.getJSONObject("main");
                    double temp = jsonMain.getDouble("temp");
                    jsonData.setTemp(temp);
                    binding.textviewTemp.setText(jsonData.getTemp() + "");



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
}
