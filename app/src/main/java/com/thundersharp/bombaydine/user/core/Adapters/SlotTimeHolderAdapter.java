package com.thundersharp.bombaydine.user.core.Adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thundersharp.bombaydine.R;
import com.thundersharp.bombaydine.user.ui.tableBooking.TableBookingMain;

import java.util.List;

public class SlotTimeHolderAdapter extends RecyclerView.Adapter<SlotTimeHolderAdapter.ViewHoldr> {

    List<String> time;
    Integer selectedPos;

    public SlotTimeHolderAdapter(List<String> time) {
        this.time = time;
    }


    @NonNull
    @Override
    public ViewHoldr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHoldr(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_slots,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoldr holder, int position) {
        String startTime = time.get(position).substring(0,time.get(position).indexOf("-"));
        String endTime = time.get(position).substring(time.get(position).indexOf("-")+1);

        if (Integer.parseInt(startTime) > 12) {
            holder.timeH.setText((Integer.parseInt(startTime) - 12) + "PM - " + (Integer.parseInt(endTime) - 12)+"PM");
        }else if (Integer.parseInt(startTime) == 12){
            holder.timeH.setText(startTime + "PM - " + (Integer.parseInt(endTime) - 12)+"PM");
        }else {
            holder.timeH.setText(startTime + "AM - " + endTime+"AM");
        }

    }

    @Override
    public int getItemCount() {
        if (time != null) return time.size(); else return 0;
    }

    class ViewHoldr extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView timeH;
        private RelativeLayout container;

        public ViewHoldr(@NonNull View itemView) {
            super(itemView);
            timeH = itemView.findViewById(R.id.timeHOlder);
            container = itemView.findViewById(R.id.container);
            BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    selectedPos = intent.getIntExtra("pos",0);
                    if (selectedPos == getAdapterPosition()){
                        container.setBackground(itemView.getResources().getDrawable(R.drawable.time_slot_bprder_selected));
                        TableBookingMain.time_slot = time.get(getAdapterPosition());
                    }else container.setBackground(itemView.getResources().getDrawable(R.drawable.time_slot_bprder));
                }
            };

            itemView.getContext().registerReceiver(broadcastReceiver,new IntentFilter("posSlot"));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            view.getContext().sendBroadcast(new Intent("posSlot").putExtra("timeSlot",time.get(getAdapterPosition())).putExtra("pos",getAdapterPosition()));

        }
    }

}
