package com.example.restaurantmap;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.restaurantmap.Data.DatabaseHelper;
import com.example.restaurantmap.Model.Restaurant;
import com.example.restaurantmap.Util.Util;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.restaurantmap.databinding.ActivityMapsBinding;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String name;
    private double lat, lng;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        intent = getIntent();
        name = intent.getStringExtra(Util.KEY_RES_NAME);
        lat = intent.getDoubleExtra(Util.KEY_RES_LAT, 0);
        lng = intent.getDoubleExtra(Util.KEY_RES_LNG, 0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (intent.hasExtra(Util.KEY_SHOW_ALL_RES)) {
            DatabaseHelper db = new DatabaseHelper(this);
            List<Restaurant> restaurants = db.getRestaurants();
             if (restaurants.size() == 0)
                Toast.makeText(MapsActivity.this, " Please Save a location to continue", Toast.LENGTH_SHORT).show();
            else {
                // Iterate through the list and add marker for each restaurant
                for (int i = 0; i < restaurants.size(); i++) {
                    LatLng restaurant = new LatLng(restaurants.get(i).getLat(), restaurants.get(i).getLng());
                    mMap.addMarker(new MarkerOptions().position(restaurant).title(restaurants.get(i).getName()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurant, 10));
                }
            }
        }
        else {
            LatLng restaurant = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(restaurant).title(name));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurant, 15));
        }
    }
}