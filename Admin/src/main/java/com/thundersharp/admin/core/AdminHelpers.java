package com.thundersharp.admin.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.thundersharp.admin.core.Errors.DataBaseException;
import com.thundersharp.admin.core.Errors.ResignException;
import com.thundersharp.admin.core.utils.CONSTANTS;

import java.util.HashMap;

public class AdminHelpers {

    private Context context;
    static AdminHelpers adminHelpers;
    private String[] dbPaths;
    private String dbPath;
    private Update update;

    public AdminHelpers(Context context) {
        this.context = context;
    }

    public static AdminHelpers getInstance(Context context) {
        adminHelpers = new AdminHelpers(context);
        return adminHelpers;
    }

    public AdminHelpers setExternalDeletePaths(String... dbPaths) {
        this.dbPaths = dbPaths;
        return adminHelpers;
    }

    public AdminHelpers setExternalUpdatePaths(String... dbPaths) {
        this.dbPaths = dbPaths;
        return adminHelpers;
    }

    public AdminHelpers setSingleitemIdToDelete(String itemId) {
        this.dbPath = itemId;
        return adminHelpers;
    }

    public void clearAllData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("EmpAccount", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void deletePaths() {
        if (dbPath != null || dbPaths != null) {
            if (dbPaths != null) {

                HashMap<String, Object> updateData = new HashMap<>();

                for (int i = 0; i < dbPaths.length; i++) {
                    updateData.put(dbPaths[i], null);
                }

                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .updateChildren(updateData)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if (update != null) update.updateSuccess();
                                    Toast.makeText(context, "All paths removed", Toast.LENGTH_SHORT).show();
                                } else
                                if (update != null) update.updateFailure();

                            }
                        });

            } else {

                FirebaseDatabase
                        .getInstance()
                        .getReference(CONSTANTS.DATABASE_NODE_ALL_ITEMS)
                        .child(dbPath)
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if (update != null) update.updateSuccess();

                                    Toast.makeText(context, "All paths removed", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (update != null) update.updateFailure();

                                }
                            }
                        });


            }
        }
    }


    public void updateTOPaths(Object data) {
        if (dbPath != null || dbPaths != null) {
            if (dbPaths != null) {

                HashMap<String, Object> updateData = new HashMap<>();

                for (int i = 0; i < dbPaths.length; i++) {
                    updateData.put(dbPaths[i], data);
                }

                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .updateChildren(updateData)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if (update != null) update.updateSuccess();
                                    Toast.makeText(context, "All paths updated", Toast.LENGTH_SHORT).show();
                                } else
                                if (update != null) update.updateFailure();

                            }
                        });

            }
        }
    }



    public AdminHelpers setListner(Update update){
        this.update = update;
        return adminHelpers;
    }

    public void updateStatus(boolean status) {
            if (dbPaths != null) {

                HashMap<String, Object> updateData = new HashMap<>();

                for (int i = 0; i < dbPaths.length; i++) {
                    updateData.put(dbPaths[i], status);
                }

                FirebaseDatabase
                        .getInstance()
                        .getReference()
                        .updateChildren(updateData)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Availability changed globally to : "+status, Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(context, " ERROR : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            } else {

                FirebaseDatabase
                        .getInstance()
                        .getReference(CONSTANTS.DATABASE_NODE_ALL_ITEMS)
                        .child(dbPath)
                        .child("AVAILABLE")
                        .setValue(status)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(context, "Availability changed to : "+status, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, " ERROR : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }

    }

    public void reSignAdmin() throws ResignException {
        if (TokenVerificationAdmin.getInstance(context).isDataNullOrEmpty()) {
            SharedPreferences sharedPreferences = TokenVerificationAdmin.getInstance(context).getSharedPrefs();
            if (FirebaseAuth.getInstance().getCurrentUser() != null) FirebaseAuth.getInstance().signOut();
            FirebaseAuth
                    .getInstance()
                    .signInWithEmailAndPassword(sharedPreferences.getString(TokenVerificationAdmin.SHARED_PREF_EMAIL_ADMIN, "temp@error.com"),
                            sharedPreferences.getString(TokenVerificationAdmin.SHARED_PREF_PASSWORD_ADMIN, "123456"))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(context, "Signed in back", Toast.LENGTH_SHORT).show();
                                //finish();

                            } else {
                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }else throw new ResignException("EC:783 Error in resigning either saved password or email id of the admin is null or empty."+"\n\nSUGGESTION :Logout the existing user for security purpose and ask admin to resign to his/her account.");
    }

    public void clearAllAdminData(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("EmpAccount", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public interface Update{
        void updateSuccess();
        void updateFailure();
    }
}
