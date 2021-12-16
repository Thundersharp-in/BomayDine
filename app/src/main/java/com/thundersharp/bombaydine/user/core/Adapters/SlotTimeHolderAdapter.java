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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thundersharp.bombaydine.R;

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
        holder.timeH.setText(time.get(position));
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
                    }else container.setBackground(itemView.getResources().getDrawable(R.drawable.time_slot_bprder));
                }
            };

            itemView.getContext().registerReceiver(broadcastReceiver,new IntentFilter("posSlot"));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            view.getContext().sendBroadcast(new Intent("posSlot").putExtra("pos",getAdapterPosition()));

        }
    }

}
