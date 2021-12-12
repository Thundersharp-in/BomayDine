package com.thundersharp.bombaydine.user.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.thundersharp.bombaydine.R;

public class Home_bottom_adapter extends PagerAdapter {

    Context context;
   // int[] ids;

    public Home_bottom_adapter(Context context) {
        this.context = context;
        //this.ids = ids;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup collection, int pos){
        View view ;
        LayoutInflater inflater = LayoutInflater.from(context);
        int resId = 0;
        switch (pos){
            case 0:
                view = inflater.inflate(R.layout.cart_item,collection,false);
                //resId = R.id.bottomnoti_order;
                break;
            case 1:
                view = inflater.inflate(R.layout.last_order,collection,false);
                //resId = R.id.bottomnoti;
                break;
            default:
                view = inflater.inflate(null,collection);
        }
        collection.addView(view,0);

        return view;//collection.findViewById(resId)
    }


    @Override
    public int getCount() {
        return 2;//ids.length
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    /*
     public Object instantiateItem( ViewGroup container, int position) {
        RelativeLayout relativeLayout = new RelativeLayout(context);
        // relativeLayout.addView(ids[position]);
         container.addView(relativeLayout,0);
        return  relativeLayout;
    }
     */

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
