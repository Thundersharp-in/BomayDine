package com.thundersharp.admin.core.Model;

import java.io.Serializable;

public class FoodItemAdapter implements Serializable {

    public FoodItemAdapter(){}

    public double AMOUNT;
    public boolean AVAILABLE;
    public String CAT_NAME_ID;
    public String DESC;
    public int FOOD_TYPE;
    public String ICON_URL;
    public String NAME;
    public String ID;


    public FoodItemAdapter(double AMOUNT, boolean AVAILABLE, String CAT_NAME_ID, String DESC, int FOOD_TYPE, String ICON_URL, String NAME, String ID) {
        this.AMOUNT = AMOUNT;
        this.AVAILABLE = AVAILABLE;
        this.CAT_NAME_ID = CAT_NAME_ID;
        this.DESC = DESC;
        this.FOOD_TYPE = FOOD_TYPE;
        this.ICON_URL = ICON_URL;
        this.NAME = NAME;
        this.ID = ID;
    }


}
