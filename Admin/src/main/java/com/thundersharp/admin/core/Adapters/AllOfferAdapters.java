package com.thundersharp.admin.core.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Model.OfferModel;

import java.util.List;

public class AllOfferAdapters extends RecyclerView.Adapter<AllOfferAdapters.View> {

    private static int adapterType= 0;

    public static AllOfferAdapters getInstance(Context context, List<Object> data,int AdapterType){
        adapterType=AdapterType;
        return new AllOfferAdapters(context,data);
    }

    private AllOfferAdapters(Context context, List<Object> objects) {
        this.context = context;
        this.objects = objects;
    }

    private Context context;
    private List<Object> objects;

    @NonNull
    @Override
    public View onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (adapterType == 0)
            return new View(LayoutInflater.from(context).inflate(R.layout.item_offer_admin,parent,false));
        else return new View(LayoutInflater.from(context).inflate(R.layout.item_offer_all_admin,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull View holder, int position) {

        if (adapterType == 0){

        }else {
            OfferModel offerModel =  (OfferModel) objects.get(position);
            ((View)holder).offer_apply.setOnClickListener(view ->{
                //TODO Tap to apply
            });
            ((View)holder).offer_desc.setText(offerModel.getDESC());
            ((View)holder).offer_text_amt.setText("Get instant discount of "+offerModel.getPERCENT() +"% Off upto Rs. "+offerModel.getUPTO()+" on your delisious orders."+ context.getString(R.string.offer_view_detail));
            ((View)holder).offer_code.setText(offerModel.getCODE());
        }

    }

    @Override
    public int getItemCount() {
        if (objects != null) return objects.size();else return 0;
    }

    class View extends RecyclerView.ViewHolder {

        ImageView offerBy;
        TextView offer_desc, offer_text_amt, offer_code, offer_apply;

        public View(@NonNull android.view.View itemView) {
            super(itemView);
            offerBy =itemView.findViewById(R.id.offerBy);
            offer_desc =itemView.findViewById(R.id.offer_desc);
            offer_text_amt =itemView.findViewById(R.id.offer_text_amt);
            offer_code =itemView.findViewById(R.id.offer_code);
            offer_apply =itemView.findViewById(R.id.offer_apply);

        }
    }
}
