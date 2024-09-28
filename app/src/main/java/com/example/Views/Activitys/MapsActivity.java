package com.example.Views.Activitys;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.example.btl_libary.R;
import com.example.btl_libary.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng uttHanoi = new LatLng(20.98512, 105.79899);
        mMap.addMarker(new MarkerOptions().position(uttHanoi).title("Đại học Công nghệ Giao thông Vận tải"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uttHanoi, 15));
    }
}
