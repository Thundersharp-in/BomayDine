package com.thundersharp.admin.core.Model;

import java.io.Serializable;

public class CategoryData implements Serializable {

    public CategoryData(){}

    public String NAME,ID,IMAGES;

    public CategoryData(String NAME, String ID, String IMAGES) {
        this.NAME = NAME;
        this.ID = ID;
        this.IMAGES = IMAGES;
    }


}
