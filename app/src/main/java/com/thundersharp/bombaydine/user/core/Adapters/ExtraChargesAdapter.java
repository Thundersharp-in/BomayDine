package com.thundersharp.bombaydine.user.core.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Model.CartOptionsModel;

import java.util.List;

public class ExtraChargesAdapter extends RecyclerView.Adapter<ExtraChargesAdapter.ViewHolder>{

    List<CartOptionsModel> dataList;

    public ExtraChargesAdapter(List<CartOptionsModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.extra_charges_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.descriptionText.setText(dataList.get(position).REQUEST_OPTION_NAME);
        holder.value.setText("\u20B9 "+dataList.get(position).CART_VALUE_CHANGE);
    }

    @Override
    public int getItemCount() {
        if (dataList != null) return dataList.size(); else return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView descriptionText,value;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            descriptionText = itemView.findViewById(R.id.descriptionText);
            value = itemView.findViewById(R.id.del_charges);


        }
    }
}
