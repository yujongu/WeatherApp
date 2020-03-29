package com.yujongu.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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

public class Firstpage extends AppCompatActivity {

    ArrayList<Firstpage_Data> mArrayList;
    Firstpage_Adapter mAdapter;
    Firstpage_Data deleted = null;
    AutocompleteSupportFragment autocompleteFragment;
    ActivityFirstpageBinding binding;
    String TAG = "FirstPageT";
    String searchedCity = "";

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
        rq = Volley.newRequestQueue(this);

        if (!Places.isInitialized()){
            Places.initialize(getApplicationContext(), OpenWeatherAppKey.GOOGLE_PLACES_APP_KEY);
        }

        PlacesClient placesClient = Places.createClient(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mArrayList = new ArrayList<>();
        mAdapter = new Firstpage_Adapter( mArrayList);
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
    }


    public void sentJsonRequest(String name){
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

                    Toast.makeText(Firstpage.this, jsonData.getName() + "   " + jsonData.getTemp() + "°C", Toast.LENGTH_SHORT).show();


                    Firstpage_Data data = new Firstpage_Data(jsonData.getName(), jsonData.getTemp());
                    mArrayList.add(data);
                    mAdapter.notifyDataSetChanged();

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



    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.imagebutton_add:
                    if (!searchedCity.equals("")){

                        //데이터를 날씨 데이터 저장해서 mArrayList에 추가해주면 됨.
                       sentJsonRequest(searchedCity);


                        //얘내들을 sentJsonRequest onResponse에 넣어주면 되고.



                        searchedCity = "";
                        autocompleteFragment.setText("");

                    } else {
                        Toast.makeText(Firstpage.this, "Please search a city name", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };



    private void setupAutocompleteFrag(){
        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES);

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
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
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
