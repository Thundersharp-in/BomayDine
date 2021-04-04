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

public class TopsellingAdapter extends RecyclerView.Adapter<TopsellingAdapter.ViewHolder> {

    Context context;
    List<Object> listr;

    public TopsellingAdapter(){}

    public TopsellingAdapter(Context context, List<Object> listr) {
        this.context = context;
        this.listr = listr;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.topselling,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String,String> hashMap = (HashMap<String, String>) listr.get(position);

        holder.name.setText(hashMap.get("name"));
        Glide.with(context).load(hashMap.get("imageuri")).into(holder.imageView);


    }

    @Override
    public int getItemCount() {
        if (listr != null) return listr.size(); else return 0;
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
