package com.thundersharp.tableactions.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.slider.RangeSlider;
import com.thundersharp.tableactions.R;
import com.thundersharp.tableactions.listeners.GuestChangeListener;

/**
 * @author hrishikeshprateek
 * @see android.graphics.drawable.Drawable.Callback
 * @see android.view.View
 * @see android.view.ViewGroup
 * @see android.widget.RelativeLayout
 */
public class TableGuestCounter extends RelativeLayout {

    /**
     * Global instance Variables
     */
    private Integer noOfChairs = 2;
    private Boolean animateChairs = true;
    private int backGroundColor;
    private View view;
    private Integer counter = 1;

    /**
     * Ui variables
     */
    private LinearLayout background;
    private ImageView chair_one;
    private ImageView chair_two;
    private ImageView chair_three;
    private ImageView chair_four;
    private ImageView chair_five;
    private ImageView chair_six;
    private ImageView chair_seven;
    private ImageView chair_eight;

    private TextView table;
    private RangeSlider slider;


    private GuestChangeListener guestChangeListener;


    /**
     * Constructor for TableGuestCounter
     * @param context
     */
    public TableGuestCounter(Context context) {
        super(context);
        initViews(context,null);
    }

    /**
     * Constructor for TableGuestCounter
     * @param context
     * @param attrs
     */
    public TableGuestCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context,attrs);
    }

    /**
     * Constructor for TableGuestCounter
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TableGuestCounter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context,attrs);
    }

    /**
     * Constructor for TableGuestCounter
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    public TableGuestCounter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initViews(context,attrs);
    }

    public void initialize(){
        table.setText("1");
        animateChairs(true,chair_one);
        animateChairs(false,chair_two,chair_three,chair_four,chair_five,chair_six,chair_seven,chair_eight);
    }

    public void setNoOfGuestChangeListener(GuestChangeListener guestChangeListener){
        this.guestChangeListener = guestChangeListener;
    }

    public void initViews(@NonNull Context context, @Nullable AttributeSet attributeSet){
        view = inflate(context, R.layout.guest_counter_view,this);

        background = view.findViewById(R.id.container);
        chair_one = view.findViewById(R.id.chair_one);
        chair_two = view.findViewById(R.id.chair_two);
        chair_three = view.findViewById(R.id.chair_three);
        chair_four = view.findViewById(R.id.chair_four);
        chair_five = view.findViewById(R.id.chair_five);
        chair_six = view.findViewById(R.id.chair_six);
        chair_seven = view.findViewById(R.id.chair_seven);
        chair_eight = view.findViewById(R.id.chair_eight);
        table = view.findViewById(R.id.table);
        slider = view.findViewById(R.id.slider);


        @SuppressLint("Recycle") TypedArray typedArray = context.obtainStyledAttributes(attributeSet,R.styleable.TableGuestCounter);

        noOfChairs = typedArray.getInteger(R.styleable.TableGuestCounter_maxNoOfChairs,4);
        animateChairs = typedArray.getBoolean(R.styleable.TableGuestCounter_animateChairs,true);
        backGroundColor = typedArray.getColor(R.styleable.TableGuestCounter_backgroundColor,getResources().getColor(R.color.mainBg));

        background.setBackgroundColor(backGroundColor);
        initialize();


        chair_one.setOnClickListener(e-> Toast.makeText(context,"One",Toast.LENGTH_SHORT).show());
        slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                table.setText(""+(int) value);
                if (counter>value){
                    counter = (int) value;
                    //backward
                    switch ((int) value){
                        case 1:
                            animateChairs(false,chair_two);
                            break;
                        case 2:
                            animateChairs(false,chair_three);
                            break;
                        case 3:
                            animateChairs(false,chair_four);
                            break;
                        case 4:
                            animateChairs(false,chair_five);
                            break;
                        case 5:
                            animateChairs(false,chair_six);
                            break;
                        case 6:
                            animateChairs(false,chair_seven);
                            break;
                        case 7:
                            animateChairs(false,chair_eight);
                            break;
                        default:
                            break;
                    }
                }else {
                    //forward
                    counter = (int) value;
                    switch ((int) value){
                    case 1:
                        animateChairs(true,chair_one);
                        break;
                    case 2:
                        animateChairs(true,chair_two);
                        break;
                    case 3:
                        animateChairs(true,chair_three);
                        break;
                    case 4:
                        animateChairs(true,chair_four);
                        break;
                    case 5:
                        animateChairs(true,chair_five);
                        break;
                    case 6:
                        animateChairs(true,chair_six);
                        break;
                    case 7:
                        animateChairs(true,chair_seven);
                        break;
                    case 8:
                        animateChairs(true,chair_eight);
                        break;
                        default:
                            break;
                    }
                }
                /*
                switch ((int) value){

                    case 1:
                        animateChairs(true,chair_one);
                        animateChairs(false,chair_two,chair_three,chair_four,chair_five,chair_six,chair_seven,chair_eight);
                        break;
                    case 2:
                        animateChairs(true,chair_one,chair_two);
                        animateChairs(false,chair_three,chair_four,chair_five,chair_six,chair_seven,chair_eight);
                        break;
                    case 3:
                        animateChairs(true,chair_one,chair_two,chair_three);
                        animateChairs(false,chair_four,chair_five,chair_six,chair_seven,chair_eight);
                        break;
                    case 4:
                        animateChairs(true,chair_one,chair_two,chair_three,chair_four);
                        animateChairs(false,chair_five,chair_six,chair_seven,chair_eight);
                        break;
                    case 5:
                        animateChairs(true,chair_one,chair_two,chair_three,chair_four,chair_five);
                        animateChairs(false,chair_six,chair_seven,chair_eight);
                        break;
                    case 6:
                        animateChairs(true,chair_one,chair_two,chair_three,chair_four,chair_five,chair_six);
                        animateChairs(false,chair_seven,chair_eight);
                        break;
                    case 7:
                        animateChairs(true,chair_one,chair_two,chair_three,chair_four,chair_five,chair_six,chair_seven);
                        animateChairs(false,chair_eight);
                        break;
                    case 8:

                        animateChairs(true,chair_one,chair_two,chair_three,chair_four,chair_five,chair_six,chair_seven,chair_eight);
                        break;

                default:
                break;
            }
                 */
            }
        });
    }

    private void animateChairs(boolean visibility, ImageView... chairs){

        if (visibility){
            for (ImageView chair : chairs){
                chair.setVisibility(VISIBLE);
            }
        }else {
            for (ImageView c : chairs)
                c.setVisibility(GONE);
        }

    }
}
