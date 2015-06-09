package ch.heigvd.wordhuntclient.activities.Test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import ch.heigvd.gen.wordhuntclient.R;

import static android.view.View.OnTouchListener;

public class TestActivity extends Activity implements OnTouchListener {

    private static final String TAG_LOG = "TouchActivity";
    private static final boolean LOG = true;

    Button b1;
    Button b2;
    Button b3;
    Button b4;
    GridLayout grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        b3 = (Button) findViewById(R.id.b3);
        b4 = (Button) findViewById(R.id.b4);
        grid = (GridLayout) findViewById(R.id.grid);

        b1.setOnTouchListener(this);
        b2.setOnTouchListener(this);
        b3.setOnTouchListener(this);
        b4.setOnTouchListener(this);
//        grid.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent ev) {
//        log("Activity " + stringValue(ev));
        log(((Button)view).getText() + stringValue(ev));
        return super.onTouchEvent(ev);
    }

    OnTouchListener buttonListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View view, MotionEvent ev) {
            log(((Button)view).getText() + stringValue(ev));
            return false;
        }
    };




    private String stringValue(MotionEvent event) {

        final int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                return "ACTION_DOWN";
            case MotionEvent.ACTION_MOVE:
                return "ACTION_MOVE";
            case MotionEvent.ACTION_UP:
                return "ACTION_UP";
            case MotionEvent.ACTION_CANCEL:
                return "ACTION_CANCEL";
        }

        return "";
    }

    private static void log(String message){
        if (LOG) {
            Log.d(TAG_LOG, message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
