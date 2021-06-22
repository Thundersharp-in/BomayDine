package com.thundersharp.admin.core.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.AdminHelpers;
import com.thundersharp.admin.core.Model.SliderModel;
import com.thundersharp.admin.core.utils.CONSTANTS;
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

        holder.delete_img.setOnClickListener(view->{
            FirebaseStorage
                    .getInstance()
                    .getReference("SliderImages/"+sliderModel.PAGE+".jpg")
                    .delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference(CONSTANTS.DATABASE_TOP_CAROUSEL)
                                    .child(String.valueOf(sliderModel.PAGE))
                                    .removeValue()
                                    .addOnCompleteListener(database_task -> {
                                        if (task.isSuccessful()){
                                            url.remove(position);
                                            notifyDataSetChanged();
                                            Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
                                        }else Toast.makeText(context, "DATABASE ERROR : "+database_task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    });

                        }else Toast.makeText(context, "Storage ERROR : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

    }

    @Override
    public int getItemCount() {
        if (url != null) return url.size();else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageview, delete_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.imageview);
            delete_img = itemView.findViewById(R.id.delete_img);

        }
    }
}
