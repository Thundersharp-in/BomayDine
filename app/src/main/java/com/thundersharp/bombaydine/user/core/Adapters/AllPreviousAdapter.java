package com.thundersharp.bombaydine.user.core.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.PolyUtil;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Model.AddressData;
import com.thundersharp.bombaydine.user.core.address.CordinatesInteractor;
import com.thundersharp.bombaydine.user.core.address.Cordinateslistner;
import com.thundersharp.bombaydine.user.core.address.SharedPrefHelper;
import com.thundersharp.bombaydine.user.core.address.SharedPrefUpdater;
import com.thundersharp.bombaydine.user.core.location.StorageFailure;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.user.core.utils.LatLongConverter;
import com.thundersharp.bombaydine.user.ui.home.HomeFragment;
import com.thundersharp.bombaydine.user.ui.location.AddressEdit;

import java.util.Arrays;
import java.util.List;

public class AllPreviousAdapter extends RecyclerView.Adapter<AllPreviousAdapter.ViewHolder> implements
        SharedPrefUpdater.OnSharedprefUpdated, Cordinateslistner.fetchSuccessListener {

    private Context context;
    private List<AddressData> addressData;
    private SharedPrefHelper sharedPrefHelper;
    private CordinatesInteractor cordinatesInteractor;
    List<LatLng> latLngList;

    public AllPreviousAdapter(Context context, List<AddressData> addressData) {
        this.context = context;
        this.addressData = addressData;
        cordinatesInteractor = new CordinatesInteractor(this,context);
        cordinatesInteractor.fetchAllCoordinatesFromStorage();
        sharedPrefHelper = new SharedPrefHelper(context, this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_address,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddressData addressDatainst = addressData.get(position);
        if (sharedPrefHelper.getSavedHomeLocationData().getLAT_LONG().equalsIgnoreCase(addressDatainst.getLAT_LONG())){
            holder.tick.setVisibility(View.VISIBLE);
            holder.setok.setVisibility(View.GONE);
        }
        holder.tittle.setText(addressDatainst.getADDRESS_NICKNAME());
        holder.recentorders.setText(addressDatainst.getADDRESS_LINE1());//+","+addressDatainst.getADDRESS_LINE2()+","+addressDatainst.getCITY()+",Pin : "+addressDatainst.getZIP()

        holder.city.setText(addressDatainst.getCITY());

        boolean isInBounds = PolyUtil.containsLocation(LatLongConverter.initialize().getlatlang(addressDatainst.getLAT_LONG()),latLngList,false);
        if (!isInBounds){
            holder.recentorders.setTextColor(Color.RED);
        }
        holder.endicon.setOnClickListener(view -> {
                PopupMenu popup = new PopupMenu(context, holder.endicon);
                //inflating menu from xml resource
                popup.inflate(R.menu.popup_more_address);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.edit:
                                context.startActivity(new Intent(context, AddressEdit.class).putExtra("data", addressDatainst));
                                break;
                            case R.id.del:
                                if (sharedPrefHelper.getSavedHomeLocationData().getLAT_LONG().equalsIgnoreCase(addressDatainst.getLAT_LONG())) {
                                    Toast.makeText(context, "Cannot delete a primary location.", Toast.LENGTH_SHORT).show();
                                } else{
                                    FirebaseDatabase
                                            .getInstance()
                                            .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child(CONSTANTS.DATABASE_NODE_ADDRESS)
                                            .child(String.valueOf(addressDatainst.getID()))
                                            .removeValue()
                                            .addOnCompleteListener(task -> { //TODO UPdate to shared pref if bydefult contains
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
                                                    addressData.remove(position);
                                                    notifyDataSetChanged();
                                                } else
                                                    Toast.makeText(context, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                        }


                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            });


    }

    @Override
    public int getItemCount() {
        if (addressData != null) return addressData.size(); else return 0;
    }

    @Override
    public void onSharedPrefUpdate(AddressData addressData) {
        //AllPreviousAdapter.this.notifyDataSetChanged();
        context.sendBroadcast(new Intent("full"));
    }

    @Override
    public void onCordinatesSuccess(LatLng... coOrdinates) {
        latLngList = Arrays.asList(coOrdinates);
        notifyDataSetChanged();

    }

    @Override
    public void onCordinatesFailure(Exception exception) {
        if (exception instanceof StorageFailure){
            cordinatesInteractor.fetchAllCoordinates();
            Toast.makeText(context,"Storage failure",Toast.LENGTH_LONG).show();
        }else
            Toast.makeText(context, ""+exception.getMessage(), Toast.LENGTH_SHORT).show();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView endicon,tick;
        RelativeLayout update;
        private TextView tittle,recentorders,city,setok;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            city = itemView.findViewById(R.id.city);

            tittle = itemView.findViewById(R.id.tittle);
            recentorders = itemView.findViewById(R.id.addline1);
            endicon = itemView.findViewById(R.id.endicon);
            update = itemView.findViewById(R.id.update);
            tick = itemView.findViewById(R.id.tick);
            setok=itemView.findViewById(R.id.setok);

            update.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            boolean isInBounds = PolyUtil.containsLocation(LatLongConverter.initialize().getlatlang(addressData.get(getAdapterPosition()).getLAT_LONG()),latLngList,false);
            if (isInBounds) {
                if (sharedPrefHelper.getSavedHomeLocationData().getLAT_LONG().equalsIgnoreCase(addressData.get(getAdapterPosition()).getLAT_LONG())) {
                    Toast.makeText(context, "Already a primary location.", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(context).setMessage("Set this address as your default address ?").setPositiveButton("OK", (dialogInterface, i) -> {

                        sharedPrefHelper.SaveDataToSharedPref(addressData.get(getAdapterPosition()));



                    }).setNegativeButton("CANCEL", (dialogInterface, i) -> {

                    }).setCancelable(false).show();
                }
            }else {
                Toast.makeText(context, "Unserviceable location", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
