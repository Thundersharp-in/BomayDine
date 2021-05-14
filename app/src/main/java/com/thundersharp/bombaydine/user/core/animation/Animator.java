package com.thundersharp.bombaydine.user.core.animation;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.thundersharp.bombaydine.R;

public final class Animator implements AnimatorListner{

    public static Animator initializeAnimator(){
        return new Animator();
    }

    @Override
    public void runRecyclerSlideRightAnimation(RecyclerView recyclerView) {
            final Context context = recyclerView.getContext();
            final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_right);

            recyclerView.setLayoutAnimation(controller);
           // recyclerView.getAdapter().notifyDataSetChanged();
            recyclerView.scheduleLayoutAnimation();

    }

    @Override
    public void runRecyclerFallDownAnimation(RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down);

        recyclerView.setLayoutAnimation(controller);
        //recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();

    }

    @Override
    public void slideUp(View view){

        TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                view.getHeight(),
                0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void recyclerTooPos(RecyclerView recyclerView, int pos) {
        if (recyclerView != null){
            recyclerView.smoothScrollToPosition(pos);
        }else {
            Log.e("E:/recyclerTooPos","Recycler view is null");
        }
    }

    @Override
    public void slideDown(View view){
        TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                0,
                view.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.INVISIBLE);



    }


    @Override
    public void runFadeInAnimation(View view) {
        Animation a = AnimationUtils.loadAnimation(view.getContext(), R.anim.fadeout);
        a.reset();
        RelativeLayout ll = view.findViewById(R.id.container);
        ll.clearAnimation();
        ll.startAnimation(a);
    }

    @Override
    public void runFadeoutAnimation(View view) {
        Animation a = AnimationUtils.loadAnimation(view.getContext(), R.anim.fadein);
        a.reset();
        RelativeLayout ll = view.findViewById(R.id.container);
        ll.clearAnimation();
        ll.startAnimation(a);
    }

    @Override
    public Animation customAnimation(int animationID, View view){
        Animation animation = AnimationUtils.loadAnimation(view.getContext(),animationID);
        view.startAnimation(animation);
        return animation;
    }
}

interface AnimatorListner{
    Animation customAnimation(int animationID, View view);
    void runFadeoutAnimation(View view);
    void runFadeInAnimation(View view);
    void slideUp(View view);
    void slideDown(View view);
    void runRecyclerSlideRightAnimation(RecyclerView recyclerView);
    void runRecyclerFallDownAnimation(RecyclerView recyclerView);
    void recyclerTooPos(RecyclerView recyclerView , int pos);
}
