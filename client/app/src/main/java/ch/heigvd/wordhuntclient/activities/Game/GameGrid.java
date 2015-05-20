package ch.heigvd.wordhuntclient.activities.Game;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Random;

import whobjects.Grid;


public class GameGrid extends GridView {

    private GameGridAdapter gameGridAdapter;
    private TextView result;

    private Grid grid;

    public static String DEBUG_EVENT = "TOUCH_L";

    public void setDrawing(boolean isDrawing) {
        this.isDrawing = isDrawing;
    }

    private boolean isDrawing = false;

    public GameGrid(Context context, TextView result, Grid grid) {
        super(context);
        this.result = result;
        this.grid = grid;
        init();
    }

    void init(){

        setNumColumns(4);
        setHorizontalSpacing(10);

        setVerticalSpacing(10);
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
        gameGridAdapter = new GameGridAdapter(getContext(), result, data);
        setAdapter(gameGridAdapter);


        setFocusableInTouchMode(true);
        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float posY = motionEvent.getY();

                Log.d(DEBUG_EVENT, "event " + motionEvent.getAction() + " " + motionEvent.getX());
                switch (motionEvent.getAction()){
                    case 0:
                        Log.d(DEBUG_EVENT, "Start drawing");
                        isDrawing = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d(DEBUG_EVENT, "Stop drawing");
                        isDrawing = false;
                        result.setText("");
                        break;
                }

//                if(posY > 700 && posY < 500){
//                    result.setText((String)gameGridAdapter.getItem(12));
//                }

                return true;
            }
        });
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("START", String.valueOf(getLeft()));

        for(InfosButton i: GameGridAdapter.getInfosButtons()){
            i.setPosX(i.getButton().getLeft());
            i.setPosY(i.getButton().getTop());
        }
    }
}
