package com.example.practica34.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculadora.R;
import com.example.calculadora.ip;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ImagesFragment extends Fragment implements AdapterImages.SendData{

    RecyclerView recImage;
    GridLayoutManager glmImage;
    AdapterImages adpImage;
    Context context;
    View redir;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        context=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_images, container, false);

        recImage=root.findViewById(R.id.images_recycler);
        glmImage=new GridLayoutManager(context,2);
        adpImage=new AdapterImages(context,this);
        recImage.setLayoutManager(glmImage);
        recImage.setAdapter(adpImage);

        load();
        redir=root;
        return root;
    }

    private void load() {
        SharedPreferences pref=getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        String token=pref.getString("token","");
        AsyncHttpClient client=new AsyncHttpClient();
        client.addHeader("token",token);
        client.get(ip.ip+"/up",null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                for(int i=0;i<response.length();i++){
                    try {
                        JSONObject obj=response.getJSONObject(i);
                        adpImage.add(new ItemImage(obj.getString("url"),obj.getString("name")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void sendDataImage(Bundle data) {
        Navigation.findNavController(redir).navigate(R.id.action_nav_images_to_nav_details,data);
    }
}