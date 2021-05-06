package com.thundersharp.bombaydine.user.core.aligantnumber;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ElegentNumberHelper implements ElegantNumberInteractor.ViewBinder,ElegantNumberInteractor.Presentor{

    private Context context;
    private ElegantNumberInteractor.setOnTextChangeListner setOnTextChangeListner;
    private View view;
    int counter = 0;
    private TextView textView;
    private LinearLayout initialView;
    private LinearLayout finalview;

    /**
     * This method needs to be called initially before calling<>@bindviewHolder(LinearLayout initialView, LinearLayout finalview, int plusbuttonId, int minusbuttonId, int textviewid)</>
     * @param context
     * @param view
     */

    public ElegentNumberHelper(Context context, ElegantNumberInteractor.setOnTextChangeListner setOnTextChangeListner, View view) {
        this.context = context;
        this.setOnTextChangeListner = setOnTextChangeListner;
        this.view = view;
    }

    /**
     *
     * @param initialView this is the view that will be replaced when user clicks when counter is 0 {Needs to be initialized before passing }
     * @param finalview this is the main counter view { Needs to be initialized before passing }
     * @param plusbuttonId
     * @param minusbuttonId
     * @param textviewid
     */

    @Override
    public void bindviewHolder(LinearLayout initialView, LinearLayout finalview, int plusbuttonId, int minusbuttonId, int textviewid, int plusinitial) {
        this.initialView = initialView;
        this.finalview = finalview;
        ImageButton plus = view.findViewById(plusbuttonId);
        ImageButton minus = view.findViewById(minusbuttonId);
        ImageButton plusinit = view.findViewById(plusinitial);
        textView = view.findViewById(textviewid);

        if (counter == 0){
            initialView.setVisibility(View.VISIBLE);
            finalview.setVisibility(View.GONE);

        }else {
            initialView.setVisibility(View.GONE);
            finalview.setVisibility(View.VISIBLE);
        }

        plusinit.setOnClickListener(view1 -> {
            counter++;
            update(counter);
            textView.setText(""+counter);
            initialView.setVisibility(View.GONE);
            finalview.setVisibility(View.VISIBLE);
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;

                textView.setText(""+counter);
                update(counter);
            }
        });

        minus.setOnClickListener(view1 -> {
            counter--;
            if (counter == 0){
                initialView.setVisibility(View.VISIBLE);
                finalview.setVisibility(View.GONE);
            }
            update(counter);
            textView.setText(""+counter);

        });


    }


    @Override
    public int getcurrentnumber() {
        return counter;
    }

    public void updateNo(int number){
        if (number == 0){
            counter =0;
            initialView.setVisibility(View.VISIBLE);
            finalview.setVisibility(View.GONE);
        }else {
            initialView.setVisibility(View.GONE);
            finalview.setVisibility(View.VISIBLE);
            counter = number;
            textView.setText(""+counter);
        }
    }

    public void update(int counter){

        setOnTextChangeListner.OnTextChangeListner(counter);
    }

}
