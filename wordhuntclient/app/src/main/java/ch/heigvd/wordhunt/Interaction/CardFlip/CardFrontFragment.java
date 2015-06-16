package ch.heigvd.wordhunt.Interaction.CardFlip;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ch.heigvd.wordhunt.Interaction.Animator.AnimatedToast;
import ch.heigvd.wordhunt.Interaction.Animator.ScoreAnimator;
import ch.heigvd.wordhunt.design.R;

public class CardFrontFragment extends Fragment {

    ScoreAnimator scoreAnimator;
    protected AnimatedToast animatedToast;
    protected RelativeLayout rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = (RelativeLayout) inflater.inflate(R.layout.card1, container, false);
        TextView score = (android.widget.TextView) rootView.findViewById(R.id.score);
        scoreAnimator = new ScoreAnimator(score, 300);
        scoreAnimator.addPoints(3245);
        scoreAnimator.run();
        animatedToast = new AnimatedToast(rootView, getActivity());

        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    Log.d("ANIM", "x: " + event.getX() + " y: " + event.getY());
                    animatedToast.makeToast("+34", (int)event.getX(), 0, (int)event.getY(), 0);
                }
                return false;
            }
        });

        return rootView;
    }

    public void animateScore(int score){
        scoreAnimator.addPoints(score);
        scoreAnimator.run();
    }
}
