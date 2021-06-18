package com.thundersharp.admin.core.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Model.OrederBasicDetails;
import com.thundersharp.admin.core.orders.OrderContract;
import com.thundersharp.admin.core.orders.OrderHistoryProvider;
import com.thundersharp.admin.core.utils.TimeUtils;
import com.thundersharp.admin.ui.orders.OrderStatus;

import java.util.List;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewBinder> implements OrderContract.StatusSuccessFailure{

    private Context context;
    private List<DataSnapshot> objectList;

    public void addNew(DataSnapshot data) {
        objectList.add(data);
        notifyItemInserted(objectList.size() - 1);
    }

    public void upDateExisting(DataSnapshot data) {
        if (objectList.contains(data.child("orderID"))){

            int index = objectList.indexOf(data.child("orderID"));
            Toast.makeText(context,"True ",index).show();
            objectList.add(index,data);
            notifyItemChanged(index);
        }else {
            Toast.makeText(context,"False ",Toast.LENGTH_SHORT).show();
            //Toast.makeText(context,"Fal ",Toast.LENGTH_SHORT).show();
        }
    }

    public RecentAdapter(Context context, List<DataSnapshot> objectList) {
        this.context = context;
        this.objectList = objectList;
    }


    @NonNull
    @Override
    public ViewBinder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewBinder(LayoutInflater.from(context).inflate( R.layout.item_order_holder_admin,parent,false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewBinder holder, int position) {
        OrederBasicDetails orederBasicDetails = ((DataSnapshot)objectList.get(position)).getValue(OrederBasicDetails.class);

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

        holder.btn_approve.setOnClickListener(view->{
            OrderHistoryProvider
                    .getOrderInstance()
                    .setOnStatusSuccessFailureListner(this)
                    .setStatus(orederBasicDetails.getOrderID(),9,orederBasicDetails.getUid());
            holder.lower.setVisibility(View.GONE);
        });

        holder.btn_decline.setOnClickListener(view->{
            OrderHistoryProvider
                    .getOrderInstance()
                    .setOnStatusSuccessFailureListner(this)
                    .setStatus(orederBasicDetails.getOrderID(),7,orederBasicDetails.getUid());
            holder.lower.setVisibility(View.GONE);
        });

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
                holder.lower.setVisibility(View.VISIBLE);
                //holder.status.setTextColor();
                break;
            case "1":
                holder.lower.setVisibility(View.GONE);
                holder.status.setText("Status : Food being prepared");
                //holder.status.setTextColor(0);
                break;
            case "2":
                holder.lower.setVisibility(View.GONE);
                holder.status.setText("Status : Order in transit");
                //holder.status.setTextColor(0);
                break;
            case "3":
                holder.lower.setVisibility(View.GONE);
                holder.status.setText("Status : Delivered");
                //holder.status.setTextColor(R.color.green);
                break;
            case "4":
                holder.lower.setVisibility(View.GONE);
                holder.status.setText("Status : Payment failed");
                //holder.status.setTextColor(0);
                break;
            case "5":
                holder.lower.setVisibility(View.GONE);
                holder.status.setText("Status : Cancelled and refunded");
                //holder.status.setTextColor(0);
                break;
            case "6":
                holder.lower.setVisibility(View.GONE);
                holder.status.setText("Status : Payment received but not delivered contact support");
                //holder.status.setTextColor(0);
                break;
            case "8":
                holder.lower.setVisibility(View.VISIBLE);
                holder.status.setText("Status : Payment successfully received waiting for admin");
                //holder.status.setTextColor(0);
                break;
            default:
                holder.lower.setVisibility(View.GONE);
                holder.status.setText("Status : Unknown");
                //holder.status.setTextColor(0);
                break;

        }


    }

    @Override
    public int getItemCount() {
        if (objectList != null) return objectList.size(); else return 0;
    }

    @Override
    public void onSuccess(@NonNull Task<Void> task) {
        Toast.makeText(context, "Status Updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Exception e) {
        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    class ViewBinder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView status,total_amat,order_time,allitems,el_address,orderid;
        AppCompatButton btn_decline, btn_approve;
        LinearLayout lower;

        public ViewBinder(@NonNull View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.status);
            total_amat = itemView.findViewById(R.id.total_amat);
            order_time = itemView.findViewById(R.id.order_time);
            allitems = itemView.findViewById(R.id.allitems);
            el_address = itemView.findViewById(R.id.el_address);
            orderid = itemView.findViewById(R.id.orderid);

            btn_decline = itemView.findViewById(R.id.btn_decline);
            btn_approve = itemView.findViewById(R.id.btn_approve);

            lower = itemView.findViewById(R.id.lower);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            OrderStatus.showOrderStatus(context,((DataSnapshot)objectList.get(getAdapterPosition())).getValue(OrederBasicDetails.class));
        }
    }
}
