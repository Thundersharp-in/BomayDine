package com.thundersharp.admin.core.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Model.ReportModel;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder>{

    Context context;
    List<ReportModel> reportModelList;

    public ReportAdapter(Context context, List<ReportModel> reportModelList) {
        this.context = context;
        this.reportModelList = reportModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_report,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReportModel reportModel = reportModelList.get(position);

        holder.report_id.setText("#"+reportModel.ID);
        holder.report_type.setText(reportModel.TYPE);
        holder.reporter_name.setText(reportModel.NAME);
        holder.reporter_email.setText(reportModel.EMAIL);
        holder.reported_date.setText(reportModel.ID);
        holder.phone_no.setText(reportModel.PHONE);
        //holder.report_status.setText(reportModel);


        /*
         ////////////////////////////////////////////////////////////////////////////
        || Report status 0 : ReportPlaced                         ::::           ||
        || Report status 1 : Report Reviewed by admin             ::::           ||
        || Report status 2 : Reported cancelled by user           ::::           ||
        || Report status 3 : Report cancelled by admin            ::::           ||
        || Report status 4 : Report Approved by admin             ::::           ||
        || Report status 5 : Problem Solved                       ::::           ||
        || Report status 6 : User Satisfied with Solution         ::::           ||
        || Report status 7 : User un-Satisfied with Solution      ::::           ||
        || Report status 8 : Reported Again                       ::::           ||
        ////////////////////////////////////////////////////////////////////////////
         */
        switch (reportModel.STATUS){
            case 0:
                holder.report_status.setText("Report Placed");
                holder.lower.setVisibility(View.VISIBLE);
                holder.btn_cancel.setVisibility(View.VISIBLE);

                //holder.status.setTextColor();
                break;
            case 1:
                holder.report_status.setText("Report Placed");
                holder.lower.setVisibility(View.VISIBLE);
                holder.btn_cancel.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.report_status.setText("Report Placed");
                holder.lower.setVisibility(View.VISIBLE);
                holder.btn_cancel.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.report_status.setText("Report Placed");
                holder.lower.setVisibility(View.VISIBLE);
                holder.btn_cancel.setVisibility(View.VISIBLE);
            default:
                holder.lower.setVisibility(View.GONE);
                holder.btn_cancel.setVisibility(View.GONE);
                holder.report_status.setText("Status : Unknown");
                //holder.status.setTextColor(0);
                break;

        }

    }

    @Override
    public int getItemCount() {
        if (reportModelList != null) return reportModelList.size();else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView report_id, report_type, reporter_name, reporter_email, reported_date, phone_no, report_status;
        LinearLayout lower;
        AppCompatButton verify, reply, btn_cancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            report_id = itemView.findViewById(R.id.report_id);
            report_type = itemView.findViewById(R.id.report_type);
            reporter_name = itemView.findViewById(R.id.reporter_name);
            reporter_email = itemView.findViewById(R.id.reporter_email);
            reported_date = itemView.findViewById(R.id.reported_date);
            phone_no = itemView.findViewById(R.id.phone_no);
            report_status = itemView.findViewById(R.id.report_status);
            lower = itemView.findViewById(R.id.lower);
            verify = itemView.findViewById(R.id.verify);
            reply = itemView.findViewById(R.id.reply);
            btn_cancel = itemView.findViewById(R.id.btn_cancel);

        }
    }
}
