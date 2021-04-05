package com.thundersharp.bombaydine.user.core.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.aligantnumber.ElegantNumberInteractor;
import com.thundersharp.bombaydine.user.core.aligantnumber.ElegentNumberHelper;

import java.util.HashMap;
import java.util.List;

public class AllItemAdapter extends RecyclerView.Adapter<AllItemAdapter.ViewHolder>  implements ElegantNumberInteractor.setOnTextChangeListner {

    List<Object> itemObjectlist;
    Context context;
    ElegentNumberHelper elegentNumberHelper;

    public AllItemAdapter(){}

    public AllItemAdapter(List<Object> itemObjectlist, Context context) {
        this.itemObjectlist = itemObjectlist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.uneversal_holder,parent,false);
        elegentNumberHelper = new ElegentNumberHelper(context,this,view);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HashMap<String,String> hashMap = (HashMap<String,String>)itemObjectlist.get(position);

        elegentNumberHelper.bindviewHolder(holder.initial,holder.finalview,R.id.minus,R.id.plus,R.id.displaytext,R.id.plusinit);
        elegentNumberHelper.getcurrentnumber();

        holder.name.setText(hashMap.get("name"));
        Glide.with(context).load(hashMap.get("imageuri")).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (itemObjectlist != null) return itemObjectlist.size();else return 0;
    }

    @Override
    public int OnTextChangeListner(int val) {
        Toast.makeText(context,""+val,Toast.LENGTH_SHORT).show();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name;
        LinearLayout initial,finalview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageview);
            name = itemView.findViewById(R.id.name);

            initial = itemView.findViewById(R.id.initial);
            finalview = itemView.findViewById(R.id.finl);
        }
    }
}
