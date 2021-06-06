package com.thundersharp.bombaydine.Delevery.core;

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
import com.thundersharp.bombaydine.kitchen.core.KitchenOrderListner;
import com.thundersharp.bombaydine.user.core.Model.OrederBasicDetails;
import com.thundersharp.bombaydine.user.core.orders.OrderContract;
import com.thundersharp.bombaydine.user.core.utils.TimeUtils;
import com.thundersharp.conversation.model.Chat;

import java.util.List;

import static com.thundersharp.conversation.ChatFragmentInternal.sendmessageRecycler;

public class ItemDeliverHolder extends RecyclerView.Adapter<ItemDeliverHolder.ViewHolder> implements OrderContract.StatusSuccessFailure {

    private Context context;
    private List<DataSnapshot> objectList;

    public ItemDeliverHolder(Context context, List<DataSnapshot> objectList) {
        this.context = context;
        this.objectList = objectList;
    }

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



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.delivery_order_item,parent,false));
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
            case "2":
                holder.btn_navigate_map.setEnabled(true);
                holder.btn_delever.setEnabled(true);
                holder.btn_pick_up.setEnabled(false);
                holder.btn_navigate_map.setClickable(true);
                holder.btn_delever.setClickable(true);
                holder.btn_pick_up.setClickable(false);
                break;
            /*
            case "3":
                holder.btn_navigate_map.setEnabled(false);
                holder.btn_delever.setEnabled(false);
                holder.btn_pick_up.setEnabled(false);
                holder.btn_navigate_map.setClickable(false);
                holder.btn_delever.setClickable(false);
                holder.btn_pick_up.setClickable(false);
                break;
             */

            case "10":
                holder.btn_navigate_map.setEnabled(false);
                holder.btn_delever.setEnabled(false);
                holder.btn_pick_up.setEnabled(true);
                holder.btn_navigate_map.setClickable(false);
                holder.btn_delever.setClickable(false);
                holder.btn_pick_up.setClickable(true);
                break;

            default:
                holder.btn_navigate_map.setEnabled(false);
                holder.btn_delever.setEnabled(false);
                holder.btn_pick_up.setEnabled(false);
                holder.btn_navigate_map.setClickable(false);
                holder.btn_delever.setClickable(false);
                holder.btn_pick_up.setClickable(false);

        }

        holder.btn_navigate_map.setOnClickListener( (view)-> {

        });

        holder.btn_delever.setOnClickListener( (view)-> {
            KitchenOrderListner
                    .getKitchenOrderInstance()
                    .setOnStatusSuccessFailureListner(this)
                    .setStatus(orederBasicDetails.getOrderID(),3,orederBasicDetails.getUid());
            //getStatus("3");
            /*
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirmation Dialog");
            builder.setMessage("Are you sure you have started preparing food !!");
            builder.setCancelable(false);

            builder.setPositiveButton("YES", (dialog, which) -> {

                holder.btn_preparation_stated.setText("Started");
                dialog.dismiss();

            }).setNegativeButton("NO", (dialog, which) -> {
                holder.btn_preparation_stated.setText("Not Started");
                dialog.cancel();
            });

            AlertDialog dialog = builder.create();
            dialog.show();

             */
        });

        holder.btn_pick_up.setOnClickListener( (view)-> {
            KitchenOrderListner
                    .getKitchenOrderInstance()
                    .setOnStatusSuccessFailureListner(this)
                    .setStatus(orederBasicDetails.getOrderID(),2,orederBasicDetails.getUid());

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
        AppCompatButton btn_pick_up,btn_delever,btn_navigate_map;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            orderid = itemView.findViewById(R.id.orderid);
            el_address = itemView.findViewById(R.id.el_address);
            allitems = itemView.findViewById(R.id.allitems);
            order_time = itemView.findViewById(R.id.order_time);
            total_amat = itemView.findViewById(R.id.total_amat);
            btn_navigate_map = itemView.findViewById(R.id.btn_navigate_map);
            btn_pick_up = itemView.findViewById(R.id.btn_pick_up);
            btn_delever = itemView.findViewById(R.id.btn_delever);
            uid = itemView.findViewById(R.id.uid);
            user_name = itemView.findViewById(R.id.user_name);
            user_phone = itemView.findViewById(R.id.user_phone);

        }
    }
}
