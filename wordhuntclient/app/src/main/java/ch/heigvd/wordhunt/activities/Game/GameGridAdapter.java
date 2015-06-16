package ch.heigvd.wordhunt.activities.Game;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import ch.heigvd.wordhunt.design.R;

/**
 * Created by paulnta on 13.06.15.
 */
public class GameGridAdapter extends BaseAdapter {

    private Context mContext;
    private WHTextSwitcher result;
    public static List<Pair<Button,Integer>> selectedButton = new ArrayList<Pair<Button, Integer>>();

    private char[] data;
    private String FLAG = "GameGridAdapter";

    public GameGridAdapter(Context c, WHTextSwitcher result, char[] data){
        mContext = c;
        this.result = result;
        this.data = data;
    }

    public static int getLastSelectedButtonIndex(){
        if(selectedButton.size() > 0)
            return selectedButton.get(selectedButton.size()-1).second;
        return -1;
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
        return position;
    }

    public static void resetButtonsStates(){
        for(Pair item: selectedButton){
            ((Button)item.first).setSelected(false);
        }
        selectedButton.clear();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        // if it's not recycled, we initialize it
        if(view == null){

            // Récupération du layout du bouton
            view = LayoutInflater.from(mContext).inflate(R.layout.griditem, null);
        }

        final Button button = (Button) view.findViewById(R.id.buttonGrid);
        button.setElevation(10f);
        button.setText((String)getItem(position));

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                button.dispatchTouchEvent(event);
                return true;
            }
        });

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!button.isSelected()) {
                    button.setSelected(true);
                    selectedButton.add(new Pair<Button, Integer>(button, position));

                    result.append((String) getItem(position));
                }
                return true;
            }
        });

        return view;
    }

}