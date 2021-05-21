package com.thundersharp.bombaydine.user.core.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Model.OrederBasicDetails;
import com.thundersharp.bombaydine.user.core.utils.TimeUtils;

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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewBinder holder, int position) {
        OrederBasicDetails orederBasicDetails = ((DataSnapshot)data.get(position)).getValue(OrederBasicDetails.class);

        holder.orderid.setText("Order #"+orederBasicDetails.getOrderID());
        holder.el_address.setText(orederBasicDetails.getDelivery_address());
        holder.el_address.setOnClickListener(view -> {
            Uri gmmIntentUri = Uri.parse("google.navigation:q="+orederBasicDetails.getDeliveryCoOrdinates());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            context.startActivity(mapIntent);
        });
        holder.allitems.setText(orederBasicDetails.getItemsMain());
        holder.order_time.setText(TimeUtils.getTimeFromTimeStamp(orederBasicDetails.getOrderID()));
        holder.total_amat.setText("\u20B9"+orederBasicDetails.getTotalamt());

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        // Order status 0 : Payment not received            ::::   Delivery status : Not Delivered        //
        // Order status 1 : Payment successfully received   ::::   Delivery status : Food being prepared  //
        // Order status 2 : Payment successfully received   ::::   Delivery status : In transit           //
        // Order status 3 : Payment successfully received   ::::   Delivery status : Delivered            //
        // Order status 4 : Payment Failed                  ::::   Delivery status : Not Delivered        //
        // Order status 5 : Payment successfully refunded   ::::   Delivery status : Not Delivered        //
        // Order status 6 : Payment successfully received   ::::   Delivery status : Not Delivered        //
        ////////////////////////////////////////////////////////////////////////////////////////////////////

        switch (orederBasicDetails.getStatus()){
            case "0":
                holder.status.setText("Status : Payment pending");
                //holder.status.setTextColor();
                break;
            case "1":
                holder.status.setText("Status : Food being prepared");
                //holder.status.setTextColor(0);
                break;
            case "2":
                holder.status.setText("Status : Order in transit");
                //holder.status.setTextColor(0);
                break;
            case "3":
                holder.status.setText("Status : Delivered");
                //holder.status.setTextColor(R.color.green);
                break;
            case "4":
                holder.status.setText("Status : Payment failed");
                //holder.status.setTextColor(0);
                break;
            case "5":
                holder.status.setText("Status : Cancelled and refunded");
                //holder.status.setTextColor(0);
                break;
            case "6":
                holder.status.setText("Status : Payment received but not delivered contact support");
                //holder.status.setTextColor(0);
                break;
            default:
                holder.status.setText("Status : Unknown");
                //holder.status.setTextColor(0);
                break;

        }


    }

    @Override
    public int getItemCount() {
        if (data != null) return data.size(); else return 0;
    }

    class ViewBinder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView status,total_amat,order_time,allitems,el_address,orderid;

        public ViewBinder(@NonNull View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.status);
            total_amat = itemView.findViewById(R.id.total_amat);
            order_time = itemView.findViewById(R.id.order_time);
            allitems = itemView.findViewById(R.id.allitems);
            el_address = itemView.findViewById(R.id.el_address);
            orderid = itemView.findViewById(R.id.orderid);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {


        }
    }
}
