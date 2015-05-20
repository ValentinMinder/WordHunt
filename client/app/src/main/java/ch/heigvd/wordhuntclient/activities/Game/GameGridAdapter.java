package ch.heigvd.wordhuntclient.activities.Game;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.heigvd.gen.wordhuntclient.R;

/**
 * Created by paulnta on 10.05.15.
 */
public class GameGridAdapter extends BaseAdapter {

    private Context mContext;
    private TextView result;
    public static List<Button> selectedButton = new ArrayList<Button>() {};
    public static InfosButton[] infosButtons = new InfosButton[16];

    private char[] data;

    public static InfosButton[] getInfosButtons() {
        return infosButtons;
    }

    public void addButton(Button b, int position){

            if (infosButtons[position] != null){
                infosButtons[position].setButton(b,position);
                Log.d("BUTTON", "replace bouton at " + position );
            } else {
                infosButtons[position] = new InfosButton(b,position);
                Log.d("BUTTON", "new button at " + position);
            }
    }
    public GameGridAdapter(Context c, TextView result, char[] data){
        mContext = c;
        this.result = result;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return String.valueOf(data[position]);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    public static void resetButtonsStates(){
        for(Button b: selectedButton){
            b.setSelected(false);
        }
        selectedButton.clear();


    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;


        // if it's not recycled, we initialize it
        if(view == null){

            // Récuupération du layout du bouton
            view = LayoutInflater.from(mContext).inflate(R.layout.griditem, null);
        }


        final Button button = (Button) view.findViewById(R.id.buttonGrid);
        Log.d("BUTTON", "+ " + position);

        TextView info = (TextView) view.findViewById(R.id.infos);
        Log.d("value", "item: " + position + " data:" + data[position]);

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(70, View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(70, View.MeasureSpec.EXACTLY);



        button.measure(widthMeasureSpec, heightMeasureSpec);

//        int posX = view.getLeft();
        button.setText((String)getItem(position));


        button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    result.append((String) getItem(position));
                    button.setSelected(true);
                    selectedButton.add(button);
                    Log.d("BUTTON", position+  " sel");
                }

                return false;
            }
        });



        addButton(button, position);

//        button.setOnHoverListener( new View.OnHoverListener() {
//            @Override
//            public boolean onHover(View view, MotionEvent motionEvent) {
//
//                Log.d("HOVER", "HOVER");
//                if (motionEvent.getAction() == MotionEvent.ACTION_HOVER_ENTER){
//                    Log.d("HOVER", "ENTER");
//                }
//
//                return true;
//
//            }
//        });


        return view;
    }


    public int getRelativeLeft(View v){
        if(v.getParent() == v.getRootView()){
            return v.getLeft();
        } else {
            return v.getLeft() + getRelativeLeft((View) v.getParent());
        }
    }
}
