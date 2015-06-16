package ch.heigvd.wordhunt.activities.Game;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextSwitcher;

/**
 * Created by paulnta on 13.06.15.
 */
public class WHTextSwitcher{

    CharSequence currentText = "";
    TextSwitcher mTextSwitcher;
    boolean visible = true;

    public WHTextSwitcher(TextSwitcher textSwitcher, Context context){
        mTextSwitcher = textSwitcher;
        textSwitcher.setInAnimation(context, android.R.anim.fade_in);
        textSwitcher.setOutAnimation(context, android.R.anim.fade_out);
    }

    public void setInvisible(){
        mTextSwitcher.setVisibility(View.INVISIBLE);
        visible = false;
    }

    public void show(){
        if(mTextSwitcher.getVisibility() != View.VISIBLE)
            mTextSwitcher.setVisibility(View.VISIBLE);
        visible = true;
    }

    public void setText(CharSequence text) {
        mTextSwitcher.setText(text);
        currentText = text;
        if(!visible){
            show();
        }
    }

    public void append(String text){
        currentText = currentText.toString() + text;
        mTextSwitcher.setText(currentText);
    }

    public CharSequence getText(){
        return currentText;
    }

    public void startAnimation(Animation animation) {
        mTextSwitcher.startAnimation(animation);
    }
}
