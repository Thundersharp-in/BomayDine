package com.thundersharp.conversation.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thundersharp.conversation.R;
import com.thundersharp.conversation.model.OrederBasicDetails;
import com.thundersharp.conversation.utils.TimestampUtils;

import java.util.List;

public class OrderSelecterAdapter extends RecyclerView.Adapter<OrderSelecterAdapter.ViewHolder>{

    private List<OrederBasicDetails> details;

    public OrderSelecterAdapter(List<OrederBasicDetails> details) {
        this.details = details;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_holder_order_info,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrederBasicDetails orederBasicDetails = details.get(position);
        holder.orderdate.setText("Ordered on :"+TimestampUtils.getTimeFromTimeStamp(orederBasicDetails.getOrderID()));
        holder.orderid.setText("Order id #"+orederBasicDetails.getOrderID());
        holder.orderitems.setText(orederBasicDetails.getItemsMain());

    }

    @Override
    public int getItemCount() {
        if (details != null)
            return details.size();
        else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView orderdate,orderid,orderitems;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderdate = itemView.findViewById(R.id.orderdate);
            orderid = itemView.findViewById(R.id.orderid);
            orderitems = itemView.findViewById(R.id.orderitems);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemView.getContext().sendBroadcast(
                    new Intent("updateRequest")
                            .putExtra("data","I want assistance on my order with id #"+details.get(getAdapterPosition()).getOrderID()+" Which was created on "+TimestampUtils.getTimeFromTimeStamp(details.get(getAdapterPosition()).getOrderID())));
        }
    }
}
