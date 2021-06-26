package com.thundersharp.admin.core.Model;

import java.io.Serializable;

public class SliderModel implements Serializable {
    public int PAGE, ID;
    public String URL;

    public SliderModel() {
    }

    public SliderModel(int PAGE, int ID, String URL) {
        this.PAGE = PAGE;
        this.ID = ID;
        this.URL = URL;
    }
}
