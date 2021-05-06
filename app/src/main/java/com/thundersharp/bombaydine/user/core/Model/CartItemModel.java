package com.thundersharp.bombaydine.user.core.Model;

public class CartItemModel {

    public static CartItemModel initializeValues(double AMOUNT, String DESC, int FOOD_TYPE, String ICON_URL, String NAME,String ID,int QUANTITY){
        return new CartItemModel(AMOUNT,DESC,FOOD_TYPE,ICON_URL,NAME,ID,QUANTITY);
    }

    public CartItemModel(){}

    private double AMOUNT;
    private String DESC;
    private int FOOD_TYPE;
    private String ICON_URL;
    private String NAME;
    private String ID;
    private int QUANTITY;


    public CartItemModel(double AMOUNT, String DESC, int FOOD_TYPE, String ICON_URL, String NAME, String ID, int QUANTITY) {
        this.AMOUNT = AMOUNT;
        this.DESC = DESC;
        this.FOOD_TYPE = FOOD_TYPE;
        this.ICON_URL = ICON_URL;
        this.NAME = NAME;
        this.ID = ID;
        this.QUANTITY = QUANTITY;
    }

    public int getQUANTITY() {
        return QUANTITY;
    }

    public void setQUANTITY(int QUANTITY) {
        this.QUANTITY = QUANTITY;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public double getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(double AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public String getDESC() {
        return DESC;
    }

    public void setDESC(String DESC) {
        this.DESC = DESC;
    }

    public int getFOOD_TYPE() {
        return FOOD_TYPE;
    }

    public void setFOOD_TYPE(int FOOD_TYPE) {
        this.FOOD_TYPE = FOOD_TYPE;
    }

    public String getICON_URL() {
        return ICON_URL;
    }

    public void setICON_URL(String ICON_URL) {
        this.ICON_URL = ICON_URL;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }
}
