package com.thundersharp.bombaydine.user.ui.home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.thundersharp.bombaydine.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import java.io.File;

public class MainPage extends AppCompatActivity {

    //permissions
    public static String FOLDER_PDF= Environment.getExternalStorageDirectory() + File.separator+"BombayDine/Orders";
    String[] permissionList = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION};
    private static final int PERMISSION_CALLBACK = 111;
    private static final int PERMISSION_REQUEST = 222;



    public static NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setDuplicateParentStateEnabled(false);
        loadPermission();
        //checkForPermissions();
       
        navView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


    }

    public void ChkPerm(){
        if(forSelfPermission()){

            if(shouldShow()){
                permissionCallBack();
            }  else {
                //just request the permission
                ActivityCompat.requestPermissions(MainPage.this,permissionList, PERMISSION_CALLBACK);
            }
        } else {
            //You already have the permission, just go ahead.
            afterPermission();
        }
    }


    public void loadPermission(){
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
            ChkPerm();
        }else{
            afterPermission();
        }
    }

    private void afterPermission() {

        File folderPdf=new File(FOLDER_PDF);

        if (!folderPdf.exists()){
            Toast.makeText(this, ""+folderPdf.mkdirs()+folderPdf.getPath(), Toast.LENGTH_SHORT).show();;

        }

    }


    /*
    private void checkForPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    101);
        } else {
            //initializeViews();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == 101) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //initializeFragments();
            } else {
                openAlertDialog();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



  private void openAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("This app requires your location to function!").setCancelable(false);
        alertDialogBuilder.setPositiveButton("Try again",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        checkForPermissions();
                    }
                });

        alertDialogBuilder.setNegativeButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setData(Uri.parse("package:com.thundersharp.bombaydine"));
                startActivity(i);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK){
            //check if all permissions are granted
            boolean allgranted = false;
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(allgranted){
                afterPermission();
            } else if(shouldShow()){

                permissionCallBack();
            } else {

                permissionSettings();
                Toast.makeText(getBaseContext(),"Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean forSelfPermission(){
        boolean allgranted = false;
        for(int i=0;i<permissionList.length;i++){

            if (ActivityCompat.checkSelfPermission(MainPage.this, permissionList[i]) != PackageManager.PERMISSION_GRANTED) {
                allgranted = true;
                break;
            } else {
                allgranted = false;
            }
        }

        if (allgranted){
            return true;
        }else{
            return false;
        }

    }

    private boolean resultPermission(){
        boolean allgranted = false;
        for(int i=0;i<permissionList.length;i++){

            if (ActivityCompat.checkSelfPermission(MainPage.this, permissionList[i]) == PackageManager.PERMISSION_GRANTED) {
                allgranted = true;
            } else {
                allgranted = false;
                break;
            }
        }

        if (allgranted){
            return true;
        }else{
            return false;
        }

    }


    private boolean shouldShow(){

        boolean allgranted = false;
        for(int i=0;i<permissionList.length;i++){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainPage.this, permissionList[i])) {
                allgranted = true;
                break;
            } else {
                allgranted = false;
            }
        }

        if (allgranted){
            return true;
        }else{
            return false;
        }
    }

    private void permissionCallBack(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
        builder.setTitle("Need Multiple Permissions");
        builder.setMessage("This app needs Multiple permissions.");
        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ActivityCompat.requestPermissions(MainPage.this,permissionList, PERMISSION_CALLBACK);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void permissionSettings(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
        builder.setTitle("Need Multiple Permissions");
        builder.setMessage("This app needs permission allow them from settings.");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, PERMISSION_REQUEST);
                // Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("R","result");
        if (requestCode == PERMISSION_REQUEST) {

            if (resultPermission()){
                Log.d("R","result s");
                afterPermission();
            }else{
                Log.d("R","result c");
                ChkPerm();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //runFadeoutAnimation();
    }
}