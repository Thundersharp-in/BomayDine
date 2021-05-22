package com.thundersharp.bombaydine.user.core.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Model.OrderModel;

import java.util.List;

public class OrderItem extends RecyclerView.Adapter<OrderItem.ViewHolder>{

    Context context;
    List<OrderModel> modelList;

    public OrderItem(Context context, List<OrderModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderItem.ViewHolder(LayoutInflater.from(context).inflate(R.layout.ordered_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel model=modelList.get(position);
        holder.food_name.setText(model.getName());
        holder.item_amount.setText(String.valueOf(model.getAmount()));
        holder.qty.setText(String.valueOf(model.getQuantity()));
        holder.t_amount.setText(String.valueOf(model.getQuantity()*model.getAmount()));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView v_nv;
        TextView food_name,qty,item_amount,t_amount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            v_nv=itemView.findViewById(R.id.v_nv);
            food_name=itemView.findViewById(R.id.food_name);
            qty=itemView.findViewById(R.id.qty);
            item_amount=itemView.findViewById(R.id.item_amount);
            t_amount=itemView.findViewById(R.id.t_amount);

        }
    }
}
