package com.thundersharp.admin.core.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thundersharp.admin.R;

import java.util.List;

public class GallaryAdapter extends RecyclerView.Adapter<GallaryAdapter.ViewHolder>{

    Context context;
    List<String> url;

    public GallaryAdapter(Context context, List<String> url) {
        this.context = context;
        this.url = url;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.galary,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(url.get(position)).into(holder.imageview);
    }

    @Override
    public int getItemCount() {
        if (url != null) return url.size();else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageview;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.imageview);
        }
    }
}
