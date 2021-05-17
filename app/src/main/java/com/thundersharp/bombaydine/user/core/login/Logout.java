package com.thundersharp.bombaydine.user.core.login;

import com.google.firebase.auth.FirebaseAuth;

public class Logout implements LogoutInterface{

    public static void logout(){
        new Logout().logoutNow();
    }

    @Override
    public void logoutNow(){
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            FirebaseAuth.getInstance().signOut();
    }
}

interface LogoutInterface{
    void logoutNow();
}
