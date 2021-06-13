package com.thundersharp.bombaydine.kitchen.core.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.kitchen.core.helper.KitchenOrderListner;
import com.thundersharp.bombaydine.user.core.Model.OrederBasicDetails;
import com.thundersharp.bombaydine.user.core.orders.OrderContract;
import com.thundersharp.bombaydine.user.core.utils.TimeUtils;

import java.util.List;

public class ItemOrderHolder extends RecyclerView.Adapter<ItemOrderHolder.ViewHolder> implements OrderContract.StatusSuccessFailure {

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
        }else Toast.makeText(context,"Fal ",Toast.LENGTH_SHORT).show();
    }

    public ItemOrderHolder(Context context, List<DataSnapshot> objectList) {
        this.context = context;
        this.objectList = objectList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.kitchen_order_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrederBasicDetails orederBasicDetails = ((DataSnapshot)objectList.get(position)).getValue(OrederBasicDetails.class);

        holder.orderid.setText("Order #"+orederBasicDetails.getOrderID());
        holder.el_address.setText(orederBasicDetails.getDelivery_address());
        holder.allitems.setText(orederBasicDetails.getItemsMain());
        holder.order_time.setText(TimeUtils.getTimeFromTimeStamp(orederBasicDetails.getOrderID()));
        holder.total_amat.setText("\u20B9"+orederBasicDetails.getTotalamt());
        holder.uid.setText(orederBasicDetails.getUid());
        holder.user_name.setText(orederBasicDetails.getDeliveryNameData());
        holder.user_phone.setText("+91 9876543210");


        switch (orederBasicDetails.getStatus()){
            /*
            case "0":
                holder.btn_preparation_stated.setEnabled(false);
                holder.btn_chat.setEnabled(false);
                holder.btn_prepared.setEnabled(false);
                holder.btn_preparation_stated.setClickable(false);
                holder.btn_chat.setClickable(false);
                holder.btn_prepared.setClickable(false);
                break;

                case "3":
                holder.btn_preparation_stated.setEnabled(true);
                holder.btn_chat.setEnabled(false);
                holder.btn_prepared.setEnabled(false);
                holder.btn_chat.setClickable(false);
                holder.btn_preparation_stated.setClickable(true);
                holder.btn_prepared.setClickable(false);
                break;
            case "4":
                holder.btn_chat.setEnabled(true);
                holder.btn_preparation_stated.setEnabled(false);
                holder.btn_prepared.setEnabled(false);
                holder.btn_chat.setClickable(true);
                holder.btn_preparation_stated.setClickable(false);
                holder.btn_prepared.setClickable(false);
                break;
            case "5":
                holder.btn_chat.setEnabled(true);
                holder.btn_preparation_stated.setEnabled(false);
                holder.btn_prepared.setEnabled(false);
                holder.btn_chat.setClickable(true);
                holder.btn_preparation_stated.setClickable(false);
                holder.btn_prepared.setClickable(false);
                break;

                case "7"://Order Cancelled
                holder.btn_chat.setEnabled(false);
                holder.btn_preparation_stated.setEnabled(false);
                holder.btn_prepared.setEnabled(false);
                holder.btn_chat.setClickable(false);
                holder.btn_preparation_stated.setClickable(false);
                holder.btn_prepared.setClickable(false);
                break;
                case "11":
                holder.btn_chat.setEnabled(true);
                holder.btn_preparation_stated.setEnabled(false);
                holder.btn_prepared.setEnabled(false);
                holder.btn_chat.setClickable(true);
                holder.btn_preparation_stated.setClickable(false);
                holder.btn_prepared.setClickable(false);
                break;
            case "12":
                holder.btn_chat.setEnabled(false);
                holder.btn_preparation_stated.setEnabled(false);
                holder.btn_prepared.setEnabled(false);
                holder.btn_chat.setClickable(false);
                holder.btn_preparation_stated.setClickable(false);
                holder.btn_prepared.setClickable(false);
                break;
             */
            case "1"://Payment successfully received   ::::   Delivery status : Food being prepared
                holder.btn_preparation_stated.setText("Preparing...");
                holder.btn_preparation_stated.setEnabled(false);
                holder.btn_chat.setEnabled(true);
                holder.btn_prepared.setEnabled(true);
                holder.btn_chat.setClickable(true);
                holder.btn_preparation_stated.setClickable(false);
                holder.btn_prepared.setClickable(true);
                break;
            case "2"://Payment successfully received   ::::   Delivery status : In transit
                holder.btn_prepared.setText("Prepared Done");
                holder.btn_preparation_stated.setText("Preparing Done");
                holder.btn_chat.setText("Chat Unavailable");
                holder.btn_chat.setClickable(false);
                holder.btn_chat.setEnabled(false);
                holder.btn_preparation_stated.setClickable(false);
                holder.btn_preparation_stated.setEnabled(false);
                holder.btn_prepared.setClickable(false);
                holder.btn_prepared.setEnabled(false);
                break;

            case "6"://Payment successfully received   ::::   Delivery status : Not Delivered

            case "8"://Payment successfully received waiting for admin
                holder.btn_chat.setEnabled(true);
                holder.btn_preparation_stated.setEnabled(true);
                holder.btn_prepared.setEnabled(false);
                holder.btn_chat.setClickable(true);
                holder.btn_preparation_stated.setClickable(true);
                holder.btn_prepared.setClickable(false);
                break;
            case "9"://start ot prepare
                holder.btn_chat.setEnabled(true);
                holder.btn_preparation_stated.setEnabled(false);
                holder.btn_preparation_stated.setText("Preparing...");
                holder.btn_prepared.setEnabled(true);
                holder.btn_chat.setClickable(true);
                holder.btn_preparation_stated.setClickable(false);
                holder.btn_prepared.setClickable(true);
                break;
            case "10"://Food prepared
                holder.btn_prepared.setText("Prepared Done");
                holder.btn_preparation_stated.setText("Preparing Done");
                holder.btn_chat.setText("Chat Unavailable");
                holder.btn_chat.setEnabled(false);
                holder.btn_preparation_stated.setEnabled(false);
                holder.btn_prepared.setEnabled(false);
                holder.btn_chat.setClickable(false);
                holder.btn_preparation_stated.setClickable(false);
                holder.btn_prepared.setClickable(false);
                break;

            default:
                holder.btn_prepared.setText("Not Available");
                holder.btn_preparation_stated.setText("Not Available");
                holder.btn_chat.setText("Chat Unavailable");
                holder.btn_preparation_stated.setEnabled(false);
                holder.btn_chat.setEnabled(false);
                holder.btn_prepared.setEnabled(false);
                holder.btn_chat.setClickable(false);
                holder.btn_preparation_stated.setClickable(false);
                holder.btn_prepared.setClickable(false);

        }

        holder.btn_chat.setOnClickListener( (view)-> {

        });

        holder.btn_preparation_stated.setOnClickListener( (view)-> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirmation Dialog");
            builder.setMessage("Are you sure you have started preparing food !!");
            builder.setIcon(R.drawable.ic_round_warning_24);
            builder.setCancelable(false);

            builder.setPositiveButton("YES", (dialog, which) -> {

                KitchenOrderListner
                        .getKitchenOrderInstance()
                        .setOnStatusSuccessFailureListner(this)
                        .setStatus(orederBasicDetails.getOrderID(),9,orederBasicDetails.getUid());

                holder.btn_preparation_stated.setText("Preparing...");
                dialog.dismiss();

            }).setNegativeButton("NO", (dialog, which) -> {
                holder.btn_preparation_stated.setText("Not Started");
                dialog.cancel();
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        });

        holder.btn_prepared.setOnClickListener( (view)-> {
            KitchenOrderListner
                    .getKitchenOrderInstance()
                    .setOnStatusSuccessFailureListner(this)
                    .setStatus(orederBasicDetails.getOrderID(),10,orederBasicDetails.getUid());
            holder.btn_prepared.setText("Prepared Done");
            holder.btn_preparation_stated.setText("Preparing Done");
            holder.btn_chat.setText("Chat Unavailable");
        });

    }

    @Override
    public int getItemCount() {
        if (objectList!=null) return objectList.size(); else
        return 0;
    }

    @Override
    public void onSuccess(@NonNull Task<Void> task) {
        Toast.makeText(context, "SuccessFul!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Exception e) {
        Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView orderid,el_address,allitems,order_time,total_amat,uid,user_name,user_phone;
        AppCompatButton btn_preparation_stated,btn_prepared,btn_chat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderid = itemView.findViewById(R.id.orderid);
            el_address = itemView.findViewById(R.id.el_address);
            allitems = itemView.findViewById(R.id.allitems);
            order_time = itemView.findViewById(R.id.order_time);
            total_amat = itemView.findViewById(R.id.total_amat);
            btn_preparation_stated = itemView.findViewById(R.id.btn_preparation_stated);
            btn_prepared = itemView.findViewById(R.id.btn_prepared);
            btn_chat = itemView.findViewById(R.id.btn_chat);
            uid = itemView.findViewById(R.id.uid);
            user_name = itemView.findViewById(R.id.user_name);
            user_phone = itemView.findViewById(R.id.user_phone);

        }
    }
}
