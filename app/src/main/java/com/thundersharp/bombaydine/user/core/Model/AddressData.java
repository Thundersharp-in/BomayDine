package com.thundersharp.bombaydine.user.core.Model;

import java.io.Serializable;

public class AddressData implements Serializable {

    public AddressData(){}

    private String ADDRESS_LINE1;
    private String ADDRESS_LINE2;
    private String ADDRESS_NICKNAME;
    private String CITY;
    private long ID;
    private String LAT_LONG;
    private int ZIP;

    public AddressData(String ADDRESS_LINE1, String ADDRESS_LINE2, String ADDRESS_NICKNAME, String CITY, long ID, String LAT_LONG, int ZIP) {
        this.ADDRESS_LINE1 = ADDRESS_LINE1;
        this.ADDRESS_LINE2 = ADDRESS_LINE2;
        this.ADDRESS_NICKNAME = ADDRESS_NICKNAME;
        this.CITY = CITY;
        this.ID = ID;
        this.LAT_LONG = LAT_LONG;
        this.ZIP = ZIP;
    }

    public String getADDRESS_LINE1() {
        return ADDRESS_LINE1;
    }

    public void setADDRESS_LINE1(String ADDRESS_LINE1) {
        this.ADDRESS_LINE1 = ADDRESS_LINE1;
    }

    public String getADDRESS_LINE2() {
        return ADDRESS_LINE2;
    }

    public void setADDRESS_LINE2(String ADDRESS_LINE2) {
        this.ADDRESS_LINE2 = ADDRESS_LINE2;
    }

    public String getADDRESS_NICKNAME() {
        return ADDRESS_NICKNAME;
    }

    public void setADDRESS_NICKNAME(String ADDRESS_NICKNAME) {
        this.ADDRESS_NICKNAME = ADDRESS_NICKNAME;
    }

    public String getCITY() {
        return CITY;
    }

    public void setCITY(String CITY) {
        this.CITY = CITY;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getLAT_LONG() {
        return LAT_LONG;
    }

    public void setLAT_LONG(String LAT_LONG) {
        this.LAT_LONG = LAT_LONG;
    }

    public int getZIP() {
        return ZIP;
    }

    public void setZIP(int ZIP) {
        this.ZIP = ZIP;
    }


}
