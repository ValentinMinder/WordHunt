package ch.heigvd.wordhunt.activities.Game;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.GridLayoutAnimationController;
import android.widget.Button;
import android.widget.GridView;

import java.util.Random;

import ch.heigvd.wordhunt.design.R;
import whobjects.Grid;


public class GameGrid extends GridView {

    private Context context;
    private GameFragment gameFragment;
    private WHTextSwitcher result;
    private String FLAG = "GAMEGRID";
    public static int NB_COL = 4;
    public static int NB_ROW = 4;

    public void setDrawing(boolean isDrawing) {
        this.isDrawing = isDrawing;
    }
    private boolean isDrawing = false;

    public GameGrid(Context context, GameFragment gameFragment, WHTextSwitcher result) {
        super(context);
        this.context = context;
        this.gameFragment = gameFragment;
        this.result = result;
        setNumColumns(4);
        setHorizontalSpacing(5);
        setVerticalSpacing(30);
        setFocusableInTouchMode(true);
    }

    public void init(Grid grid){

//        setAlpha(0);

        char[] data;

        // RANDOM IF GRID NULL (for DEBUG)
        if (grid == null) {
            data = new char[16];
            for (int i = 0; i < data.length; i++)
                data[i] = (char) ('A' + (new Random().nextInt('Z' - 'A')));

            // GRID 2D -> 1D
        } else {

            char data2D[][] = grid.getContent();
            data = new char[data2D.length * data2D.length];

            for (int row = 0, i = 0; row < data2D.length; row++)
                for (int col = 0; col < data2D.length; col++)
                    data[i++] = data2D[row][col];

        }

        GameGridAdapter gameGridAdapter = new GameGridAdapter(getContext(), result, data);
        setAdapter(gameGridAdapter);

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.smooth_show);
        GridLayoutAnimationController controller = new GridLayoutAnimationController(animation, .2f, .2f);
        controller.setInterpolator(new AnticipateOvershootInterpolator(1f));
        setLayoutAnimation(controller);

        startLayoutAnimation();
        gameFragment.onGridViewReady();
    }

    /**
     * The Grid handle:
     * - ACTION_DOWN :  Start drawing the word
     * - ACTION_UP:     Finish drawing the word
     */
    public boolean onTouchEvent(MotionEvent e){

        Log.d(FLAG, "GAMEGRID TOUCH " + e.getAction());

        switch (e.getAction()){

            case MotionEvent.ACTION_DOWN:
                Log.d(FLAG, "start drawing");
                result.setText("");
                break;

            case MotionEvent.ACTION_UP:
                Log.d(FLAG, "stop drawing");
                gameFragment.checkResult();
                GameGridAdapter.resetButtonsStates();
                break;
        }

        return true;
    }

    /**
     * Events are not directly handled by the button but by the grid
     * So we intercept all events by returning true.
     * Depending on the location of the event, the grid will dispatch
     * the event to the correct button.
     */
    public boolean onInterceptTouchEvent(MotionEvent event){
        return true;
    }

    /**
     * DispatchTouchEvent to the correct button.
     */
    public boolean dispatchTouchEvent(MotionEvent e){

        boolean consumed = super.dispatchTouchEvent(e);

        if(e.getAction() != MotionEvent.ACTION_UP) {
            for (int i = 0; i < getChildCount(); i++) {

                if (intersectView(getChildAt(i), e.getX(), e.getY())) {

                    if(checkNear(GameGridAdapter.getLastSelectedButtonIndex(), i)) {

                        Button b = (Button) getChildAt(i).findViewById(R.id.buttonGrid);
                        MotionEvent cp = MotionEvent.obtain(e);
                        consumed = consumed && b.dispatchTouchEvent(cp);
                        cp.recycle();

                        return consumed;
                    }
                }
            }
        }
        return consumed;

    }

    private boolean checkNear(int prev, int next) {

        if(prev == -1){ // first button  = ok
            return true;
        }

        GridPoint p = new GridPoint(prev);
        boolean near =  p.isNear(next);
        Log.d("CHECK", next + " is near " + prev  + " " +  near);
        return near;
    }

    /**
     * Determine if a view intersect x,y position
     */
    public boolean intersectView(View v, float x, float y){

        if(x >= v.getX() && x <= (v.getRight() - 30)
                && y >= v.getY() && y <= v.getBottom()){
            return true;
        }

        return false;
    }
}
