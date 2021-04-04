package com.thundersharp.bombaydine.user.core.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thundersharp.bombaydine.R;

import java.util.HashMap;
import java.util.List;

public class AllItemAdapter extends RecyclerView.Adapter<AllItemAdapter.ViewHolder> {

    List<Object> itemObjectlist;
    Context context;

    public AllItemAdapter(){}

    public AllItemAdapter(List<Object> itemObjectlist, Context context) {
        this.itemObjectlist = itemObjectlist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.uneversal_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String,String> hashMap = (HashMap<String,String>)itemObjectlist.get(position);

        holder.name.setText(hashMap.get("name"));
        Glide.with(context).load(hashMap.get("imageuri")).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (itemObjectlist != null) return itemObjectlist.size();else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageview);
            name = itemView.findViewById(R.id.name);
        }
    }
}
