package com.yujongu.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.yujongu.weatherapp.databinding.ActivityFirstpageBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Firstpage extends AppCompatActivity {

    ArrayList<Firstpage_Data> mArrayList;
    LinkedHashMap<String, LatLng> nSaveList;
    Firstpage_Adapter mAdapter;
    Firstpage_Data deleted = null;
    AutocompleteSupportFragment autocompleteFragment;
    ActivityFirstpageBinding binding;
    String TAG = "FirstPageT";
    String searchedCity = "";
    LatLng searchedLatLng;
    SharedPreferences pref;
    LinearLayoutManager mLinearLayoutManager;

    RequestQueue rq;
    JsonObjectData jsonData = new JsonObjectData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_firstpage);
        binding.setActivity(this);

        initInstances();
        eventListeners();

    }



    private void initInstances(){
        putAds();

        pref = getSharedPreferences("pref", MODE_PRIVATE);

        rq = Volley.newRequestQueue(this);

        if (!Places.isInitialized()){
            Places.initialize(getApplicationContext(), OpenWeatherAppKey.GOOGLE_PLACES_APP_KEY);
        }

        PlacesClient placesClient = Places.createClient(this);
        mLinearLayoutManager = new LinearLayoutManager(this);

        nSaveList = loadData();

        mArrayList = new ArrayList<>();

        mAdapter = new Firstpage_Adapter(mArrayList);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.recyclerviewFirstpage.getContext(),
                mLinearLayoutManager.getOrientation());
        binding.recyclerviewFirstpage.addItemDecoration(dividerItemDecoration);
        rvItemTouchHelper();
        setupAutocompleteFrag();


    }

    private void eventListeners(){
        binding.recyclerviewFirstpage.setLayoutManager(mLinearLayoutManager);
        binding.imagebuttonAdd.setOnClickListener(onClickListener);
        binding.recyclerviewFirstpage.setAdapter(mAdapter);
        binding.buttonC.setOnClickListener(onClickListener);
        binding.buttonF.setOnClickListener(onClickListener);

        if (!nSaveList.isEmpty()){
            int ind = 0;
            for (Map.Entry<String, LatLng> entry : nSaveList.entrySet()){
                sentJsonRequest(entry.getKey(), entry.getValue(), ind);
                ind++;
            }
        }
    }

    private void putAds(){
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }


    public void sentJsonRequest(final String inputName, final LatLng latLng, final int index){
        String url = "http://api.openweathermap.org/data/2.5/weather?"
                + "appid=" + OpenWeatherAppKey.OPEN_WEATHER_APP_KEY
                + "&lat=" + latLng.latitude
                + "&lon=" + latLng.longitude
                + "&units=metric";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String name = response.getString("name");
                    jsonData.setName(name);

                    JSONObject sys = response.getJSONObject(("sys"));
                    String country = sys.getString("country");
                    jsonData.setCountry(country);

                    JSONObject coord = response.getJSONObject("coord");
                    double lat = coord.getDouble("lat");
                    double lon = coord.getDouble("lon");
                    jsonData.setLat(lat);
                    jsonData.setLon(lon);

                    JSONArray weather = response.getJSONArray("weather");
                    JSONObject weatherObject = weather.getJSONObject(0);
                    String main = weatherObject.getString("main");
                    jsonData.setMain(main);

                    String description = weatherObject.getString("description");
                    jsonData.setDescription(description);

                    String icon = weatherObject.getString("icon");
                    jsonData.setIcon(icon);

                    JSONObject jsonMain = response.getJSONObject("main");
                    double tempC = Double.parseDouble(String.format("%.1f",jsonMain.getDouble("temp")));
                    jsonData.setTempC(tempC);

                    double tempF = Double.parseDouble(String.format("%.1f",((jsonMain.getDouble("temp")*9/5)+32)));
                    jsonData.setTempF(tempF);

                    int humidity = jsonMain.getInt("humidity");
                    jsonData.setHumidity(humidity);

                    JSONObject jsonWind = response.getJSONObject("wind");
                    double windspeed = jsonWind.getDouble("speed");
                    jsonData.setWindspeed(windspeed);

                    double max = jsonMain.getDouble("temp_max");
                    jsonData.setMax(max);


                    double min = jsonMain.getDouble("temp_min");
                    jsonData.setMin(min);
                    
                    Firstpage_Data data = new Firstpage_Data(jsonData.getIcon(), jsonData.getName(), jsonData.getTempC(), jsonData.getTempF(), jsonData.getCountry(),
                            jsonData.getMain(), jsonData.getHumidity(), jsonData.getWindspeed(), jsonData.getMax(), jsonData.getMin(), jsonData.getLat(), jsonData.getLon());
                    data.setInputCityName(inputName);

                    if (index != -1){
                        if (mArrayList.size() > index){
                            mArrayList.add(index, data);
                        } else {
                            mArrayList.add(data);
                        }
                    } else {
                        mArrayList.add(data);
                    }

                    if (!nSaveList.containsKey(inputName)){
                        nSaveList.put(inputName, latLng);
                        saveData(nSaveList);
                    }

                    mAdapter.notifyDataSetChanged();

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Firstpage.this, "Sorry we couldn't find the weather for the city...", Toast.LENGTH_LONG).show();
            }
        });
        rq.add(jsonObjectRequest);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imagebutton_add:
                    int index = isPresent(searchedCity);
                    if (index != -1){
                        mAdapter.presentInt = index;
                        mAdapter.notifyDataSetChanged();

                        searchedCity = "";
                        autocompleteFragment.setText("");

                    } else {
                        if (!searchedCity.equals("")){

                            sentJsonRequest(searchedCity, searchedLatLng, -1);
                            searchedCity = "";
                            searchedLatLng = null;
                            autocompleteFragment.setText("");
                        } else {
                            Toast.makeText(Firstpage.this, "Please search a city name", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case R.id.button_c:
                    binding.buttonF.setTextColor(getColor(R.color.colorPrimaryDark));
                    binding.buttonC.setTextColor(Color.BLACK);
                    mAdapter.celcius = true;
                    mAdapter.notifyDataSetChanged();
                    break;
                case R.id.button_f:
                    binding.buttonF.setTextColor(Color.BLACK);
                    binding.buttonC.setTextColor(getColor(R.color.colorPrimaryDark));
                    mAdapter.celcius = false;
                    mAdapter.notifyDataSetChanged();
                    break;

            }
        }
    };

    private int isPresent(String city){
        for (int i = 0; i < mArrayList.size(); i++){
            if (mArrayList.get(i).getCity().equals(city)){
                return i;
            }
        }
        return -1;
    }

    private void saveData(LinkedHashMap<String, LatLng> map) {
        SharedPreferences.Editor editor = pref.edit();
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, LatLng> entry : map.entrySet()){
            jsonArray.put(entry.getKey());
            jsonArray.put(entry.getValue());
        }
        if (!map.isEmpty()){
            editor.putString("nameList", jsonArray.toString());
        } else {
            editor.putString("nameList", null);
        }
        editor.apply();
    }

    private LinkedHashMap<String, LatLng> loadData(){
        String json = pref.getString("nameList", null);
        LinkedHashMap<String, LatLng> nameList = new LinkedHashMap<>();
        if (json != null){
            try {
                JSONArray jsonArray = new JSONArray(json);
                Log.i(TAG, json);
                for (int i = 0; i < jsonArray.length(); i+=2){
                    String[] latlong =  jsonArray.get(i + 1).toString().split(",");
                    latlong[0] = latlong[0].replace("lat/lng: (", "");
                    latlong[1] = latlong[1].replace(")", "");

                    double latitude = Double.parseDouble(latlong[0]);
                    double longitude = Double.parseDouble(latlong[1]);
                    LatLng hold = new LatLng(latitude, longitude);
                    nameList.put((String) jsonArray.get(i), hold);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return nameList;
    }


    private void setupAutocompleteFrag(){
        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setTypeFilter(TypeFilter.REGIONS);

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                searchedCity = place.getName();
                searchedLatLng = place.getLatLng();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }



    private void rvItemTouchHelper(){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP| ItemTouchHelper.DOWN
                |ItemTouchHelper.START | ItemTouchHelper.END,  ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                Collections.swap(mArrayList, fromPosition, toPosition);
                recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);

                nSaveList.clear();

                for (int i = 0; i < mArrayList.size(); i++){
                    nSaveList.put(mArrayList.get(i).getInputCityName(), new LatLng(mArrayList.get(i).getLatitude(), mArrayList.get(i).getLongitude()));
                }

                saveData(nSaveList);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                switch(direction){
                    case ItemTouchHelper.LEFT:
                        deleted = mArrayList.get(position);
                        mArrayList.remove(position);
                        mAdapter.notifyItemRemoved(position);

                        nSaveList.remove(deleted.getInputCityName());
                        saveData(nSaveList);
                        break;
                }
            }
            @Override
            public void onChildDraw (@NonNull Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(Firstpage.this, R.color.colorAccent))
                        .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.recyclerviewFirstpage);
    }

}
