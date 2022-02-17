package com.thundersharp.admin.core.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Model.OrederBasicDetails;
import com.thundersharp.admin.core.orders.OrderContract;
import com.thundersharp.admin.core.orders.OrderHistoryProvider;
import com.thundersharp.admin.core.utils.TimeUtils;
import com.thundersharp.admin.ui.edits.EditItemActivity;
import com.thundersharp.admin.ui.orders.OrderStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ViewBinder> implements OrderContract.StatusSuccessFailure{

    private Context context;
    private List<DataSnapshot> objectList;
    private List<String> orderidS;

    public void addNew(DataSnapshot data) {
        objectList.add(data);
        if (orderidS == null) {
            orderidS = new ArrayList<>();

        }
        orderidS.add(data.child("orderID").getValue(String.class));
        notifyItemInserted(objectList.size() - 1);
    }

    public void upDateExisting(DataSnapshot data) {

        if (orderidS.contains(data.child("orderID").getValue(String.class))){

            int index = orderidS.indexOf(data.child("orderID").getValue(String.class));
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
        this.orderidS = new ArrayList<>();
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

        /*
        holder.btn_approve.setOnClickListener(view->{
            OrderHistoryProvider
                    .getOrderInstance()
                    .setOnStatusSuccessFailureListner(this)
                    .setStatus(orederBasicDetails.getOrderID(),9,orederBasicDetails.getUid());
            holder.lower.setVisibility(View.GONE);
            holder.btn_cancel.setVisibility(View.VISIBLE);
        });

        holder.btn_decline.setOnClickListener(view->{
            OrderHistoryProvider
                    .getOrderInstance()
                    .setOnStatusSuccessFailureListner(this)
                    .setStatus(orederBasicDetails.getOrderID(),7,orederBasicDetails.getUid());
            holder.lower.setVisibility(View.GONE);
            holder.btn_cancel.setVisibility(View.GONE);
        });

         */
        holder.btn_cancel.setOnClickListener(view->{
            OrderHistoryProvider
                    .getOrderInstance()
                    .setOnStatusSuccessFailureListner(this)
                    .setStatus(orederBasicDetails.getOrderID(),7,orederBasicDetails.getUid());
            holder.update_status.setVisibility(View.GONE);
            holder.btn_cancel.setVisibility(View.GONE);
        });

        if (TimeUtils.getTodaysDate().equals(TimeUtils.getDateFromTimeStamp(orederBasicDetails.getOrderID()))){
            holder.isToday = true;
            holder.update_status.setVisibility(View.VISIBLE);
            holder.btn_cancel.setVisibility(View.VISIBLE);
            holder.btn_update.setVisibility(View.VISIBLE);
        }else {
            holder.isToday = false;
            holder.update_status.setVisibility(View.GONE);
            holder.btn_cancel.setVisibility(View.GONE);
            holder.btn_update.setVisibility(View.GONE);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        // Order status 0 : Payment not received            ::::   Delivery status : Not Delivered        //
        // Order status 1 : Payment successfully received   ::::   Delivery status : Food being prepared  //
        // Order status 2 : Payment successfully received   ::::   Delivery status : In transit           //
        // Order status 3 : Payment successfully received   ::::   Delivery status : Delivered            //
        // Order status 4 : Payment Failed                  ::::   Delivery status : Not Delivered        //
        // Order status 5 : Payment successfully refunded   ::::   Delivery status : Not Delivered        //
        // Order status 6 : Payment successfully received   ::::   Delivery status : Not Delivered        //
        ////////////////////////////////////////////////////////////////////////////////////////////////////

        try {
            holder.pos = Integer.parseInt(orederBasicDetails.getStatus());
        }catch (NumberFormatException e){
            Toast.makeText(context, "Status unknown!", Toast.LENGTH_SHORT).show();
        }
        holder.btn_cancel.setVisibility(View.GONE);
        switch (orederBasicDetails.getStatus()){
            case "0":
                holder.status.setText("Status : Payment pending");
                holder.btn_cancel.setVisibility(View.VISIBLE);
               /*
                if (holder.isToday){
                    holder.update_status.setVisibility(View.VISIBLE);
                    holder.btn_cancel.setVisibility(View.VISIBLE);
                }else {
                    holder.update_status.setVisibility(View.GONE);
                    holder.btn_cancel.setVisibility(View.GONE);
                }
                */

                //holder.status.setTextColor();
                break;
            case "1":
                holder.status.setText("Status : Payment Complete Food being prepared");
                holder.btn_cancel.setVisibility(View.VISIBLE);
                /*
                if (holder.isToday){
                    holder.update_status.setVisibility(View.VISIBLE);
                    holder.btn_cancel.setVisibility(View.VISIBLE);
                }else {
                    holder.update_status.setVisibility(View.GONE);
                    holder.btn_cancel.setVisibility(View.GONE);
                }
                 */
                //holder.status.setTextColor(0);
                break;
            case "2":
                holder.btn_cancel.setVisibility(View.VISIBLE);
                /*
                if (holder.isToday){
                    holder.update_status.setVisibility(View.VISIBLE);
                    holder.btn_cancel.setVisibility(View.VISIBLE);
                }else {
                    holder.update_status.setVisibility(View.GONE);
                    holder.btn_cancel.setVisibility(View.GONE);
                }
                 */

                holder.status.setText("Status : Order in transit");
                //holder.status.setTextColor(0);
                break;
            case "3":
                /*
                holder.update_status.setVisibility(View.VISIBLE);
                holder.btn_cancel.setVisibility(View.GONE);

                 */
                holder.btn_cancel.setVisibility(View.GONE);
                holder.status.setText("Status : Delivered");
                //holder.status.setTextColor(R.color.green);
                break;
            case "4":
                /*
                if (holder.isToday){
                    holder.update_status.setVisibility(View.VISIBLE);
                    holder.btn_cancel.setVisibility(View.VISIBLE);
                }else {
                    holder.update_status.setVisibility(View.GONE);
                    holder.btn_cancel.setVisibility(View.GONE);
                }
                 */
                holder.btn_cancel.setVisibility(View.VISIBLE);
                holder.status.setText("Status : Payment failed");
                //holder.status.setTextColor(0);
                break;
            case "6":
               /*
                if (holder.isToday){
                    holder.update_status.setVisibility(View.VISIBLE);
                    holder.btn_cancel.setVisibility(View.VISIBLE);
                }else {
                    holder.update_status.setVisibility(View.GONE);
                    holder.btn_cancel.setVisibility(View.GONE);
                }
                */
                holder.status.setText("Status : Payment received but not delivered contact support");
                //holder.status.setTextColor(0);
                break;
            case "8":
                /*
                if (holder.isToday){
                    holder.update_status.setVisibility(View.VISIBLE);
                }else {
                    holder.update_status.setVisibility(View.GONE);
                }
                holder.btn_cancel.setVisibility(View.GONE);

                 */
                holder.status.setText("Status : Payment successfully received waiting for admin");
                //holder.status.setTextColor(0);
                break;
            case "7":
                /*
                holder.update_status.setVisibility(View.VISIBLE);
                holder.btn_cancel.setVisibility(View.GONE);

                 */
                holder.status.setText("Status : Order Cancelled Not delivered");
                //holder.status.setTextColor(0);
                break;
            case "9":
                /*
                holder.update_status.setVisibility(View.VISIBLE);
                holder.btn_cancel.setVisibility(View.GONE);

                 */
                holder.status.setText("Status : Food started to prepare Not Delivered");
                //holder.status.setTextColor(0);
                break;
            case "10":
                /*
                if (holder.isToday){
                    holder.update_status.setVisibility(View.VISIBLE);
                    holder.btn_cancel.setVisibility(View.VISIBLE);
                }else {
                    holder.update_status.setVisibility(View.GONE);
                    holder.btn_cancel.setVisibility(View.GONE);
                }

                 */
                holder.status.setText("Status : Food prepared Delivery Not picked up");
                //holder.status.setTextColor(0);
                break;
            case "11":
            case "12":
            case "5":
                /*
                holder.update_status.setVisibility(View.VISIBLE);
                holder.btn_cancel.setVisibility(View.GONE);

                 */
                holder.status.setText("Status : Refund");
                //holder.status.setTextColor(0);
                break;
            default:
                /*
                holder.update_status.setVisibility(View.GONE);
                holder.btn_cancel.setVisibility(View.GONE);
                                holder.btn_update.setVisibility(View.GONE);
                 */
                holder.status.setText("Status : Unknown");
                //holder.status.setTextColor(0);
                break;

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, getOrderDetailList());
        holder.update.setAdapter(adapter);

        holder.update.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                holder.pos = i;
            }
        });

        holder.btn_update.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Order Detail Updater!");
            builder.setMessage("Do you really want to update order status !");
            builder.setIcon(R.drawable.ic_round_warning_24);
            builder.setCancelable(true);

            builder
                    .setPositiveButton("YES", (dialog, which) -> OrderHistoryProvider
                            .getOrderInstance()
                            .setOnStatusSuccessFailureListner(this)
                            .setStatus(orederBasicDetails.getOrderID(), holder.pos, orederBasicDetails.getUid()))
                    .setNegativeButton("NO", (dialog, which) -> {
                        try {
                            holder.pos = Integer.parseInt(orederBasicDetails.getStatus());
                        }catch (NumberFormatException e){
                            Toast.makeText(context, "Status unknown!", Toast.LENGTH_SHORT).show();
                        }
                        holder.update.setText("");
                        dialog.dismiss();
                    })
                    .setNeutralButton("CANCEL", (dialog, which) -> {
                        dialog.cancel();
                    });

            AlertDialog dialog = builder.create();
            dialog.show();


        });

    }

    private List<String> getOrderDetailList() {
        List<String> data = new ArrayList<>();

        data.add("0 : Payment not received (Not Delivered)");
        data.add("1 : Payment successfully received (Food being prepared)");
        data.add("2 : Payment successfully received (In transit)");
        data.add("3 : Payment successfully received (Delivered)");
        data.add("4 : Payment Failed (Not Delivered)");
        data.add("5 : Payment successfully refunded (Not Delivered)");
        data.add("6 : Payment successfully received (Not Delivered)");
        data.add("7 : Order Cancelled (Cancelled)");
        data.add("8 : Payment successfully received waiting for admin (Not Delivered)");
        data.add("9 : Food started to prepare (Not Delivered)");
        data.add("10 : Food prepared (Not Picked up)");
        data.add("11 : Payment partially refunded (n/a)");
        data.add("12 : Payment fully refunded (n/a)");

        return data;
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
        AppCompatButton  btn_cancel, btn_update;//btn_decline, btn_approve,
        //LinearLayout lower;
        Boolean isToday = false;
        TextInputLayout update_status;
        AutoCompleteTextView update;
        int pos;

        public ViewBinder(@NonNull View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.status);
            total_amat = itemView.findViewById(R.id.total_amat);
            order_time = itemView.findViewById(R.id.order_time);
            allitems = itemView.findViewById(R.id.allitems);
            el_address = itemView.findViewById(R.id.el_address);
            orderid = itemView.findViewById(R.id.orderid);

           // btn_decline = itemView.findViewById(R.id.btn_decline);
            //btn_approve = itemView.findViewById(R.id.btn_approve);
            btn_cancel = itemView.findViewById(R.id.btn_cancel);
            update_status = itemView.findViewById(R.id.update_status);
            update = itemView.findViewById(R.id.update);
            btn_update = itemView.findViewById(R.id.btn_update);
            //lower = itemView.findViewById(R.id.lower);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            OrderStatus.showOrderStatus(context,((DataSnapshot)objectList.get(getAdapterPosition())).getValue(OrederBasicDetails.class));
        }
    }
}
