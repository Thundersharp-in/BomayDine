package com.thundersharp.bombaydine.user.core.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
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
import java.util.List;

public class CategoryitemAdapter extends RecyclerView.Adapter<CategoryitemAdapter.holder>{

    private Context context;
    private List<Object> objectList;
    private ElegentNumberHelper elegentNumberHelper;
    private CartProvider cartProvider;

    public CategoryitemAdapter(Context context, List<Object> objectList) {
        this.context = context;
        this.objectList = objectList;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_cat_uni,parent,false);
        cartProvider = CartProvider.initialize(context);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        FoodItemAdapter foodItemModel = ((DataSnapshot) objectList.get(position)).getValue(FoodItemAdapter.class);

        elegentNumberHelper.bindviewHolder(holder.initial,holder.finalview,R.id.minus,R.id.plus,R.id.displaytext,R.id.plusinit);
        elegentNumberHelper.getcurrentnumber();

        holder.cat_name.setText(foodItemModel.getNAME());
        holder.price.setText("Rs. "+foodItemModel.getAMOUNT());
        holder.desc.setText(foodItemModel.getDESC());
        Glide.with(context).load(foodItemModel.getICON_URL()).into(holder.food_img);
        if (foodItemModel.getFOOD_TYPE() == 1){
            holder.food_type.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);

        }else{
            holder.food_type.setColorFilter(ContextCompat.getColor(context, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);

        }

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

    }

    @Override
    public int getItemCount() {
        if (objectList != null) return objectList.size();else return 0;
    }


    public class holder extends RecyclerView.ViewHolder implements ElegantNumberInteractor.setOnTextChangeListner  {
        ImageView food_img, food_type;
        TextView cat_name, sub_title, price, desc, visiblity;
        LinearLayout initial,finalview, counter_end;
        CartProvider cartProvider;

        public holder(@NonNull View itemView) {
            super(itemView);
            cat_name = itemView.findViewById(R.id.cat_name);
            sub_title = itemView.findViewById(R.id.sub_title);
            price = itemView.findViewById(R.id.price);
            desc = itemView.findViewById(R.id.desc);
            food_img = itemView.findViewById(R.id.food_img);
            food_type = itemView.findViewById(R.id.food_type);
            visiblity = itemView.findViewById(R.id.visiblity);
            counter_end = itemView.findViewById(R.id.counter_end);

            initial = itemView.findViewById(R.id.initial);
            finalview = itemView.findViewById(R.id.finl);
            cartProvider = CartProvider.initialize(context);
            elegentNumberHelper = new ElegentNumberHelper(context,this,itemView);
        }


        @Override
        public int OnTextChangeListner(int val) {
            FoodItemAdapter foodItemAdapter = ((DataSnapshot)objectList.get(getAdapterPosition())).getValue(FoodItemAdapter.class);
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
