package com.thundersharp.bombaydine.user.core.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thundersharp.bombaydine.R;

import java.util.List;

public class AllOfferAdapters extends RecyclerView.Adapter<AllOfferAdapters.View> {

    private static int adapterType= 0;

    public static AllOfferAdapters getInstance(Context context, List<Object> data,int AdapterType){
        adapterType=AdapterType;
        return new AllOfferAdapters(context,data);
    }

    private AllOfferAdapters(Context context, List<Object> objects) {
        this.context = context;
        this.objects = objects;
    }

    private Context context;
    private List<Object> objects;

    @NonNull
    @Override
    public View onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (adapterType == 0)
            return new View(LayoutInflater.from(context).inflate(R.layout.item_offer,parent,false));
        else return new View(LayoutInflater.from(context).inflate(R.layout.item_offer_all,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull View holder, int position) {

        if (adapterType == 0){

        }else {

        }

    }

    @Override
    public int getItemCount() {
        if (objects != null) return objects.size();else return 0;
    }

    class View extends RecyclerView.ViewHolder {
        public View(@NonNull android.view.View itemView) {
            super(itemView);
        }
    }
}
