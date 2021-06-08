package com.thundersharp.admin.core.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Model.CartItemModel;
import com.thundersharp.admin.core.Model.FoodItemAdapter;
import com.thundersharp.admin.core.aligantnumber.ElegantNumberInteractor;
import com.thundersharp.admin.core.aligantnumber.ElegentNumberHelper;
import com.thundersharp.admin.core.cart.CartProvider;
import com.thundersharp.admin.core.utils.CONSTANTS;

import java.lang.reflect.Type;
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
        Glide.with(context).load(foodItemAdapter.getICON_URL()).into(holder.icon_main);
        if (foodItemAdapter.getFOOD_TYPE() == 1){
            holder.veg_nonveg.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);

        }else{
            holder.veg_nonveg.setColorFilter(ContextCompat.getColor(context, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);

        }

        holder.name.setText(foodItemAdapter.getNAME());
        holder.amount.setText("Rs. "+foodItemAdapter.getAMOUNT());
        holder.description.setText(foodItemAdapter.getDESC());
        holder.category.setText("In "+getcatName(foodItemAdapter.getCAT_NAME_ID()));

        if (position > 0) {
            if (getcatName(foodItemAdapter.getCAT_NAME_ID())
                    .equalsIgnoreCase(getcatName(((FoodItemAdapter) itemObjectlist.get(position - 1)).getCAT_NAME_ID()))) {
                holder.cathol.setVisibility(View.GONE);
            } else {
                holder.cat_name.setText(getcatName(foodItemAdapter.getCAT_NAME_ID()));
                holder.cathol.setVisibility(View.VISIBLE);
            }
        }else {
            holder.cat_name.setText(getcatName(foodItemAdapter.getCAT_NAME_ID()));
            holder.cathol.setVisibility(View.VISIBLE);
        }


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

                    if (((FoodItemAdapter)item).getNAME().toLowerCase().contains(filterData)
                            ||((FoodItemAdapter)item).getCAT_NAME_ID().toLowerCase().contains(filterData)
                            ||((FoodItemAdapter)item).getDESC().toLowerCase().contains(filterData)){
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


    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView cat_name;
        private ImageView icon_main,veg_nonveg;
        private TextView name,description,amount,category;
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

        }

    }

    public String getcatName(String key){
        if (key.contains("%&")){
            return key.substring(0,key.indexOf("%&")).toLowerCase();
        }else return null;
    }

    public String getCatID(String key){
        if (key.contains("%&")){
            return key.substring(key.indexOf("%&")+1);
        }else return null;
    }


}
