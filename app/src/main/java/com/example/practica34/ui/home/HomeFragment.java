package com.example.practica34.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.practica34.R;
import com.example.practica34.ip;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment implements View.OnClickListener{

    Context context;
    Button btn,load,send;
    ImageView img;
    static final int  CODE_PERMISSION=200;
    static final int CODE_GALERIA=100;
    String path;
    private HomeViewModel homeViewModel;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        btn=root.findViewById(R.id.to_recycler);
        load=root.findViewById(R.id.load_image);
        send=root.findViewById(R.id.send_image_api);
        img=root.findViewById(R.id.send_home_image);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_nav_home_to_nav_images);

            }
        });
        load.setOnClickListener(this);
        load.setVisibility(View.INVISIBLE);
        if(permission()){
            load.setVisibility(View.VISIBLE);
        }
        send.setOnClickListener(this);
        token();
        return root;
    }

    private boolean permission() {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if(this.getActivity().checkSelfPermission(Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED&&
                getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED&&
                getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            return true;
        }
        requestPermissions(new String[]{Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE},CODE_PERMISSION);
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(CODE_PERMISSION==requestCode){
            if(permissions.length==3){
                load.setVisibility(View.VISIBLE);
            }
        }
    }

    private void token() {
        SharedPreferences pref=getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        final SharedPreferences.Editor edit=pref.edit();
        AsyncHttpClient client=new AsyncHttpClient();

        client.get(ip.ip+"/up/token",null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String token=response.getString("token");
                    Toast.makeText(context, "token generado", Toast.LENGTH_SHORT).show();
                    edit.putString("token",token);
                    edit.commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.load_image:
                Intent in=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //in.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                in.setType("image/");
                startActivityForResult(Intent.createChooser(in,"Seleccione imagenes"),CODE_GALERIA);
                break;
            case R.id.send_image_api:
                enviar();
                break;
        }
    }
    private void enviar() {
        SharedPreferences pref=getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        String token=pref.getString("token","");
        AsyncHttpClient client=new AsyncHttpClient();
        client.addHeader("token",token);
        RequestParams req=new RequestParams();
        if(path!=null){
            File file=new File(path);
            try {
                req.put("img",file);
            }catch (Exception e){
                Toast.makeText(context, "error al insertar la imagen", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "inserte una imagen", Toast.LENGTH_SHORT).show();
            return;
        }
        client.post(ip.ip+"/up/upload",req,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(context, ""+response, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode==CODE_GALERIA&&resultCode==getActivity().RESULT_OK){
                if(data.getData()!=null){
                    Uri uri=data.getData();
                    path=getRealPath(context,uri);
                    img.setImageURI(uri);
                }else{
                    Toast.makeText(context, "error no hay imagen", Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception e){
            Toast.makeText(context, "error al leer la imagen", Toast.LENGTH_SHORT).show();
        }
    }

    String getRealPath(Context ctx, Uri uri){
        String path=null;
        Cursor cursor=ctx.getContentResolver().query(uri,null,null,null,null);
        if(cursor!=null){
            cursor.moveToFirst();
            int i=cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            path=cursor.getString(i);
            cursor.close();
        }

        return path;
    }
}