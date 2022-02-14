package com.thundersharp.admin.core.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.AdminHelpers;
import com.thundersharp.admin.core.Model.FoodItemAdapter;
import com.thundersharp.admin.core.cart.CartProvider;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.admin.ui.dailyfood.DailyfoodActivity;
import com.thundersharp.admin.ui.edits.EditDailyFoodActivity;
import com.thundersharp.admin.ui.edits.EditItemActivity;

import java.util.ArrayList;
import java.util.List;

public class AllSelectItemAdapter extends RecyclerView.Adapter<AllSelectItemAdapter.ViewHolder>{

    private List<Object> itemObjectlist;
    private Context context;
    private List<String> ids;

    public AllSelectItemAdapter(){}

    public AllSelectItemAdapter(List<Object> itemObjectlist,List<String> ids, Context context) {
        this.itemObjectlist = itemObjectlist;
        this.ids = ids;
        this.context = context;
    }

    @NonNull
    @Override
    public AllSelectItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.add_food_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItemAdapter foodItemModel = (FoodItemAdapter) itemObjectlist.get(position);

        holder.name.setText(foodItemModel.NAME);
        holder.amount.setText("Rs. " + foodItemModel.AMOUNT);
        holder.description.setText(foodItemModel.DESC);

        Glide.with(context).load(foodItemModel.ICON_URL).into(holder.imageView);

        for (String id:ids) {
            if (id.equals(foodItemModel.ID)){
                holder.click.setChecked(true);
                EditDailyFoodActivity.selected_foodItemAdapterList.put(foodItemModel.ID, foodItemModel);
            }
        }

        holder.click.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) EditDailyFoodActivity.selected_foodItemAdapterList.put(foodItemModel.ID,foodItemModel);
            else EditDailyFoodActivity.selected_foodItemAdapterList.remove(foodItemModel.ID);
        });
    }

    @Override
    public int getItemCount() {
        if (itemObjectlist != null) return itemObjectlist.size(); else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,description,amount;
        CheckBox click;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.imageview);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            amount = itemView.findViewById(R.id.amount);
            click = itemView.findViewById(R.id.click);
        }

    }
}
