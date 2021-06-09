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
import com.thundersharp.admin.ui.menu.AllItemsActivity;

import java.util.HashMap;
import java.util.List;

public class TopsellingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 0;

    Context context;
    List<Object> listr;
    boolean type = false;
    private int addedPos;

    public TopsellingAdapter(){}

    public TopsellingAdapter(Context context, List<Object> listr) {
        this.context = context;
        this.listr = listr;
    }

    public TopsellingAdapter(Context context, List<Object> listr, boolean type) {
        this.context = context;
        this.listr = listr;
        this.type = type;
    }

    public void addLastItem(Object object ,int position){
        this.addedPos = position;
        listr.add(object);
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder view = null;

        switch (viewType){
            case 1:
                View cce= null;
                if (type) cce =  LayoutInflater.from(context).inflate(R.layout.add_new_cat,parent,false);
                else cce = LayoutInflater.from(context).inflate(R.layout.add_new_cat_sized,parent,false);
                view = new TopsellingAdapter.ViewHolder(cce);
                break;
            case 0:
                View cc= null;
                if (type) cc =  LayoutInflater.from(context).inflate(R.layout.topsellingmain_admin,parent,false);
                else cc = LayoutInflater.from(context).inflate(R.layout.topselling_admin,parent,false);
                view = new TopsellingAdapter.ViewHolder(cc);
                break;
        }


        return view;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            HashMap<String, String> hashMap = (HashMap<String, String>) listr.get(position);

            ((ViewHolder)holder).name.setText(hashMap.get("NAME"));
            ((ViewHolder)holder).amount.setText(hashMap.get("NOOFORDERS") + " Orders Past Month");
            Glide.with(context).load(hashMap.get("IMAGES")).into(((ViewHolder)holder).imageView);
        }


    }

    @Override
    public int getItemViewType(int position) {


        if (listr.get(position).equals("Add")) {
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }

    }


    @Override
    public int getItemCount() {
        if (listr != null) return listr.size(); else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView name,amount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageview);
            amount = itemView.findViewById(R.id.amount);
            name = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            context.startActivity(new Intent(context, AllItemsActivity.class));
        }
    }


    class ViewHolderOther extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolderOther(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            context.startActivity(new Intent(context, AllItemsActivity.class));
        }
    }

}
