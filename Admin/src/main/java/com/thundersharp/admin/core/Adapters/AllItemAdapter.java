package com.thundersharp.admin.core.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
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
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
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

public class AllItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 0;
    private List<Object> itemObjectlist , itemList;
    private Context context;
    Integer addedPos;
    private CartProvider cartProvider;

    public AllItemAdapter(){}

    public AllItemAdapter(List<Object> itemObjectlist, Context context) {
        this.itemObjectlist = itemObjectlist;
        itemList = new ArrayList<>(itemObjectlist);
        this.context = context;
    }

    public void addLastItem(Object object ,int position){
        this.addedPos = position;
        itemObjectlist.add(object);
        notifyDataSetChanged();


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder view = null;

        switch (viewType){
            case 1:
                View viewq = LayoutInflater.from(context).inflate(R.layout.add_new_item_bottom,parent,false);
                view = new ViewHolderOther(viewq);
                break;
            case 0:
                View viewqw = LayoutInflater.from(context).inflate(R.layout.uneversal_holder_admin,parent,false);
                view = new ViewHolder(viewqw);
                break;
        }


        return view;
    }

    @Override
    public int getItemViewType(int position) {


        if (itemObjectlist.get(position).equals("Add")) {
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {

            FoodItemAdapter foodItemModel = (FoodItemAdapter) itemObjectlist.get(position);

            ((ViewHolder)holder).name.setText(foodItemModel.getNAME());
            ((ViewHolder)holder).amount.setText("Rs. " + foodItemModel.getAMOUNT());
            ((ViewHolder)holder).description.setText(foodItemModel.getDESC());
            Glide.with(context).load(foodItemModel.getICON_URL()).into(((ViewHolder)holder).imageView);

        }

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
                            ||((FoodItemAdapter)items).getDESC().toLowerCase().contains(filterText)){
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


    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name,description,amount;
        SwitchCompat foodAvailable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

                imageView = itemView.findViewById(R.id.imageview);
                name = itemView.findViewById(R.id.name);
                description = itemView.findViewById(R.id.description);
                amount = itemView.findViewById(R.id.amount);
                foodAvailable = itemView.findViewById(R.id.foodAvailable);



        }

    }

    class ViewHolderOther extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ViewHolderOther(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context,"Add clk",Toast.LENGTH_SHORT).show();
        }
    }


}
