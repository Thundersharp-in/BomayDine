package com.thundersharp.bombaydine.user.core.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thundersharp.bombaydine.R;

import java.util.List;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewBinder> {

    private Context context;
    private List<Object> data;

    public static RecentAdapter initialize(Context context, List<Object> data){
        return new RecentAdapter(context, data);
    }

    public RecentAdapter(Context context, List<Object> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewBinder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewBinder(LayoutInflater.from(context).inflate( R.layout.item_order_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewBinder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (data != null) return data.size(); else return 0;
    }

    class ViewBinder extends RecyclerView.ViewHolder {
        public ViewBinder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
