package com.thundersharp.bombaydine.user.core.login;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public interface FirebaseLoginClient {

    interface ActivityHandler{
        void postOtpSentListner(boolean isVerified, PhoneAuthCredential credential);
    }

    interface registerContract{
        void registerData(@NonNull String name, @NonNull String email, @NonNull String password);
    }

    interface loginContract{
        void loginwithfirebase(String PhoneNo);

        void loginwithfirebase(String email, String passWord);

        void loginfirebaseAuthWithGoogle(String idToken);


    }

    interface otpListner{
        void postOtpSent(@NonNull String verificationId,
                         @NonNull PhoneAuthProvider.ForceResendingToken token);
    }

    interface loginFailureListner{
        void setOnLoginFailureListner(Exception exception,int type);
    }

    interface loginSucessListner{
        void setOnLoginSucessListner( Task<AuthResult> task,boolean isDataRegisteredToDatabase, boolean isDataExists);
    }

    interface registerSucessFailureListner{

        void onRegisterSucessListner(Task<AuthResult> task,boolean isDataRegisteredToDatabase);

        void onRegisterFailureListner(Exception exception);
    }

}
