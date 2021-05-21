package com.thundersharp.bombaydine.user.core.payments;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.bombaydine.user.core.Model.CartItemModel;
import com.thundersharp.bombaydine.user.core.Model.OrederBasicDetails;
import com.thundersharp.bombaydine.user.core.utils.CONSTANTS;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.login.LoginException;

public class PrePayment {

    private static PrePayment prePayment;
    private parePayListener parePayListener;

    public static PrePayment getInstance(){
        prePayment=new PrePayment();
        return prePayment;
    }

    public PrePayment setOrderToDatabase(List<CartItemModel> cartData, OrederBasicDetails orederBasicDetails){

        if (cartData != null) {

            for (int i = 0; i < cartData.size(); i++) {
                cartData.get(i).setDESC(null);
                cartData.get(i).setICON_URL(null);
                cartData.get(i).setID(null);
            }
        }

        if (parePayListener != null){
            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                HashMap<String,Object> dataNodes = new HashMap<>();

                dataNodes.put(CONSTANTS.DATABASE_NODE_ALL_USERS+"/"+FirebaseAuth.getInstance().getUid()+"/"+CONSTANTS.DATABASE_NODE_ORDERS+"/"+CONSTANTS.DATABASE_NODE_OVERVIEW+"/"+orederBasicDetails.getOrderID(),orederBasicDetails);

                dataNodes.put(CONSTANTS.DATABASE_NODE_ALL_USERS+"/"+FirebaseAuth.getInstance().getUid()+"/"+CONSTANTS.DATABASE_NODE_ORDERS+"/"+CONSTANTS.DATABASE_NODE_DETAILS+"/"+orederBasicDetails.getOrderID(),cartData);

                //TODO UPDATE DATA TO ADMIN LOCATION
                orederBasicDetails.setUid(FirebaseAuth.getInstance().getUid());
                orederBasicDetails.setStatus("0");
                dataNodes.put(CONSTANTS.DATABASE_NODE_ALL_ORDERS+"/"+getDate()+"/"+orederBasicDetails.getOrderID(),orederBasicDetails);

                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .updateChildren(dataNodes)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    parePayListener.addSuccess();
                                }else {
                                    parePayListener.addFailure(task.getException());
                                }
                            }
                        });


            }else {
                parePayListener.addFailure(new LoginException("not Logged in"));
            }
        }
        return prePayment;
    }

    public PrePayment setDadaistListener(parePayListener parePayListener){
        this.parePayListener = parePayListener;

        return prePayment;
    }

    private String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("DD-MM-YYYY");
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }


}


