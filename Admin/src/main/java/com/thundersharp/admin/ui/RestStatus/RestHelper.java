package com.thundersharp.admin.ui.RestStatus;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thundersharp.admin.core.utils.CONSTANTS;

public class RestHelper implements RestStatus{

    static RestHelper restHelper;
    boolean status = false;
    RestStatus.updateRestStatus OnSuccessFailureListner;
    Context context;

    public static RestHelper getInstance(){
        restHelper = new RestHelper();
        return restHelper;
    }

    public RestHelper getReference(Context context){
        this.context = context;
        return restHelper;
    }
    public RestHelper setValue(boolean status, RestStatus.updateRestStatus OnSuccessFailureListner){
        this.OnSuccessFailureListner = OnSuccessFailureListner;
        restHelper.setRestStatus(status);
        return restHelper;
    }
    public RestHelper getValue(RestStatus.updateRestStatus OnSuccessFailureListner){
        this.OnSuccessFailureListner = OnSuccessFailureListner;
        isOpen();
        return restHelper;
    }

    public RestHelper() {}

    @Override
    public void isOpen() {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_RESTURANT_STATUS)
                .child(CONSTANTS.DATABASE_RESTURANT_OPEN)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            OnSuccessFailureListner.onSuccess(snapshot.getValue(Boolean.class));
                        }else {
                            OnSuccessFailureListner.onSuccess(false);
                            //status = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        OnSuccessFailureListner.onFailure(error.toException());
                    }
                });
        //return status;
    }

    @Override
    public void setRestStatus(boolean isOpen) {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_RESTURANT_STATUS)
                .child(CONSTANTS.DATABASE_RESTURANT_OPEN)
                .setValue(isOpen)
                .addOnCompleteListener(task ->{
                    if (task.isSuccessful()){
                        OnSuccessFailureListner.onSuccess(isOpen);
                    }else {
                        OnSuccessFailureListner.onFailure(task.getException());
                    }

                });
    }
}
