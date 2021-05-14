package com.thundersharp.bombaydine.user.core.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.thundersharp.bombaydine.user.core.cart.CartEmptyUpdater;
import com.thundersharp.bombaydine.user.core.cart.CartProvider;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.user.ui.home.HomeFragment;
import com.thundersharp.bombaydine.user.ui.menu.AllItemsActivity;

import java.lang.reflect.Type;
import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder>{

    public CartItemAdapter(){}

    private List<CartItemModel> itemObjectlist;
    private Context context;
    private ElegentNumberHelper elegentNumberHelper;
    private CartProvider cartProvider;
    private int mode;

    public static CartItemAdapter initializeAdapter(List<CartItemModel> objects, Context context,int mode){
        return new CartItemAdapter(objects,context,mode);
    }

    public CartItemAdapter(List<CartItemModel> itemObjectlist, Context context, int mode) {
        this.itemObjectlist = itemObjectlist;
        this.context = context;
        this.mode = mode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.item_food_cart,parent,false);

        cartProvider = CartProvider.initialize(context);
        return new CartItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItemModel foodItemAdapter = itemObjectlist.get(position);

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



    class ViewHolder extends RecyclerView.ViewHolder implements
            ElegantNumberInteractor.setOnTextChangeListner{

        private ImageView icon_main,veg_nonveg;
        private TextView name,description,amount;
        private LinearLayout initial,finalview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            initial = itemView.findViewById(R.id.initial);
            finalview = itemView.findViewById(R.id.finl);

            icon_main = itemView.findViewById(R.id.icon_main);
            veg_nonveg = itemView.findViewById(R.id.veg_nonveg);
            name = itemView.findViewById(R.id.item_name);
            description = itemView.findViewById(R.id.description);
            amount = itemView.findViewById(R.id.amount);
            elegentNumberHelper = new ElegentNumberHelper(context,this,itemView);
        }

        @Override
        public int OnTextChangeListner(int val) {

            CartItemModel foodItemAdapter =  itemObjectlist.get(getAdapterPosition());
            foodItemAdapter.setQUANTITY(val);
            cartProvider.AddItemToCart(foodItemAdapter,val);

            if (val == 0){
                deleteItem(itemView,getAdapterPosition());
            }
            return 0;
        }
    }

    private void deleteItem(View rowView, final int position) {

        Animation anim = AnimationUtils.loadAnimation(context,
                android.R.anim.slide_out_right);
        anim.setDuration(300);
        rowView.startAnimation(anim);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                itemObjectlist.remove(position);
                if (itemObjectlist.size() == 0) {

                    if (mode == 1){
                        if (HomeFragment.bottomSheetDialog != null) {
                            if (HomeFragment.bottomSheetDialog.isShowing()) {
                                HomeFragment.bottomSheetDialog.hide();
                            }
                        }

                    }else {
                        if (AllItemsActivity.bottomSheetDialog != null) {
                            if (AllItemsActivity.bottomSheetDialog.isShowing()) {
                                AllItemsActivity.bottomSheetDialog.hide();
                            }
                        }
                    }
                    return;
                }
                notifyItemRemoved(position);
            }

        }, anim.getDuration());
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
