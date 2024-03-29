package com.thundersharp.bombaydine.user.core.Adapters;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Model.CartItemModel;
import com.thundersharp.bombaydine.user.core.Model.FoodItemAdapter;
import com.thundersharp.bombaydine.user.core.aligantnumber.ElegantNumberInteractor;
import com.thundersharp.bombaydine.user.core.aligantnumber.ElegentNumberHelper;
import com.thundersharp.bombaydine.user.core.cart.CartProvider;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DealOfTheDayAdapter extends RecyclerView.Adapter<DealOfTheDayAdapter.ViewHolder> implements Filterable {

    public DealOfTheDayAdapter(){}

    private List<Object> itemObjectlist,allList;
    private Context context;
    private ElegentNumberHelper elegentNumberHelper;
    private CartProvider cartProvider;
    private int position;
    public static int currentpos =0;

    public static DealOfTheDayAdapter initializeAdapter(List<Object> objects, Context context){
        return new DealOfTheDayAdapter(objects,context);
    }

    public DealOfTheDayAdapter(List<Object> itemObjectlist, Context context) {
        this.itemObjectlist = itemObjectlist;
        allList = new ArrayList<>(itemObjectlist);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.item_recomendation,parent,false);

        cartProvider = CartProvider.initialize(context);
        return new DealOfTheDayAdapter.ViewHolder(view);
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


        if (doSharedPrefExists()){
            List<CartItemModel> cartItemModels = returnDataFromString(fetchitemfromStorage());
            for (int i = 0;i<cartItemModels.size();i++){
                if (cartItemModels.get(i).getID().equalsIgnoreCase(foodItemAdapter.getID())){
                    if (foodItemAdapter.isAVAILABLE()) {
                        elegentNumberHelper.updateNo(cartItemModels.get(i).getQUANTITY());
                        holder.counter_end.setVisibility(View.VISIBLE);
                        holder.visiblity.setVisibility(View.GONE);
                    }else {
                        cartProvider.AddItemToCart(
                                CartItemModel.initializeValues(
                                        foodItemAdapter.getAMOUNT(),
                                        foodItemAdapter.getDESC(),
                                        foodItemAdapter.getFOOD_TYPE(),
                                        foodItemAdapter.getICON_URL(),
                                        foodItemAdapter.getNAME(),
                                        foodItemAdapter.getID(),
                                        0), 0);
                        holder.counter_end.setVisibility(View.INVISIBLE);
                        holder.visiblity.setVisibility(View.VISIBLE);

                    }
                    //elegentNumberHelper.updateNo(cartItemModels.get(i).getQUANTITY());
                    break;
                }
            }
        }

        if (!foodItemAdapter.isAVAILABLE()){
            holder.counter_end.setVisibility(View.INVISIBLE);
            holder.visiblity.setVisibility(View.VISIBLE);
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


    class ViewHolder extends RecyclerView.ViewHolder implements
            ElegantNumberInteractor.setOnTextChangeListner{

        private ImageView icon_main,veg_nonveg;
        private TextView name,description,amount;
        private LinearLayout initial,finalview,counter_end;
        private TextView visiblity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            initial = itemView.findViewById(R.id.initial);
            finalview = itemView.findViewById(R.id.finl);

            icon_main = itemView.findViewById(R.id.imageview);
            veg_nonveg = itemView.findViewById(R.id.veg_nonveg);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.desc);
            amount = itemView.findViewById(R.id.price);
            counter_end = itemView.findViewById(R.id.counter_end);

            visiblity = itemView.findViewById(R.id.visiblity);

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
