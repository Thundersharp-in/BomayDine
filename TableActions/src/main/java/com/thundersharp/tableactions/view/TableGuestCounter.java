package com.thundersharp.tableactions.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.slider.RangeSlider;
import com.thundersharp.tableactions.R;
import com.thundersharp.tableactions.listeners.ChairInteraction;
import com.thundersharp.tableactions.listeners.GuestChangeListener;
import com.thundersharp.tableactions.listeners.OnTableChangeListener;

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
    private int previousNoOfGuests = 1;
    private boolean tableRotationEnabled = false;
    private Integer rotationSpeed = 200;
    private boolean infoTextEnabled = false;
    private String infoText;
    private int infoTextColor;

    private boolean secondInfoTextEnabled = false;
    private String secondInfoText;
    private int secondInfoTextColor;

    /**
     * Ui variables
     */
    private LinearLayout background;
    private TextView seekText,seekTextTable;
    private ImageView chair_one;
    private ImageView chair_two;
    private ImageView chair_three;
    private ImageView chair_four;
    private ImageView chair_five;
    private ImageView chair_six;
    private ImageView chair_seven;
    private ImageView chair_eight;

    private TextView table;
    private RangeSlider slider,sliderTable;


    private GuestChangeListener guestChangeListener;
    private OnTableChangeListener tableChangeListener;
    private ChairInteraction chairInteraction;


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

    public void viewRotator(int idOfView,int r){

    }

    private void initialize(){
        table.setText("1");
        animateChairs(true,chair_one);
        animateChairs(false,chair_two,chair_three,chair_four,chair_five,chair_six,chair_seven,chair_eight);

        instantiateListener();
        if (tableRotationEnabled) viewRotator(R.id.tableContainer,rotationSpeed);
    }

    public void setNoOfGuestChangeListener(GuestChangeListener guestChangeListener){
        this.guestChangeListener = guestChangeListener;
        instantiateListener();
    }

    public void setOnTableChangeListener(OnTableChangeListener tableChangeListener){
        this.tableChangeListener = tableChangeListener;
        initlizeTableListner();
    }

    private void initlizeTableListner() {
        if (tableChangeListener != null) tableChangeListener.onTableValuesChanged(1);
    }

    private void instantiateListener() {
        if (guestChangeListener != null) guestChangeListener.onGuestAdded(previousNoOfGuests,previousNoOfGuests);
    }

    public void setChairInteractionListener(ChairInteraction chairInteractionListener){
        this.chairInteraction = chairInteractionListener;
    }

    private void initViews(@NonNull Context context, @Nullable AttributeSet attributeSet){
        view = inflate(context, R.layout.guest_counter_view,this);

        background = view.findViewById(R.id.container);
        seekText = view.findViewById(R.id.seekText);
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
        seekTextTable = view.findViewById(R.id.seekTextTable);
        sliderTable = view.findViewById(R.id.sliderTable);


        @SuppressLint("Recycle") TypedArray typedArray = context.obtainStyledAttributes(attributeSet,R.styleable.TableGuestCounter);

        noOfChairs = typedArray.getInteger(R.styleable.TableGuestCounter_maxNoOfChairs,4);
        animateChairs = typedArray.getBoolean(R.styleable.TableGuestCounter_animateChairs,true);
        backGroundColor = typedArray.getColor(R.styleable.TableGuestCounter_backgroundColor,getResources().getColor(R.color.mainBg));
        tableRotationEnabled = typedArray.getBoolean(R.styleable.TableGuestCounter_rotationEnabled,false);
        rotationSpeed = typedArray.getInteger(R.styleable.TableGuestCounter_rotationSpeed,200);


        infoTextEnabled = typedArray.getBoolean(R.styleable.TableGuestCounter_infoTextEnabled,false);
        infoText = typedArray.getString(R.styleable.TableGuestCounter_infoText);
        infoTextColor = typedArray.getColor(R.styleable.TableGuestCounter_infoTextColor, Color.WHITE);


        secondInfoTextEnabled = typedArray.getBoolean(R.styleable.TableGuestCounter_secondInfoTextEnabled,false);
        secondInfoText = typedArray.getString(R.styleable.TableGuestCounter_secondInfoText);
        infoTextColor = typedArray.getColor(R.styleable.TableGuestCounter_secondInfoTextEnabled, Color.WHITE);

        if (!infoTextEnabled) seekText.setVisibility(GONE);

        else {
            seekText.setVisibility(VISIBLE);
            seekText.setText(infoText);
            seekText.setTextColor(infoTextColor);
        }

        if (!secondInfoTextEnabled) seekTextTable.setVisibility(GONE);

        else {
            seekTextTable.setVisibility(VISIBLE);
            seekTextTable.setText(secondInfoText);
            seekTextTable.setTextColor(infoTextColor);
        }

        background.setBackgroundColor(backGroundColor);
        initialize();


        chair_one.setOnClickListener(e-> {
            if (chairInteraction!=null) chairInteraction.onChairClicked(1);
        });

        chair_two.setOnClickListener(e-> {
            if (chairInteraction!=null) chairInteraction.onChairClicked(2);
        });

        chair_three.setOnClickListener(e-> {
            if (chairInteraction!=null) chairInteraction.onChairClicked(3);
        });

        chair_four.setOnClickListener(e-> {
            if (chairInteraction!=null) chairInteraction.onChairClicked(4);
        });

        chair_five.setOnClickListener(e-> {
            if (chairInteraction!=null) chairInteraction.onChairClicked(5);
        });

        chair_six.setOnClickListener(e-> {
            if (chairInteraction!=null) chairInteraction.onChairClicked(6);
        });

        chair_seven.setOnClickListener(e-> {
            if (chairInteraction!=null) chairInteraction.onChairClicked(7);
        });

        chair_eight.setOnClickListener(e-> {
            if (chairInteraction!=null) chairInteraction.onChairClicked(8);
        });



        chair_one.setOnLongClickListener(e-> {
            if (chairInteraction!=null) chairInteraction.onChairsLongPressed(1);
            return true;
        });

        chair_two.setOnLongClickListener(e-> {
            if (chairInteraction!=null) chairInteraction.onChairsLongPressed(2);
            return true;
        });

        chair_three.setOnLongClickListener(e-> {
            if (chairInteraction!=null) chairInteraction.onChairsLongPressed(3);
            return true;
        });

        chair_four.setOnLongClickListener(e-> {
            if (chairInteraction!=null) chairInteraction.onChairsLongPressed(4);
            return true;
        });

        chair_five.setOnLongClickListener(e-> {
            if (chairInteraction!=null) chairInteraction.onChairsLongPressed(5);
            return true;
        });

        chair_six.setOnLongClickListener(e-> {
            if (chairInteraction!=null) chairInteraction.onChairsLongPressed(6);
            return true;
        });

        chair_seven.setOnLongClickListener(e-> {
            if (chairInteraction!=null) chairInteraction.onChairsLongPressed(7);
            return true;
        });

        chair_eight.setOnLongClickListener(e-> {
            if (chairInteraction!=null) chairInteraction.onChairsLongPressed(8);
            return true;
        });

        sliderTable.addOnChangeListener((slider, value, fromUser) -> {
            if (tableChangeListener != null) tableChangeListener.onTableValuesChanged((int) value);
        });

        slider.addOnChangeListener((slider, value, fromUser) -> {
            table.setText(""+(int) value);

            if (guestChangeListener != null) {
                if ((previousNoOfGuests - (int) value) > 0)
                    guestChangeListener.onGuestRemoved(previousNoOfGuests - (int) value,(int) value);
                else guestChangeListener.onGuestAdded(Math.abs((previousNoOfGuests - (int) value)),(int) value);
            }

            previousNoOfGuests = (int) value;

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

        });
    }

    private void animateChairs(boolean visibility, ImageView... chairs){

        if (visibility){
            for (ImageView chair : chairs){
                chair.setVisibility(VISIBLE);
            }
        }else {
            for (ImageView c : chairs) {
                c.setVisibility(GONE);
            }
        }

    }
}
