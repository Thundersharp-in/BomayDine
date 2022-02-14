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
import com.thundersharp.admin.ui.edits.CategoryAddEdit;
import com.thundersharp.admin.ui.menu.CategoryResults;

import java.util.HashMap;
import java.util.List;

public class CategoryAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 0;

    List<Object> itemObjectlist;
    Context context;
    private int addedPos;

    public CategoryAdapter(){}

    public CategoryAdapter(List<Object> itemObjectlist, Context context) {
        this.itemObjectlist = itemObjectlist;
        this.context = context;
    }

    public void addLastItem(Object object ,int position){
        this.addedPos = position;
        itemObjectlist.add(object);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder view = null;

        switch (viewType){
            case 1:
                View viewq = LayoutInflater.from(context).inflate(R.layout.add_new_cat,parent,false);
                view = new CategoryAdapter.ViewHolderOther(viewq);
                break;
            case 0:
                View viewqw = LayoutInflater.from(context).inflate(R.layout.universalcatholder_admin,parent,false);
                view = new CategoryAdapter.ViewHolder(viewqw);
                break;
        }


        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            HashMap<String, String> hashMap = (HashMap<String, String>) itemObjectlist.get(position);

            ((ViewHolder)holder).name.setText(hashMap.get("NAME"));
            Glide.with(context).load(hashMap.get("IMAGES")).into(((ViewHolder)holder).imageView);
        }
    }

    @Override
    public int getItemCount() {
        if (itemObjectlist != null) return itemObjectlist.size();else return 0;
    }

    @Override
    public int getItemViewType(int position) {


        if (itemObjectlist.get(position).equals("Add")) {
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }

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
            context.startActivity(new Intent(context, CategoryAddEdit.class)
                    .putExtra("data", categoryData)
                    .putExtra("isEdit",true));
        }
    }


    class ViewHolderOther extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ViewHolderOther(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            context.startActivity(new Intent(context, CategoryAddEdit.class)
                    .putExtra("isEdit",false));

        }
    }
}
