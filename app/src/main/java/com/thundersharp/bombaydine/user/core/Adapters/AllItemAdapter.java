package com.thundersharp.bombaydine.user.core.Adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Model.CartItemModel;
import com.thundersharp.bombaydine.user.core.Model.FoodItemAdapter;
import com.thundersharp.bombaydine.user.core.aligantnumber.ElegantNumberInteractor;
import com.thundersharp.bombaydine.user.core.aligantnumber.ElegentNumberHelper;
import com.thundersharp.bombaydine.user.core.cart.CartHandler;
import com.thundersharp.bombaydine.user.core.cart.CartProvider;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllItemAdapter extends RecyclerView.Adapter<AllItemAdapter.ViewHolder> implements Filterable {

    private List<Object> itemObjectlist , itemList;
    private Context context;
    private ElegentNumberHelper elegentNumberHelper;
    private int position;
    private CartProvider cartProvider;

    public AllItemAdapter(){}

    public AllItemAdapter(List<Object> itemObjectlist, Context context) {
        this.itemObjectlist = itemObjectlist;
        itemList = new ArrayList<>(itemObjectlist);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.uneversal_holder,parent,false);

        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItemAdapter foodItemModel = (FoodItemAdapter) itemObjectlist.get(position);

        elegentNumberHelper.bindviewHolder(holder.initial,holder.finalview,R.id.minus,R.id.plus,R.id.displaytext,R.id.plusinit);
        elegentNumberHelper.getcurrentnumber();

        holder.name.setText(foodItemModel.getNAME());
        holder.amount.setText("Rs. "+foodItemModel.getAMOUNT());
        holder.description.setText(foodItemModel.getDESC());
        Glide.with(context).load(foodItemModel.getICON_URL()).into(holder.imageView);

        if (doSharedPrefExists()){
            List<CartItemModel> cartItemModels = returnDataFromString(fetchitemfromStorage());
            for (int i = 0;i<cartItemModels.size();i++){
                if (cartItemModels.get(i).getID().equalsIgnoreCase(foodItemModel.getID())){
                    if (foodItemModel.isAVAILABLE()) {
                        elegentNumberHelper.updateNo(cartItemModels.get(i).getQUANTITY());
                        holder.counter_end.setVisibility(View.VISIBLE);
                        holder.visiblity.setVisibility(View.GONE);
                    }else {
                        cartProvider.AddItemToCart(
                                CartItemModel.initializeValues(
                                        foodItemModel.getAMOUNT(),
                                        foodItemModel.getDESC(),
                                        foodItemModel.getFOOD_TYPE(),
                                        foodItemModel.getICON_URL(),
                                        foodItemModel.getNAME(),
                                        foodItemModel.getID(),
                                        0), 0);
                        holder.counter_end.setVisibility(View.INVISIBLE);
                        holder.visiblity.setVisibility(View.VISIBLE);

                    }
                    break;
                }
            }
        }

        if (!foodItemModel.isAVAILABLE()){
            holder.counter_end.setVisibility(View.INVISIBLE);
            holder.visiblity.setVisibility(View.VISIBLE);
        }
        /*
        if (doSharedPrefExists()){
            List<CartItemModel> cartItemModels = returnDataFromString(fetchitemfromStorage());
            for (int i = 0;i<cartItemModels.size();i++){
                if (cartItemModels.get(i).getID().equalsIgnoreCase(foodItemModel.getID())){
                    elegentNumberHelper.updateNo(cartItemModels.get(i).getQUANTITY());
                    break;
                }
            }
        }
         */

    }


    public int getpos(){
        return position;
    }

    public void setpos(int pos){
        this.position = pos;
    }

    @Override
    public int getItemCount() {
        if (itemObjectlist != null) return itemObjectlist.size(); else return 0;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Object> filterItemList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filterItemList.addAll(itemList);
            }else {
                String filterText = constraint.toString().toLowerCase().trim();

                for (Object items : itemObjectlist){
                    if (((FoodItemAdapter)items).getNAME().toLowerCase().contains(filterText)
                            ||((FoodItemAdapter)items).getCAT_NAME_ID().toLowerCase().contains(filterText)
                            ||((FoodItemAdapter)items).getDESC().toLowerCase().contains(filterText)
                            ||String.valueOf(((FoodItemAdapter)items).getFOOD_TYPE()).toLowerCase().contains(filterText)){
                        filterItemList.add(items);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filterItemList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemObjectlist.clear();
            itemObjectlist.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


    class ViewHolder extends RecyclerView.ViewHolder implements ElegantNumberInteractor.setOnTextChangeListner{

        ImageView imageView;
        TextView name,description,amount, visiblity;
        LinearLayout initial,finalview,counter_end;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageview);
            name = itemView.findViewById(R.id.name);
            description =  itemView.findViewById(R.id.description);
            amount = itemView.findViewById(R.id.amount);
            visiblity = itemView.findViewById(R.id.visiblity);
            counter_end = itemView.findViewById(R.id.counter_end);
            initial = itemView.findViewById(R.id.initial);
            finalview = itemView.findViewById(R.id.finl);
            cartProvider = CartProvider.initialize(context);
            elegentNumberHelper = new ElegentNumberHelper(context,this,itemView);
        }

        @Override
        public int OnTextChangeListner(int val) {
            FoodItemAdapter foodItemAdapter = (FoodItemAdapter) itemObjectlist.get(getAdapterPosition());
            if (foodItemAdapter.isAVAILABLE()){
                cartProvider.AddItemToCart(CartItemModel.initializeValues(foodItemAdapter.getAMOUNT(),foodItemAdapter.getDESC(),foodItemAdapter.getFOOD_TYPE(),foodItemAdapter.getICON_URL(),foodItemAdapter.getNAME(),foodItemAdapter.getID(),val),val);
            }
            return 0;
        }
    }

    private List<CartItemModel> returnDataFromString(String data){
        Gson gson = new Gson();
        Type type = new TypeToken<List<CartItemModel>>(){}.getType();
        return gson.fromJson(data,type);
    }

    private boolean doSharedPrefExists(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONSTANTS.CART_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(CONSTANTS.CART_SHARED_PREFERENCES_EXISTS,false);
    }

    private String fetchitemfromStorage() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CONSTANTS.CART_SHARED_PREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(CONSTANTS.CART_SHARED_PREFERENCES_DATA,null);
    }

}
