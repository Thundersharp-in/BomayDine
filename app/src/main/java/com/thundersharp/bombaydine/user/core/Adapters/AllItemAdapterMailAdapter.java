package com.thundersharp.bombaydine.user.core.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.aligantnumber.ElegantNumberInteractor;
import com.thundersharp.bombaydine.user.core.aligantnumber.ElegentNumberHelper;

import java.util.List;

public class AllItemAdapterMailAdapter extends RecyclerView.Adapter<AllItemAdapterMailAdapter.ViewHolder> implements ElegantNumberInteractor.setOnTextChangeListner {

    public AllItemAdapterMailAdapter(){}

    List<Object> itemObjectlist;
    Context context;
    ElegentNumberHelper elegentNumberHelper;

    public AllItemAdapterMailAdapter(List<Object> itemObjectlist, Context context) {
        this.itemObjectlist = itemObjectlist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.item_food,parent,false);
        elegentNumberHelper = new ElegentNumberHelper(context,this,view);

        return new AllItemAdapterMailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        elegentNumberHelper.bindviewHolder(holder.initial,holder.finalview,R.id.minus,R.id.plus,R.id.displaytext,R.id.plusinit);
        elegentNumberHelper.getcurrentnumber();

    }

    @Override
    public int getItemCount() {
        if (itemObjectlist != null)return itemObjectlist.size();else return 0;
    }



    @Override
    public int OnTextChangeListner(int val) {
        return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout initial,finalview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            initial = itemView.findViewById(R.id.initial);
            finalview = itemView.findViewById(R.id.finl);
        }
    }
}
