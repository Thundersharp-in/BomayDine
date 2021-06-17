package com.thundersharp.admin.core.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Model.OfferModel;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.admin.ui.edits.CreateOffer;
import com.thundersharp.admin.ui.offers.AllOffersActivity;

import java.io.Serializable;
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
            ((View)holder).offer_delete.setOnClickListener(view ->{
                FirebaseDatabase
                        .getInstance()
                        .getReference(CONSTANTS.DATABASE_NODE_ALL_OFFERS)
                        .child(offerModel.CODE)
                        .removeValue()
                        .addOnCompleteListener(task -> {
                           if (task.isSuccessful()){
                               Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                               objects.remove(position);
                               notifyDataSetChanged();
                           }

                           else Toast.makeText(context, "OOPS! Something went wrong try another time", Toast.LENGTH_SHORT).show();
                        });
            });
            ((View)holder).offer_desc.setText(offerModel.DESC);
            ((View)holder).offer_text_amt.setText("Get instant discount of "+offerModel.PERCENT +"% Off upto Rs. "+offerModel.UPTO+" on your delisious orders."+ context.getString(R.string.offer_view_detail));
            ((View)holder).offer_code.setText(offerModel.CODE);
            ((View)holder).edit_offer.setOnClickListener(view ->{
                Toast.makeText(context, "edit clicked", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, CreateOffer.class).putExtra("edit",true).putExtra("data", offerModel));
            });
        }

    }

    @Override
    public int getItemCount() {
        if (objects != null) return objects.size();else return 0;
    }

    class View extends RecyclerView.ViewHolder {

        ImageView offerBy, edit_offer;
        TextView offer_desc, offer_text_amt, offer_code, offer_delete;

        public View(@NonNull android.view.View itemView) {
            super(itemView);
            offerBy =itemView.findViewById(R.id.offerBy);
            offer_desc =itemView.findViewById(R.id.offer_desc);
            offer_text_amt =itemView.findViewById(R.id.offer_text_amt);
            offer_code =itemView.findViewById(R.id.offer_code);
            offer_delete =itemView.findViewById(R.id.offer_delete);
            edit_offer =itemView.findViewById(R.id.edit_offer);

        }
    }
}
