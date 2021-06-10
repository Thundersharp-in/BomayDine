package com.thundersharp.admin.core.Adapters;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.thundersharp.admin.R;
import com.thundersharp.admin.ui.location.CoOrdinatesUpdater;
import com.thundersharp.admin.ui.location.UpdatePoints;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CordinatesPointsAdapter extends RecyclerView.Adapter<CordinatesPointsAdapter.CoOrdinatesHolder>{

    Context context;
    List<LatLng> latLngs;

    public CordinatesPointsAdapter(Context context, List<LatLng> latLngs) {
        this.context = context;
        this.latLngs = latLngs;
    }

    @NonNull
    @Override
    public CoOrdinatesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new
                CoOrdinatesHolder(LayoutInflater.from(context).inflate(R.layout.holder_co_ordinates,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CoOrdinatesHolder holder, int position) {
        LatLng data = latLngs.get(position);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CO-ORDINATE").append("\n");
        stringBuilder.append(data.latitude).append(",").append(data.longitude).append("\n\n");

        stringBuilder.append("ADDRESS").append("\n");
        try {
            stringBuilder.append(getLocationfromLat(data.latitude,data.longitude).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
            stringBuilder.append("Failed to Load Address for this Co-ordinate");
        }

        holder.dataMain.setText(stringBuilder.toString());
        holder.update.setOnClickListener(v->{
            if (position+1 != latLngs.size())
                CoOrdinatesUpdater.main(context,data,position+1,latLngs.size());
            else Toast.makeText(context, "You can't edit this point.\nTo edit this point edit the starting point.", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        if (latLngs != null)return latLngs.size();else return 0;
    }

    class CoOrdinatesHolder extends RecyclerView.ViewHolder {

        TextView dataMain;
        RelativeLayout update;

        public CoOrdinatesHolder(@NonNull View itemView) {
            super(itemView);
            dataMain = itemView.findViewById(R.id.info);
            update = itemView.findViewById(R.id.update);
        }
    }

    @NonNull
    private Address getLocationfromLat(double lat, double longi) throws IOException {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        addresses = geocoder.getFromLocation(lat, longi, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        return addresses.get(0);
    }
}
