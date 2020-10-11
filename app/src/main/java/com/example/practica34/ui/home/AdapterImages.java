package com.example.practica34.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.practica34.R;

import java.util.ArrayList;

public class AdapterImages extends RecyclerView.Adapter<AdapterImages.HolderImage>{

    Context context;
    ArrayList<ItemImage> mArray;
    SendData listen;

    AdapterImages(Context context, SendData listen){
        mArray=new ArrayList<>();
        this.context=context;
        this.listen=listen;
    }
    void add(ItemImage item){
        mArray.add(item);
        notifyItemInserted(mArray.indexOf(item));
    }

    @NonNull
    @Override
    public HolderImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,parent,false);
        HolderImage holder=new HolderImage(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderImage holder, int position) {
        final ItemImage it=mArray.get(position);
        Glide.with(context).load(it.getUrl()).into(holder.img);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("name",it.getName());
                bundle.putString("url",it.getUrl());
                listen.sendDataImage(bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArray.size();
    }

    class HolderImage extends RecyclerView.ViewHolder {
        ImageView img;
        View view;
        public HolderImage(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.item_image);
            view=itemView;
        }
    }

    interface SendData{
        void sendDataImage(Bundle data);
    }
}
