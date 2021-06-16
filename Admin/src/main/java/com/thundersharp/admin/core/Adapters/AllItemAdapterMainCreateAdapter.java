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

public class AllItemAdapterMainCreateAdapter extends RecyclerView.Adapter<AllItemAdapterMainCreateAdapter.ViewHolder> implements Filterable {

    public AllItemAdapterMainCreateAdapter(){}

    private List<Object> itemObjectlist,allList;
    private Context context;
    private ElegentNumberHelper elegentNumberHelper;
    private CartProvider cartProvider;
    private int position;

    public static AllItemAdapterMailAdapter initializeAdapter(List<Object> objects, Context context){
        return new AllItemAdapterMailAdapter(objects,context);
    }

    public AllItemAdapterMainCreateAdapter(List<Object> itemObjectlist, Context context) {
        this.itemObjectlist = itemObjectlist;
        allList = new ArrayList<>(itemObjectlist);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.item_food_main_admin,parent,false);

        cartProvider = CartProvider.initialize(context);
        return new AllItemAdapterMainCreateAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        FoodItemAdapter foodItemAdapter = (FoodItemAdapter) itemObjectlist.get(position);

        elegentNumberHelper.bindviewHolder(holder.initial,holder.finalview,R.id.minus,R.id.plus,R.id.displaytext,R.id.plusinit);
        elegentNumberHelper.getcurrentnumber();

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

        if (doSharedPrefExists()){
            List<CartItemModel> cartItemModels = returnDataFromString(fetchitemfromStorage());
            for (int i = 0;i<cartItemModels.size();i++){
                if (cartItemModels.get(i).getID().equalsIgnoreCase(foodItemAdapter.ID)){
                    elegentNumberHelper.updateNo(cartItemModels.get(i).getQUANTITY());
                    break;
                }
            }
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


    class ViewHolder extends RecyclerView.ViewHolder implements
            ElegantNumberInteractor.setOnTextChangeListner{

        public TextView cat_name;
        private ImageView icon_main,veg_nonveg;
        private TextView name,description,amount,category;
        private LinearLayout initial,finalview,cathol;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            initial = itemView.findViewById(R.id.initial);
            finalview = itemView.findViewById(R.id.finl);

            cat_name = itemView.findViewById(R.id.cat_name);
            icon_main = itemView.findViewById(R.id.icon_main);
            veg_nonveg = itemView.findViewById(R.id.veg_nonveg);
            name = itemView.findViewById(R.id.item_name);
            description = itemView.findViewById(R.id.description);
            amount = itemView.findViewById(R.id.amount);
            category = itemView.findViewById(R.id.category);
            cathol = itemView.findViewById(R.id.cathol);
            elegentNumberHelper = new ElegentNumberHelper(context,this,itemView);
        }

        @Override
        public int OnTextChangeListner(int val) {
            FoodItemAdapter foodItemAdapter = (FoodItemAdapter) itemObjectlist.get(getAdapterPosition());
            cartProvider.AddItemToCart(CartItemModel.initializeValues(foodItemAdapter.AMOUNT,foodItemAdapter.DESC,foodItemAdapter.FOOD_TYPE,foodItemAdapter.ICON_URL,foodItemAdapter.NAME,foodItemAdapter.ID,val),val);
            return 0;
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
