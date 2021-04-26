package com.thundersharp.bombaydine.user.core.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.aligantnumber.ElegantNumberInteractor;
import com.thundersharp.bombaydine.user.core.aligantnumber.ElegentNumberHelper;

import java.util.List;

public class CategoryitemAdapter extends RecyclerView.Adapter<CategoryitemAdapter.holder> implements ElegantNumberInteractor.setOnTextChangeListner {

    private Context context;
    private List<Object> objectList;
    private ElegentNumberHelper elegentNumberHelper;

    public CategoryitemAdapter(Context context, List<Object> objectList) {
        this.context = context;
        this.objectList = objectList;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_cat_uni,parent,false);
        elegentNumberHelper = new ElegentNumberHelper(context,this,view);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        elegentNumberHelper.bindviewHolder(holder.initial,holder.finalview,R.id.minus,R.id.plus,R.id.displaytext,R.id.plusinit);
        elegentNumberHelper.getcurrentnumber();

    }

    @Override
    public int getItemCount() {
        if (objectList != null) return objectList.size();else return 0;
    }

    @Override
    public int OnTextChangeListner(int val) {

        return 0;
    }

    public class holder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        LinearLayout initial,finalview;

        public holder(@NonNull View itemView) {
            super(itemView);

            initial = itemView.findViewById(R.id.initial);
            finalview = itemView.findViewById(R.id.finl);
        }
    }
}
