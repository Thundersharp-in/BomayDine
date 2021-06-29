package com.thundersharp.bombaydine.user.core.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.core.Model.AddressData;
import com.thundersharp.bombaydine.user.core.address.SharedPrefHelper;
import com.thundersharp.bombaydine.user.core.address.SharedPrefUpdater;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;
import com.thundersharp.bombaydine.user.ui.home.HomeFragment;
import com.thundersharp.bombaydine.user.ui.location.AddAddressActivity;
import com.thundersharp.bombaydine.user.ui.location.AddressEdit;
import com.thundersharp.bombaydine.user.ui.location.LocationDetail;

import java.util.List;

public class AddressHolder extends RecyclerView.Adapter<AddressHolder.ViewHolder> implements SharedPrefUpdater.OnSharedprefUpdated {

    private Context context;
    private List<AddressData> addressData;
    private SharedPrefHelper sharedPrefHelper;
    private Boolean edit;

    public AddressHolder(Context context, List<AddressData> addressData, Boolean edit) {
        this.context = context;
        this.addressData = addressData;
        this.edit = edit;
        sharedPrefHelper = new SharedPrefHelper(context, this);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_location,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddressData addressDatainst = addressData.get(position);
        holder.title.setText(addressDatainst.getADDRESS_NICKNAME());
        if (addressDatainst.getADDRESS_NICKNAME().equalsIgnoreCase("Home")){
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_outline_home_24));
        }else if (addressDatainst.getADDRESS_NICKNAME().equalsIgnoreCase("Office")) {
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_outline_home_work_24));
        }else if (addressDatainst.getADDRESS_NICKNAME().equalsIgnoreCase("Others")){
            holder.icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_outline_share_location_24));
        }

        holder.address.setText(addressDatainst.getADDRESS_LINE1());//+","+addressDatainst.getADDRESS_LINE2()+","+addressDatainst.getCITY()+",Pin : "+addressDatainst.getZIP()

    }

    @Override
    public int getItemCount() {
        if (addressData != null) return addressData.size(); else return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView icon;
        private TextView title,address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            address = itemView.findViewById(R.id.address);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (edit) {
                sharedPrefHelper.SaveDataToSharedPref(addressData.get(getAdapterPosition()));
                ((AddAddressActivity) context).setResult(200, ((AddAddressActivity) context).getIntent().setData(Uri.parse(addressData.get(getAdapterPosition()).getADDRESS_LINE1())));
                ((AddAddressActivity) context).finish();
            }else context.startActivity(new Intent(context, LocationDetail.class).putExtra("locdet",addressData.get(getAdapterPosition())).putExtra("edit",true));
        }
    }

    @Override
    public void onSharedPrefUpdate(AddressData addressData) {
        //setResult(1356,getIntent().putExtra("data",uri));
       // HomeFragment.bottomSheetDialog.cancel();
        //HomeFragment.textcurrloc.setText(addressData.getADDRESS_NICKNAME()+": "+addressData.getADDRESS_LINE1());
    }
}

