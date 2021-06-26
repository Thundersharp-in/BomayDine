package com.thundersharp.bombaydine.user.core.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Data.OfferListner;
import com.thundersharp.bombaydine.user.core.Data.OffersProvider;
import com.thundersharp.bombaydine.user.core.Model.CartItemModel;
import com.thundersharp.bombaydine.user.core.Model.FoodItemAdapter;
import com.thundersharp.bombaydine.user.core.aligantnumber.ElegantNumberInteractor;
import com.thundersharp.bombaydine.user.core.aligantnumber.ElegentNumberHelper;
import com.thundersharp.bombaydine.user.core.cart.CartHandler;
import com.thundersharp.bombaydine.user.core.cart.CartProvider;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.*;

public class AllItemAdapterMailAdapter extends RecyclerView.Adapter<AllItemAdapterMailAdapter.ViewHolder> implements Filterable {

    public AllItemAdapterMailAdapter(){}

    private List<Object> itemObjectlist,allList;
    private Context context;
    private ElegentNumberHelper elegentNumberHelper;
    private CartProvider cartProvider;
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
        View view =  LayoutInflater.from(context).inflate(R.layout.item_food,parent,false);

        cartProvider = CartProvider.initialize(context);
        return new AllItemAdapterMailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        FoodItemAdapter foodItemAdapter = (FoodItemAdapter) itemObjectlist.get(position);

        elegentNumberHelper.bindviewHolder(holder.initial,holder.finalview,R.id.minus,R.id.plus,R.id.displaytext,R.id.plusinit);
        elegentNumberHelper.getcurrentnumber();

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

        if (doSharedPrefExists()){
            List<CartItemModel> cartItemModels = returnDataFromString(fetchitemfromStorage());
            for (int i = 0;i<cartItemModels.size();i++){
                if (cartItemModels.get(i).getID().equalsIgnoreCase(foodItemAdapter.getID())){
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

                    if (((FoodItemAdapter)item).getNAME().toLowerCase().contains(filterData)
                            ||((FoodItemAdapter)item).getCAT_NAME_ID().toLowerCase().contains(filterData)
                            ||((FoodItemAdapter)item).getDESC().toLowerCase().contains(filterData)
                            ||String.valueOf(((FoodItemAdapter)item).getFOOD_TYPE()).toLowerCase().contains(filterData)){
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
            cartProvider.AddItemToCart(CartItemModel.initializeValues(foodItemAdapter.getAMOUNT(),foodItemAdapter.getDESC(),foodItemAdapter.getFOOD_TYPE(),foodItemAdapter.getICON_URL(),foodItemAdapter.getNAME(),foodItemAdapter.getID(),val),val);
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
