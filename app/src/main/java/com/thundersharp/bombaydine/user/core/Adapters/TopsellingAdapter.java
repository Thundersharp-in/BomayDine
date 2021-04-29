package com.thundersharp.bombaydine.user.core.Adapters;

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
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.ui.menu.AllItemsActivity;

import java.util.HashMap;
import java.util.List;

public class TopsellingAdapter extends RecyclerView.Adapter<TopsellingAdapter.ViewHolder> {

    Context context;
    List<Object> listr;
    boolean type = false;

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (type) return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.topsellingmain,parent,false));
        else return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.topselling,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String,String> hashMap = (HashMap<String, String>) listr.get(position);

        holder.name.setText(hashMap.get("NAME"));
        holder.amount.setText(hashMap.get("NOOFORDERS")+" Orders Past Month");
        Glide.with(context).load(hashMap.get("IMAGES")).into(holder.imageView);


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
}
