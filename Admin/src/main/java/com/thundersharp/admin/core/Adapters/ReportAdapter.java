package com.thundersharp.admin.core.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.Model.ReportModel;
import com.thundersharp.admin.core.utils.CONSTANTS;
import com.thundersharp.admin.core.utils.TimeUtils;

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
        holder.reporter_email.setText(reportModel.EMAIL);
        holder.reported_date.setText(TimeUtils.getTimeFromTimeStamp(reportModel.ID));

        holder.btn_delete.setOnClickListener(view-> {
            delete(reportModel.ID, position);

        });
        holder.verify.setOnClickListener(view-> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"amritasingh09092002@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Serious Issue report from admin registered by user. ID : "
                    +reportModel.ID);
            i.putExtra(Intent.EXTRA_TEXT   , "REPORT ID : "
                    +reportModel.ID+"\n\nREPORT TYPE : "
                            +reportModel.TYPE
                    +"\n\nMESSAGE : "+reportModel.MESSAGE
                    +"\n\n\n\nSENDER DETAILS : \n\nNAME : "+
                    reportModel.NAME+"\nEMAIL : "+reportModel.EMAIL
                    +"\nPHONE NO. : "+reportModel.PHONE+"\nREPORTED ON : "
                    +TimeUtils.getTimeFromTimeStamp(reportModel.ID));

            try {
                context.startActivity(Intent.createChooser(i, "Send mail..."));
                Toast.makeText(context, "Send!", Toast.LENGTH_SHORT).show();
                //delete(reportModel.ID, position);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
            }
        });

        //holder.reporter_name.setText(reportModel.NAME);
        //holder.phone_no.setText(reportModel.PHONE);
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

        /*
        switch (reportModel.STATUS){
            case 0:
                holder.report_status.setText("Report Placed");
                holder.lower.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.report_status.setText("Report Reviewed by admin");
                holder.lower.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.report_status.setText("Reported cancelled by user ");
                holder.lower.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.report_status.setText("Report cancelled by admin");
                holder.lower.setVisibility(View.VISIBLE);
                break;
            case 4:
                holder.report_status.setText("Report Approved by admin ");
                holder.lower.setVisibility(View.VISIBLE);
                break;
            case 5:
                holder.report_status.setText("Problem Solved ");
                holder.lower.setVisibility(View.VISIBLE);
                break;
            case 6:
                holder.report_status.setText("User Satisfied with Solution");
                holder.lower.setVisibility(View.VISIBLE);
                break;
            case 7:
                holder.report_status.setText("User un-Satisfied with Solution");
                holder.lower.setVisibility(View.VISIBLE);
                break;
            case 8:
                holder.report_status.setText("Reported Again");
                holder.lower.setVisibility(View.VISIBLE);
                break;
            default:
                holder.lower.setVisibility(View.GONE);
                holder.report_status.setText("Status : Unknown");
                //holder.status.setTextColor(0);
                break;

        }
         */

    }

    private void delete(String id, int position) {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_REPORT)
                .child(id)
                .removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

                        reportModelList.remove(position);
                        notifyDataSetChanged();

                    }else Toast.makeText(context, "DELETE FAILED : Try to delete again after sometime", Toast.LENGTH_SHORT).show();
                });


    }

    @Override
    public int getItemCount() {
        if (reportModelList != null) return reportModelList.size();else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView report_id, report_type, reporter_email, reported_date, report_status;//, reporter_name, phone_no,
        LinearLayout lower;
        AppCompatButton verify, btn_delete;//, reply

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            report_id = itemView.findViewById(R.id.report_id);
            report_type = itemView.findViewById(R.id.report_type);
            //reporter_name = itemView.findViewById(R.id.reporter_name);
            reporter_email = itemView.findViewById(R.id.reporter_email);
            reported_date = itemView.findViewById(R.id.reported_date);
           // phone_no = itemView.findViewById(R.id.phone_no);
            report_status = itemView.findViewById(R.id.report_status);
            lower = itemView.findViewById(R.id.lower);
            verify = itemView.findViewById(R.id.verify);
            //reply = itemView.findViewById(R.id.reply);
            btn_delete = itemView.findViewById(R.id.btn_delete);

        }
    }
}
