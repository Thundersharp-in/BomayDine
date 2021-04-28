package com.thundersharp.bombaydine.user.core.Model;

import java.io.Serializable;

public class FoodItemAdapter implements Serializable {

    public FoodItemAdapter(){}

    private double AMOUNT;
    private boolean AVAILABLE;
    private String CAT_NAME_ID;
    private String DESC;
    private int FOOD_TYPE;
    private String ICON_URL;
    private String NAME;

    public FoodItemAdapter(double AMOUNT, boolean AVAILABLE, String CAT_NAME_ID, String DESC, int FOOD_TYPE, String ICON_URL, String NAME) {
        this.AMOUNT = AMOUNT;
        this.AVAILABLE = AVAILABLE;
        this.CAT_NAME_ID = CAT_NAME_ID;
        this.DESC = DESC;
        this.FOOD_TYPE = FOOD_TYPE;
        this.ICON_URL = ICON_URL;
        this.NAME = NAME;
    }

    public double getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(double AMOUNT) {
        this.AMOUNT = AMOUNT;
    }

    public boolean isAVAILABLE() {
        return AVAILABLE;
    }

    public void setAVAILABLE(boolean AVAILABLE) {
        this.AVAILABLE = AVAILABLE;
    }

    public String getCAT_NAME_ID() {
        return CAT_NAME_ID;
    }

    public void setCAT_NAME_ID(String CAT_NAME_ID) {
        this.CAT_NAME_ID = CAT_NAME_ID;
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
