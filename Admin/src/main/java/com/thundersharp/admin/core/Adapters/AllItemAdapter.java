package com.thundersharp.admin.core.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.AdminHelpers;
import com.thundersharp.admin.core.Model.CartItemModel;
import com.thundersharp.admin.core.Model.FoodItemAdapter;
import com.thundersharp.admin.core.aligantnumber.ElegantNumberInteractor;
import com.thundersharp.admin.core.aligantnumber.ElegentNumberHelper;
import com.thundersharp.admin.core.cart.CartProvider;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.admin.ui.edits.CategoryAddEdit;
import com.thundersharp.admin.ui.edits.EditItemActivity;

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

            ((ViewHolder)holder).name.setText(foodItemModel.NAME);
            ((ViewHolder)holder).amount.setText("Rs. " + foodItemModel.AMOUNT);
            ((ViewHolder)holder).description.setText(foodItemModel.DESC);
            if (foodItemModel.AVAILABLE){
                ((ViewHolder)holder).foodAvailable.setChecked(true);
                ((ViewHolder)holder).textavlaible.setTextColor(Color.YELLOW);
                ((ViewHolder)holder).textavlaible.setText("Available");
            } else{
                ((ViewHolder)holder).foodAvailable.setChecked(false);
                ((ViewHolder)holder).textavlaible.setTextColor(Color.RED);
                ((ViewHolder)holder).textavlaible.setText("Unavailable");
            }
            ((ViewHolder)holder).foodAvailable.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b){
                    ((ViewHolder)holder).textavlaible.setTextColor(Color.YELLOW);
                    ((ViewHolder)holder).textavlaible.setText("Available");
                }else {
                    ((ViewHolder)holder).textavlaible.setTextColor(Color.RED);
                    ((ViewHolder)holder).textavlaible.setText("Unavailable");
                }

                AdminHelpers
                        .getInstance(context)
                        .setExternalDeletePaths(
                                CONSTANTS.DATABASE_NODE_ALL_ITEMS+"/"+foodItemModel.ID+"/AVAILABLE",
                                CONSTANTS.DATABASE_NODE_CATEGORY_ITEMS+"/"+getCatID(foodItemModel.CAT_NAME_ID)+"/"+foodItemModel.ID+"/AVAILABLE")
                        .setListner(new AdminHelpers.Update() {
                            @Override
                            public void updateSuccess() {

                            }

                            @Override
                            public void updateFailure() {
                                if (b){
                                    ((ViewHolder)holder).textavlaible.setTextColor(Color.RED);
                                    ((ViewHolder)holder).textavlaible.setText("Unavailable");
                                }else {
                                    ((ViewHolder)holder).textavlaible.setTextColor(Color.YELLOW);
                                    ((ViewHolder)holder).textavlaible.setText("Available");
                                }
                            }

                        }).updateStatus(b);

            });
            Glide.with(context).load(foodItemModel.ICON_URL).into(((ViewHolder)holder).imageView);

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
                    if (((FoodItemAdapter)items).NAME.toLowerCase().contains(filterText)
                            ||((FoodItemAdapter)items).CAT_NAME_ID.toLowerCase().contains(filterText)
                            ||((FoodItemAdapter)items).DESC.toLowerCase().contains(filterText)){
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


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView,edit_act;
        TextView name,description,amount,textavlaible;
        SwitchCompat foodAvailable;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

                imageView = itemView.findViewById(R.id.imageview);
                name = itemView.findViewById(R.id.name);
                description = itemView.findViewById(R.id.description);
                amount = itemView.findViewById(R.id.amount);
                foodAvailable = itemView.findViewById(R.id.foodAvailable);
                textavlaible = itemView.findViewById(R.id.avltext);
                edit_act = itemView.findViewById(R.id.edit_act);
                edit_act.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            context.startActivity(new Intent(context, EditItemActivity.class).putExtra("dataMain",((FoodItemAdapter)itemObjectlist.get(getAdapterPosition()))));
        }
    }

    class ViewHolderOther extends RecyclerView.ViewHolder implements View.OnClickListener {


        public ViewHolderOther(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            context.startActivity(new Intent(context, EditItemActivity.class));
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
