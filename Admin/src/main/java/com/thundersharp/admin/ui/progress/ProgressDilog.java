package com.thundersharp.admin.ui.progress;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import com.thundersharp.admin.R;

public class ProgressDilog {

    Context context;
    private AlertDialog.Builder builder;
    private static Dialog dialog;


    public ProgressDilog(Context context) {
        this.context = context;
        instantiateDilog(context);
    }

    private void instantiateDilog(Context context) {

        builder = new AlertDialog.Builder(context);
        View dialogview = LayoutInflater.from(context).inflate(R.layout.progress_dialog_admin,null,false);
        builder.setView(dialogview);
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public ProgressDilog show(){
        dialog.show();
        return this;
    }

    public ProgressDilog stop(){
        dialog.dismiss();
        return this;
    }
}
