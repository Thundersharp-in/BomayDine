package com.thundersharp.admin.core.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.thundersharp.admin.R;

public class AlertCreater {

    private Context context;
    private View view;
    private boolean isCreated = false;

    public AlertCreater(Context context) {
        this.context = context;
    }

    public static AlertCreater initialize(Context context){
        return new AlertCreater(context);
    }


    public Dialog createAlert(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        view = LayoutInflater.from(context).inflate(R.layout.layout_dialog,null,false);
        builder.setView(view);
        if (message != null) ((TextView)view.findViewById(R.id.textData)).setText(message);
        isCreated = true;
        return builder.create();
    }

    public void updateText(String message){
        if (isCreated && view != null){
            ((TextView)view.findViewById(R.id.textData)).setText(message);
        }
    }

}
