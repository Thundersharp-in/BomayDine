package com.thundersharp.admin.ui.passcode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.thundersharp.admin.R;
import com.thundersharp.admin.core.AdminHelpers;
import com.thundersharp.admin.ui.AdminMain;

import java.io.Serializable;


public class PasscodeView extends AppCompatActivity {

    com.thundersharp.passcode.PasscodeView passcodeView;
    SharedPreferences code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode_view);


        passcodeView = findViewById(R.id.passcodeview);
        code = getSharedPreferences("security",MODE_PRIVATE);

        if (getIntent().getBooleanExtra("type",false)){
            passcodeView.setPasscodeType(com.thundersharp.passcode.PasscodeView.PasscodeViewType.TYPE_SET_PASSCODE);
            passcodeView.setFirstInputTip("Create your 4 digit pin");
            passcodeView.setSecondInputTip("Re-enter your 4 digit pin");
            passcodeView.setListener(new com.thundersharp.passcode.PasscodeView.PasscodeViewListener() {
                @Override
                public void onFail(String wrongNumber) {
                    Toast.makeText(PasscodeView.this, wrongNumber+" mismatched !!", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onSuccess(String number) {
                    SharedPreferences.Editor editor = code.edit();
                    editor.putInt("code",Integer.parseInt(number));
                    editor.apply();

                    AlertDialog.Builder builder = new AlertDialog.Builder(PasscodeView.this);
                    builder.setCancelable(false);
                    builder.setMessage("Keep your pin safe if you will loose your pin then you have to clear your app data and re-login");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            startActivity(new Intent(PasscodeView.this, AdminMain.class));
                        }
                    }).show();
                }
            });

        }else {
            passcodeView.setFirstInputTip("Enter your 4 digit pin");
            passcodeView
                    .setLocalPasscode(String.valueOf(code.getInt("code",0000)))
                    .setPasscodeType(com.thundersharp.passcode.PasscodeView.PasscodeViewType.TYPE_CHECK_PASSCODE)
                    .setListener(new com.thundersharp.passcode.PasscodeView.PasscodeViewListener() {
                        @Override
                        public void onFail(String wrongNumber) {
                            Toast.makeText(PasscodeView.this, wrongNumber+" is not your current pin !!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(String number) {
                            finish();
                            startActivity(new Intent(PasscodeView.this, AdminMain.class));
                        }
                    });


        }

    }
}