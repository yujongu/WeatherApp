package com.yujongu.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    RequestQueue rq;
    JsonObjectData jsonData = new JsonObjectData();

    TextView textview_name, textview_country, textview_main, textview_description, textview_icon, textview_temp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
