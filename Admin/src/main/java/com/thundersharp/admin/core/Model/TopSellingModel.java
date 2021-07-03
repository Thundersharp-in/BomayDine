package com.thundersharp.admin.core.Model;

import java.io.Serializable;

public class TopSellingModel implements Serializable {
    public String IMAGES, ITEMID, NAME, NOOFORDERS;

    public TopSellingModel(String IMAGES, String ITEMID, String NAME, String NOOFORDERS) {
        this.IMAGES = IMAGES;
        this.ITEMID = ITEMID;
        this.NAME = NAME;
        this.NOOFORDERS = NOOFORDERS;
    }

}
