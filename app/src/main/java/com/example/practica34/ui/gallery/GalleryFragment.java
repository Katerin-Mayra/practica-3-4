package com.example.practica34.ui.gallery;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.practica34.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GalleryFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    GoogleMap googleMap;
    Context context;
    Marker aux;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        SupportMapFragment supportMapFragment= (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
        return root;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }
    @Override
    public void onMapClick(LatLng latLng) {
        if(aux==null){
            Marker marker=googleMap.addMarker(new MarkerOptions().position(latLng).title("este es mi marker"));
            aux=marker;
        }else{
            aux.remove();
            Marker marker=googleMap.addMarker(new MarkerOptions().position(latLng).title("este es mi marker"));
            aux=marker;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;

        LatLng potosi = new LatLng(-19.578297,-65.758633);
        // mMap.addMarker(new MarkerOptions().position(potosi).title("potosi"));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(potosi,14));
        this.googleMap.setOnMapClickListener(this);

    }
}