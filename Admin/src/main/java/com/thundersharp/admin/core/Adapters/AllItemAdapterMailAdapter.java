package com.thundersharp.admin.core.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.AdminHelpers;
import com.thundersharp.admin.core.Model.FoodItemAdapter;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.admin.ui.edits.EditItemActivity;

import java.util.ArrayList;
import java.util.List;

public class AllItemAdapterMailAdapter extends RecyclerView.Adapter<AllItemAdapterMailAdapter.ViewHolder> implements Filterable {

    public AllItemAdapterMailAdapter(){}

    private List<Object> itemObjectlist,allList;
    private Context context;
    private int position;

    public static AllItemAdapterMailAdapter initializeAdapter(List<Object> objects, Context context){
        return new AllItemAdapterMailAdapter(objects,context);
    }

    public AllItemAdapterMailAdapter(List<Object> itemObjectlist, Context context) {
        this.itemObjectlist = itemObjectlist;
        allList = new ArrayList<>(itemObjectlist);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.item_food_admin,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        FoodItemAdapter foodItemAdapter = (FoodItemAdapter) itemObjectlist.get(position);

        //TODO ADD LOGIC IF IMG ASSET == N
        Glide.with(context).load(foodItemAdapter.ICON_URL).into(holder.icon_main);
        if (foodItemAdapter.FOOD_TYPE == 1){
            holder.veg_nonveg.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);

        }else{
            holder.veg_nonveg.setColorFilter(ContextCompat.getColor(context, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);

        }

        holder.name.setText(foodItemAdapter.NAME);
        holder.amount.setText("Rs. "+foodItemAdapter.AMOUNT);
        holder.description.setText(foodItemAdapter.DESC);
        holder.category.setText("In "+getcatName(foodItemAdapter.CAT_NAME_ID));

        if (position > 0) {
            if (getcatName(foodItemAdapter.CAT_NAME_ID)
                    .equalsIgnoreCase(getcatName(((FoodItemAdapter) itemObjectlist.get(position - 1)).CAT_NAME_ID))) {
                holder.cathol.setVisibility(View.GONE);
            } else {
                holder.cat_name.setText(getcatName(foodItemAdapter.CAT_NAME_ID));
                holder.cathol.setVisibility(View.VISIBLE);
            }
        }else {
            holder.cat_name.setText(getcatName(foodItemAdapter.CAT_NAME_ID));
            holder.cathol.setVisibility(View.VISIBLE);
        }

        if (foodItemAdapter.AVAILABLE)
            holder.foodavailable.setChecked(true);
        else holder.foodavailable.setChecked(false);
        holder.foodavailable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    holder.textavlaible.setTextColor(Color.YELLOW);
                    holder.textavlaible.setText("Available");
                }else {
                    holder.textavlaible.setTextColor(Color.RED);
                    holder.textavlaible.setText("Unavailable");

                }
                AdminHelpers
                        .getInstance(context)
                        .setExternalDeletePaths(
                                CONSTANTS.DATABASE_NODE_ALL_ITEMS+"/"+foodItemAdapter.ID+"/AVAILABLE",
                                CONSTANTS.DATABASE_NODE_CATEGORY_ITEMS+"/"+getCatID(foodItemAdapter.CAT_NAME_ID)+"/"+foodItemAdapter.ID+"/AVAILABLE")
                        .setListner(new AdminHelpers.Update() {
                            @Override
                            public void updateSuccess() {

                            }

                            @Override
                            public void updateFailure() {
                                if (b){
                                    holder.textavlaible.setTextColor(Color.RED);
                                    holder.textavlaible.setText("Unavailable");
                                }else {
                                    holder.textavlaible.setTextColor(Color.YELLOW);
                                    holder.textavlaible.setText("Available");
                                }
                            }
                        })
                        .updateStatus(b);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (itemObjectlist != null)
            return itemObjectlist.size();
        else
            return 0;
    }

    @Override
    public Filter getFilter() {
        return foodName;
    }

    Filter foodName = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Object> allFoodData=new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0){
                allFoodData.addAll(allList);

            }else {
                String filterData = charSequence.toString().toLowerCase().trim();
                for (Object item : itemObjectlist){

                    if (((FoodItemAdapter)item).NAME.toLowerCase().contains(filterData)
                            ||((FoodItemAdapter)item).CAT_NAME_ID.toLowerCase().contains(filterData)
                            ||((FoodItemAdapter)item).DESC.toLowerCase().contains(filterData)){
                        allFoodData.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=allFoodData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            itemObjectlist.clear();
            itemObjectlist.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView cat_name;
        private ImageView icon_main,veg_nonveg,edit_item;
        private TextView name,description,amount,category,textavlaible;
        private SwitchCompat foodavailable;
        private LinearLayout cathol;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            cat_name = itemView.findViewById(R.id.cat_name);
            icon_main = itemView.findViewById(R.id.icon_main);
            veg_nonveg = itemView.findViewById(R.id.veg_nonveg);
            name = itemView.findViewById(R.id.item_name);
            description = itemView.findViewById(R.id.description);
            amount = itemView.findViewById(R.id.amount);
            category = itemView.findViewById(R.id.category);
            cathol = itemView.findViewById(R.id.cathol);
            foodavailable = itemView.findViewById(R.id.foodAvailable);
            textavlaible = itemView.findViewById(R.id.textAvl);
            edit_item = itemView.findViewById(R.id.edit_item);
            edit_item.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            context.startActivity(new Intent(context, EditItemActivity.class).putExtra("dataMain",((FoodItemAdapter)itemObjectlist.get(getAdapterPosition()))));
        }
    }

    public String getcatName(String key){
        if (key.contains("%&")){
            return key.substring(0,key.indexOf("%&")).toLowerCase();
        }else return null;
    }

    public String getCatID(String key){
        if (key.contains("%&")){
            return key.substring(key.indexOf("&")+1);
        }else return null;
    }


}
