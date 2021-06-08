package com.thundersharp.admin.core.aligantnumber;

import android.widget.LinearLayout;

public interface ElegantNumberInteractor {

    interface ViewBinder {

        void bindviewHolder(LinearLayout initialView, LinearLayout finalview, int plusbuttonId, int minusbuttonId,int textviewid,int plusinitial);

    }

    interface Presentor{

        int getcurrentnumber();

    }

    interface setOnTextChangeListner{

        int OnTextChangeListner(int val);
    }

}
