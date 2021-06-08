package com.thundersharp.admin.core.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;
import com.thundersharp.admin.R;

import java.util.List;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder> {

    private List<String> mFeedList;
    private Context mContext;

    private int orderStatus;
    private LayoutInflater mLayoutInflater;

    public TimeLineAdapter(List<String> feedList, int orderStatus) {
        mFeedList = feedList;
        this.orderStatus = orderStatus;
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public TimeLineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mLayoutInflater = LayoutInflater.from(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_timeline_admin,parent,false);

        return new TimeLineViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(TimeLineViewHolder holder, int position) {

       // TimeLineModel timeLineModel = mFeedList.get(position);

        if (position == 3){
            holder.mTimelineView.setMarker(mContext.getDrawable(R.drawable.circle_normal_delivered));
        }else if (position == 2){
            holder.mTimelineView.setMarker(mContext.getDrawable(R.drawable.circle_normal_out_for_delivry));
            holder.itemView.setOnClickListener(view -> {
                Toast.makeText(mContext, "No delivery boy no", Toast.LENGTH_SHORT).show();
            });
        }else if (position == 1){
            holder.mTimelineView.setMarker(mContext.getDrawable(R.drawable.round));
        }else if (position == 0){
            holder.mTimelineView.setMarker(mContext.getDrawable(R.drawable.circle_normal_ordered));
        }

       // Toast.makeText(mContext, ""+orderStatus, Toast.LENGTH_SHORT).show();

        switch (orderStatus){
            case 0:
                break;
            case 1:

                if (position ==0){
                    holder.mTimelineView.setMarker(mContext.getDrawable(R.drawable.circle_blue_order_placed));
                    holder.mTimelineView.setEndLineColor(mContext.getResources().getColor(R.color.blue),getItemViewType(position));

                }
                if (position == 1) {
                    holder.mTimelineView.setStartLineColor(mContext.getResources().getColor(R.color.blue), getItemViewType(position));
                    holder.mTimelineView.setMarker(mContext.getDrawable(R.drawable.drawable_blue));
                    holder.status.setText("We have accepted your order your food is being prepared, for any special instructions you can call us.");
                }
                break;

            case 2:

                if (position ==0){
                    holder.mTimelineView.setMarker(mContext.getDrawable(R.drawable.circle_blue_order_placed));
                    holder.mTimelineView.setEndLineColor(mContext.getResources().getColor(R.color.blue),getItemViewType(position));

                }
                if (position == 1) {
                    holder.mTimelineView.setStartLineColor(mContext.getResources().getColor(R.color.blue), getItemViewType(position));
                    holder.mTimelineView.setEndLineColor(mContext.getResources().getColor(R.color.blue), getItemViewType(position));
                    holder.mTimelineView.setMarker(mContext.getDrawable(R.drawable.drawable_blue));
                    holder.status.setText("We have accepted your order your food is being prepared.");
                }

                if (position ==2) {
                    holder.mTimelineView.setMarker(mContext.getDrawable(R.drawable.circle_blue_out_for_delivery));
                    holder.mTimelineView.setStartLineColor(mContext.getResources().getColor(R.color.blue), getItemViewType(position));
                    holder.status.setText("Your order is picked up by our delivery executive you can contact him on : 987543210");
                }
                break;

            case 3:

                if (position ==0){
                    holder.mTimelineView.setMarker(mContext.getDrawable(R.drawable.circle_blue_order_placed));
                    holder.mTimelineView.setEndLineColor(mContext.getResources().getColor(R.color.blue),getItemViewType(position));

                }
                if (position == 1) {
                    holder.mTimelineView.setMarker(mContext.getDrawable(R.drawable.drawable_blue));
                    holder.mTimelineView.setStartLineColor(mContext.getResources().getColor(R.color.blue), getItemViewType(position));
                    holder.mTimelineView.setEndLineColor(mContext.getResources().getColor(R.color.blue), getItemViewType(position));
                    holder.status.setText("We have accepted your order your food is being prepared.");

                }

                if (position ==2) {
                    holder.mTimelineView.setMarker(mContext.getDrawable(R.drawable.circle_blue_out_for_delivery));
                    holder.mTimelineView.setStartLineColor(mContext.getResources().getColor(R.color.blue), getItemViewType(position));
                    holder.mTimelineView.setEndLineColor(mContext.getResources().getColor(R.color.blue), getItemViewType(position));
                    holder.status.setText("Your order is picked up by our delivery executive.");
                }

                if (position ==3) {
                    holder.mTimelineView.setMarker(mContext.getDrawable(R.drawable.circle_blue_deliverd));
                    holder.mTimelineView.setStartLineColor(mContext.getResources().getColor(R.color.blue), getItemViewType(position));
                    holder.status.setText("Your order was successfully delivered");
                }
                break;
            case 4:



            case 5:

            case 6:
            default:
        }
        holder.tittle.setText(mFeedList.get(position));

       // holder.mMessage.setText(timeLineModel.getMessage());
    }

    @Override
    public int getItemCount() {
        return (mFeedList!=null? mFeedList.size():0);
    }

    class TimeLineViewHolder extends RecyclerView.ViewHolder{
        public  TimelineView mTimelineView;
        TextView tittle,status;

        public TimeLineViewHolder(View itemView, int viewType) {
            super(itemView);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.timeline);
            mTimelineView.initLine(viewType);

            tittle = itemView.findViewById(R.id.dummy);
            status = itemView.findViewById(R.id.statusdes);
        }
    }

}
