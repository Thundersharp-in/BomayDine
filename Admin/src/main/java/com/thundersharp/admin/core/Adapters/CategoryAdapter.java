package com.thundersharp.admin.core.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Model.CategoryData;
import com.thundersharp.admin.ui.menu.CategoryResults;

import java.util.HashMap;
import java.util.List;

public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    List<Object> itemObjectlist;
    Context context;

    public CategoryAdapter(){}

    public CategoryAdapter(List<Object> itemObjectlist, Context context) {
        this.itemObjectlist = itemObjectlist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.universalcatholder_admin,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String,String> hashMap = (HashMap<String,String>)itemObjectlist.get(position);

        holder.name.setText(hashMap.get("NAME"));
        Glide.with(context).load(hashMap.get("IMAGES")).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (itemObjectlist != null) return itemObjectlist.size();else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageview);
            name = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            HashMap<String,String> hashMap = (HashMap<String,String>)itemObjectlist.get(getAdapterPosition());
            CategoryData categoryData = new CategoryData(hashMap.get("NAME"),hashMap.get("ID"),hashMap.get("IMAGES"));
            context.startActivity(new Intent(context, CategoryResults.class).putExtra("data", categoryData));
        }
    }
}
