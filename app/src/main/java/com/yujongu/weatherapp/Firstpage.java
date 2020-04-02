package com.yujongu.weatherapp;

import android.content.SharedPreferences;
import android.graphics.Canvas;
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
import com.google.android.gms.common.api.Status;
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
import java.util.HashMap;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Firstpage extends AppCompatActivity {

    ArrayList<Firstpage_Data> mArrayList;
    HashMap<String, Integer> nameArrayList;
    Firstpage_Adapter mAdapter;
    Firstpage_Data deleted = null;
    AutocompleteSupportFragment autocompleteFragment;
    ActivityFirstpageBinding binding;
    String TAG = "FirstPageT";
    String searchedCity = "";
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
        pref = getSharedPreferences("pref", MODE_PRIVATE);

        rq = Volley.newRequestQueue(this);

        if (!Places.isInitialized()){
            Places.initialize(getApplicationContext(), OpenWeatherAppKey.GOOGLE_PLACES_APP_KEY);
        }

        PlacesClient placesClient = Places.createClient(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        nameArrayList = loadData();

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


        if (!nameArrayList.isEmpty()){
            for (Map.Entry<String, Integer> entry : nameArrayList.entrySet()){
                sentJsonRequest(entry.getKey(), entry.getValue());
            }
        }
    }


    public void sentJsonRequest(String name, final int index){
        String cityName = name;
        String url = "http://api.openweathermap.org/data/2.5/weather?"
                + "appid=" + OpenWeatherAppKey.OPEN_WEATHER_APP_KEY
                + "&q=" + cityName
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

                    int humidity = jsonMain.getInt("humidity");
                    jsonData.setHumidity(humidity);

                    JSONObject jsonWind = response.getJSONObject("wind");
                    double windspeed = jsonWind.getDouble("speed");
                    jsonData.setWindspeed(windspeed);

                    double max = jsonMain.getDouble("temp_max");
                    jsonData.setMax(max);

                    double min = jsonMain.getDouble("temp_min");
                    jsonData.setMin(min);


                    Firstpage_Data data = new Firstpage_Data(jsonData.getIcon(), jsonData.getName(), jsonData.getTemp(), jsonData.getCountry(),
                            jsonData.getMain(), jsonData.getHumidity(), jsonData.getWindspeed(), jsonData.getMax(), jsonData.getMin());
                    if (index != -1){
                        if (mArrayList.size() > index){
                            mArrayList.add(index, data);
                        } else {
                            mArrayList.add(data);
                        }
                    } else {
                        mArrayList.add(data);
                    }
                    if (!nameArrayList.containsKey(name)){
                        nameArrayList.put(name, nameArrayList.size());
                        saveData(nameArrayList);
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

                            sentJsonRequest(searchedCity, -1);

                            searchedCity = "";
                            autocompleteFragment.setText("");

                        } else {
                            Toast.makeText(Firstpage.this, "Please search a city name", Toast.LENGTH_SHORT).show();
                        }
                    }
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
    private void saveData(HashMap<String, Integer> arrayList) {
        SharedPreferences.Editor editor = pref.edit();
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, Integer> entry : arrayList.entrySet()){
            jsonArray.put(entry.getKey());
        }
        if (!arrayList.isEmpty()){
            editor.putString("nameList", jsonArray.toString());
        } else {
            editor.putString("nameList", null);
        }
        editor.apply();
    }

    private HashMap<String, Integer> loadData(){
        String json = pref.getString("nameList", null);
        HashMap<String, Integer> nameList = new HashMap<>();
        if (json != null){
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++){
                    nameList.put((String) jsonArray.get(i), i);
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
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setTypeFilter(TypeFilter.REGIONS);

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                searchedCity = place.getName();
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

                        nameArrayList.remove(deleted.getCity());
                        saveData(nameArrayList);
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
