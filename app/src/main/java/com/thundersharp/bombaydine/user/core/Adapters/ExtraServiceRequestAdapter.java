package com.thundersharp.bombaydine.user.core.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Model.CartOptionsModel;

import java.util.List;

public class ExtraServiceRequestAdapter extends RecyclerView.Adapter<ExtraServiceRequestAdapter.ViewHolder>{

    List<CartOptionsModel> dataList;
    ItemInteractionListener itemInteractionListener;

    public void setItemInteractionListener(ItemInteractionListener itemInteractionListener){
        this.itemInteractionListener = itemInteractionListener;
    }

    public ExtraServiceRequestAdapter(List<CartOptionsModel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.extra_service_request_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.checkBoxText.setText(dataList.get(position).REQUEST_OPTION_NAME);
    }

    @Override
    public int getItemCount() {
        if (dataList != null) return dataList.size(); else return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatCheckBox checkBoxText;
        private MaterialCardView materialCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBoxText = itemView.findViewById(R.id.checkBoxText);
            materialCardView = itemView.findViewById(R.id.material);

            checkBoxText.setOnCheckedChangeListener((compoundButton, b) -> {
                if (itemInteractionListener != null)
                    if (b) {
                        itemInteractionListener.onServiceItemAdded(compoundButton,dataList.get(getAdapterPosition()).CART_VALUE_CHANGE);
                        materialCardView.setBackgroundResource(R.color.card_attr);
                        materialCardView.setRadius(10);
                    } else {
                        itemInteractionListener.onServiceItemRemoved(compoundButton,dataList.get(getAdapterPosition()).CART_VALUE_CHANGE);
                        materialCardView.setBackgroundResource(R.color.card_norm);
                        materialCardView.setRadius(10);
                    }
            });

        }
    }

    public interface ItemInteractionListener{

        void onServiceItemAdded(CompoundButton compoundButton,double cartValue);
        void onServiceItemRemoved(CompoundButton compoundButton,double cartValue);

    }
}
