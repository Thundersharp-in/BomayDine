package com.thundersharp.bombaydine.user.core.animation;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;

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
}

interface AnimatorListner{
    void slideUp(View view);
    void slideDown(View view);
    void runRecyclerSlideRightAnimation(RecyclerView recyclerView);
    void runRecyclerFallDownAnimation(RecyclerView recyclerView);
}
