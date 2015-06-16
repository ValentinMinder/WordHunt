package ch.heigvd.wordhunt.Interaction.Animator;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by paulnta on 12.06.15.
 */
public class AnimatedToast {

   Context mContext;
   RelativeLayout mView;
   TextView toast;

   private int ANIM_LENGTH = 800;
   private float MIN_SCALE = 1f;
   private float MAX_SCALE = 1.5f;

   public AnimatedToast(RelativeLayout view, Context context){
       mContext = context;
       mView = view;
       toast = new TextView(context);
       mView.addView(toast, 0);
       toast.setElevation(24);
       toast.setVisibility(View.INVISIBLE);


   }

    public void makeToast(String value, int fromX, int toX, int fromY, int toY ){

        toast.setText(value);

        AnimationSet animationSet = new AnimationSet(true);
        Animation translate = new TranslateAnimation(Animation.ABSOLUTE, fromX, Animation.ABSOLUTE, toX,
                                                     Animation.ABSOLUTE, fromY, Animation.ABSOLUTE, toY);

        AlphaAnimation alphaAnimationEnter = new AlphaAnimation(0f,1f);
        AlphaAnimation alphaAnimationOut = new AlphaAnimation(1f, 0f);
        alphaAnimationEnter.setDuration(0);
        alphaAnimationOut.setDuration(ANIM_LENGTH/4);
        alphaAnimationOut.setStartOffset(ANIM_LENGTH - (ANIM_LENGTH/4));

        ScaleAnimation scaleAnimationEnter = new ScaleAnimation(MIN_SCALE,MAX_SCALE,MIN_SCALE,MAX_SCALE,
                Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f);

        scaleAnimationEnter.setDuration(ANIM_LENGTH/4);
        scaleAnimationEnter.setFillAfter(true);
//
        ScaleAnimation scaleAnimationOut = new ScaleAnimation(MAX_SCALE,MIN_SCALE,MAX_SCALE,MIN_SCALE,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimationOut.setDuration(ANIM_LENGTH/4);
        scaleAnimationOut.setStartOffset(ANIM_LENGTH/2+ANIM_LENGTH/4);

//        scaleAnimationOut.setInterpolator(new AnticipateOvershootInterpolator(0.5f));
//        scaleAnimationEnter.setInterpolator(new AnticipateOvershootInterpolator(0.5f));
//        translate.setInterpolator(new DecelerateInterpolator());


        animationSet.addAnimation(translate);
        animationSet.addAnimation(scaleAnimationEnter);
        animationSet.addAnimation(scaleAnimationOut);
        animationSet.addAnimation(alphaAnimationEnter);
        animationSet.addAnimation(alphaAnimationOut);

        animationSet.setAnimationListener(animationListener);
//        animationSet.setInterpolator();
        animationSet.setDuration(ANIM_LENGTH);
        toast.setVisibility(View.VISIBLE);
        toast.startAnimation(animationSet);
    }

    Animation.AnimationListener animationListener = new Animation.AnimationListener() {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            toast.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

    };

}
