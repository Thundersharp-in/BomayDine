package com.thundersharp.admin.core.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Model.SliderModel;
import com.thundersharp.admin.ui.gallary.UrlTransfer;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ViewHolder>{

    Context context;
    List<SliderModel> url;

    public SliderAdapter(Context context, List<SliderModel> url) {
        this.context = context;
        this.url = url;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.slider_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SliderModel sliderModel = url.get(position);

        Glide.with(context).load(sliderModel.URL).into(holder.imageview);
        holder.edit_img.setOnClickListener(view->{
            Toast.makeText(context, "Edit Clicked", Toast.LENGTH_SHORT).show();
        });
        holder.delete_img.setOnClickListener(view->{
            Toast.makeText(context, "Delete Clicked", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        if (url != null) return url.size();else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageview, edit_img, delete_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.imageview);
            edit_img = itemView.findViewById(R.id.edit_img);
            delete_img = itemView.findViewById(R.id.delete_img);

        }
    }
}
