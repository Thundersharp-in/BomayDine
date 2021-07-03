package com.thundersharp.bombaydine.user.core.Model;

import com.google.firebase.auth.PhoneAuthCredential;

import java.io.Serializable;

public class ChangeNumber implements Serializable {
    Boolean isVerified;
    PhoneAuthCredential phoneAuthCredential;
    String number;

    public ChangeNumber(Boolean isVerified, PhoneAuthCredential phoneAuthCredential, String number) {
        this.isVerified = isVerified;
        this.phoneAuthCredential = phoneAuthCredential;
        this.number = number;
    }

    public ChangeNumber() {
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public PhoneAuthCredential getPhoneAuthCredential() {
        return phoneAuthCredential;
    }

    public void setPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        this.phoneAuthCredential = phoneAuthCredential;
    }

    public String getNumber() {
        return number;
    }
}
