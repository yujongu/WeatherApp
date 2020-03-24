package com.yujongu.weatherapp;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yujongu.weatherapp.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SendJsonRequest {

    JsonObjectData jsonData = new JsonObjectData();

    public JsonObjectData sentJsonRequest(RequestQueue rq){
        String url = "http://api.openweathermap.org/data/2.5/weather?q=Seoul&appid=a039162fa2fe09ab61cf60af7145fc65&units=metric";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String name = response.getString("name");
                    jsonData.setName(name);

                    JSONObject sys = response.getJSONObject(("sys"));
                    String country = sys.getString("country");
                    jsonData.setCountry(country);

                    JSONArray weather = response.getJSONArray("weather");
                    JSONObject weatherObject = weather.getJSONObject(0);
                    String main = weatherObject.getString("main");
                    jsonData.setMain(main);

                    String description = weatherObject.getString("description");
                    jsonData.setDescription(description);

                    String icon = weatherObject.getString("icon");
                    jsonData.setIcon(icon);

                    JSONObject jsonMain = response.getJSONObject("main");
                    double temp = jsonMain.getDouble("temp");
                    jsonData.setTemp(temp);


                    System.out.println(jsonData.getName());

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

        return jsonData;
    }


}
