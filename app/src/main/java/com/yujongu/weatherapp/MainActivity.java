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
