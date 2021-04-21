package com.thundersharp.bombaydine.user.core.Model;

import java.io.Serializable;

public class CategoryData implements Serializable {

    public CategoryData(){}

    private String NAME,ID,IMAGES;

    public CategoryData(String NAME, String ID, String IMAGES) {
        this.NAME = NAME;
        this.ID = ID;
        this.IMAGES = IMAGES;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIMAGES() {
        return IMAGES;
    }

    public void setIMAGES(String IMAGES) {
        this.IMAGES = IMAGES;
    }
}
