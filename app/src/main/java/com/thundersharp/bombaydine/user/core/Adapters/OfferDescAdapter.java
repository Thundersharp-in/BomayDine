package com.thundersharp.bombaydine.user.core.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.thundersharp.bombaydine.R;
import java.util.List;

public class OfferDescAdapter extends RecyclerView.Adapter<OfferDescAdapter.ViewHolder> {

    private Context context;
    private List<String> stringList;

    public OfferDescAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OfferDescAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.offer_t_and_c,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tc.setText(stringList.get(position));
    }

    @Override
    public int getItemCount() {
        if (stringList != null) return stringList.size(); else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tc = itemView.findViewById(R.id.tc);

        }
    }
}
