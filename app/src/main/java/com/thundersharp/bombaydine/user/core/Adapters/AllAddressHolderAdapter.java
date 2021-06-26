package com.thundersharp.bombaydine.user.core.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Model.AddressData;
import com.thundersharp.bombaydine.user.core.address.SharedPrefHelper;
import com.thundersharp.bombaydine.user.core.address.SharedPrefUpdater;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.user.ui.home.HomeFragment;
import com.thundersharp.bombaydine.user.ui.location.AddressEdit;

import java.util.List;

public class AllAddressHolderAdapter extends RecyclerView.Adapter<AllAddressHolderAdapter.ViewHolder> implements SharedPrefUpdater.OnSharedprefUpdated {

    private Context context;
    private List<AddressData> addressData;
    private SharedPrefHelper sharedPrefHelper;

    public AllAddressHolderAdapter(Context context, List<AddressData> addressData) {
        this.context = context;
        this.addressData = addressData;
        sharedPrefHelper = new SharedPrefHelper(context, this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.addressholder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddressData addressDatainst = addressData.get(position);
        holder.tittle.setText(addressDatainst.getADDRESS_NICKNAME());
        holder.recentorders.setText(addressDatainst.getADDRESS_LINE1());//+","+addressDatainst.getADDRESS_LINE2()+","+addressDatainst.getCITY()+",Pin : "+addressDatainst.getZIP()

        holder.endicon.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(context, holder.endicon);
            //inflating menu from xml resource
            popup.inflate(R.menu.popup_more_address);
            //adding click listener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.edit :
                            context.startActivity(new Intent(context, AddressEdit.class).putExtra("data",addressDatainst));
                            break;
                        case R.id.del :
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child(CONSTANTS.DATABASE_NODE_ADDRESS)
                                    .child(String.valueOf(addressDatainst.getID()))
                                    .removeValue()
                                    .addOnCompleteListener(task -> { //TODO UPdate to shared pref if bydefult contains
                                        if (task.isSuccessful()){
                                            Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
                                            addressData.remove(position);
                                            notifyDataSetChanged();
                                        }else
                                            Toast.makeText(context, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    });

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
        HomeFragment.bottomSheetDialog.cancel();
        HomeFragment.textcurrloc.setText(addressData.getADDRESS_NICKNAME()+": "+addressData.getADDRESS_LINE1());
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView homeicon,endicon;
        private TextView tittle,recentorders;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            homeicon = itemView.findViewById(R.id.homeicon);
            tittle = itemView.findViewById(R.id.tittle);
            recentorders = itemView.findViewById(R.id.recentorders);
            endicon = itemView.findViewById(R.id.endicon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            sharedPrefHelper.SaveDataToSharedPref(addressData.get(getAdapterPosition()));
        }
    }
}
