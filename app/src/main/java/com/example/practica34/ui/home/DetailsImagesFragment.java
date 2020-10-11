package com.example.practica34.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.practica34.R;


public class DetailsImagesFragment extends Fragment {

    TextView url,name;
    String a,b;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            a=getArguments().getString("name");
            b=getArguments().getString("url");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_details_images, container, false);

        url=root.findViewById(R.id.fragmetn_details_url);
        name=root.findViewById(R.id.fragmetn_details_name);

        url.setText(b);
        name.setText(a);

        return root;
    }
}