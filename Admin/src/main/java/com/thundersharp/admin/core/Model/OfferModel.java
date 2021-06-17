package com.thundersharp.admin.core.Model;

import java.io.Serializable;

public class OfferModel implements Serializable {

    public OfferModel(){}

    public String CODE,DESC,TNC;
    public Integer PERCENT,TYPE,UPTO;

    public OfferModel(String CODE, String DESC, String TNC, Integer PERCENT, Integer TYPE, Integer UPTO) {
        this.CODE = CODE;
        this.DESC = DESC;
        this.TNC = TNC;
        this.PERCENT = PERCENT;
        this.TYPE = TYPE;
        this.UPTO = UPTO;
    }
}
