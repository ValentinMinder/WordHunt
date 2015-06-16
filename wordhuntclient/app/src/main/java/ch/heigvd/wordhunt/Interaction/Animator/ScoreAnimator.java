package ch.heigvd.wordhunt.Interaction.Animator;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

/**
 * Created by paulnta on 12.06.15.
 */
public class ScoreAnimator implements Runnable {

    ObjectAnimator animator;
    AnimatedTextView score;
    private int currentScore = 0;

    public ScoreAnimator(TextView textView, int delay){

        score = new AnimatedTextView(textView);

         animator = ObjectAnimator.ofObject(score, "Text", new TypeEvaluator<String>() {
            @Override
            public String evaluate(float fraction, String startValue, String endValue) {
                return (fraction < 0.5)? startValue:endValue;
            }
        },"0");
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setStartDelay(delay);
        animator.setDuration(1000L);
    }

    @Override
    public void run() {
        animator.start();
    }

    public void addPoints(int score){
        animator.setObjectValues(listValues(currentScore, currentScore + score));
        currentScore += score;
        run();
    }

   public int getCurrentScore(){
       return currentScore;
   }

    public String[] listValues(int start, int stop){
        String values[] = new String[stop-start+1];

        for(int i=start;  i <= stop; i++)
            values[i-start] = String.valueOf(i);
        return values;
    }

    private class AnimatedTextView {
        private final TextView textView;

        public AnimatedTextView(TextView textView) {this.textView = textView;}
        public String getText() {return textView.getText().toString();}
        public void setText(String text) {textView.setText(text);}
    }

    public void addAnimationListener(Animator.AnimatorListener listener){
        animator.addListener(listener);
    }
}
