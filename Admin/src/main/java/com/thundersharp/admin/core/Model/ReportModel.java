package com.thundersharp.admin.core.Model;

public class ReportModel {
    public String NAME, EMAIL, UID, PHONE, MESSAGE, ID;
    public String TYPE;
    public int STATUS;

    public ReportModel() {
    }


    public ReportModel(String NAME, String EMAIL, String UID, String PHONE, String MESSAGE, String ID, String TYPE, int STATUS) {
        this.NAME = NAME;
        this.EMAIL = EMAIL;
        this.UID = UID;
        this.PHONE = PHONE;
        this.MESSAGE = MESSAGE;
        this.ID = ID;
        this.TYPE = TYPE;
        this.STATUS = STATUS;
    }
}
