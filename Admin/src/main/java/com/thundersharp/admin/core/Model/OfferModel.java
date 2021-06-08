package com.thundersharp.admin.core.Model;

public class OfferModel {

    public OfferModel(){}

    private String CODE,DESC,TNC;
    private Integer PERCENT,TYPE,UPTO;

    public OfferModel(String CODE, String DESC, String TNC, Integer PERCENT, Integer TYPE, Integer UPTO) {
        this.CODE = CODE;
        this.DESC = DESC;
        this.TNC = TNC;
        this.PERCENT = PERCENT;
        this.TYPE = TYPE;
        this.UPTO = UPTO;
    }

    public String getCODE() {
        return CODE;
    }

    public void setCODE(String CODE) {
        this.CODE = CODE;
    }

    public String getDESC() {
        return DESC;
    }

    public void setDESC(String DESC) {
        this.DESC = DESC;
    }

    public String getTNC() {
        return TNC;
    }

    public void setTNC(String TNC) {
        this.TNC = TNC;
    }

    public Integer getPERCENT() {
        return PERCENT;
    }

    public void setPERCENT(Integer PERCENT) {
        this.PERCENT = PERCENT;
    }

    public Integer getTYPE() {
        return TYPE;
    }

    public void setTYPE(Integer TYPE) {
        this.TYPE = TYPE;
    }

    public Integer getUPTO() {
        return UPTO;
    }

    public void setUPTO(Integer UPTO) {
        this.UPTO = UPTO;
    }
}
