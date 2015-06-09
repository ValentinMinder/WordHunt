package ch.heigvd.wordhuntclient.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.logging.Logger;

import ch.heigvd.gen.wordhuntclient.R;
import ch.heigvd.wordhuntclient.activities.Game.GameActivity;
import ch.heigvd.wordhuntclient.asynctask.WordHuntASyncTask;
import whprotocol.WHMessage;
import whprotocol.WHProtocol;
import whprotocol.WHSimpleMessage;

public class DevActivity extends Activity implements IWHView  {

    private Logger logger = Logger.getLogger(getClass().getName());
    public static SharedPreferences preferences;
    public static final String prefLocation = "ch.heigvd.wordhuntclient.PREFERENCES";
    public static final String prefLastIP = "LAST_USED_IP";
    public static final String CLASS_ID = "ch.heigvd.wordhuntclient.activities.DevActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);

        logger.info("--- Launching the App ! --- ");

        // init preferences
        preferences = getSharedPreferences(prefLocation, Context.MODE_PRIVATE);

        // check for last ip.
        if (preferences.contains(prefLastIP)) {
            TextView tv = (TextView) findViewById(R.id.IP_last_used);
            tv.setText(preferences.getString(prefLastIP, "localhost"));
            tv.setVisibility(View.VISIBLE);
        }

        EditText ipButton = (EditText) findViewById(R.id.btn_IP);
        ipButton.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(prefLastIP, arg0.toString());
                editor.commit();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dev, menu);
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



    public void onIPpreset(View v) {
        RadioButton rb = (RadioButton) v;
        EditText ipButton = (EditText) findViewById(R.id.btn_IP);
        ipButton.setText(rb.getText());
        ipButton.setVisibility(View.VISIBLE);
    }

    public void onTestPing(View v) {
        Button b = (Button) v;
        b.setClickable(false);
        b.setText("Processing...");
        logger.info("Scheduling PING test");

        new WordHuntASyncTask(this).execute(new WHMessage(WHProtocol.WHMessageHeader.PING, "Ping from client.")) ;
    }

    public void onTrainning(View v) {
        Button b = (Button) v;

        b.setText("Processing...");
        logger.info("Scheduling PING test");

        if(GameActivity.USING_SERVER)
            new WordHuntASyncTask(this).execute(new WHMessage(WHProtocol.WHMessageHeader.GRID_GET, "GIVE ME MORE")) ;
        else{
            Intent gameActivity = new Intent(DevActivity.this, GameActivity.class);
            startActivity(gameActivity);
        }
    }

    // ---- IO TASK REPLY ---- //

    @Override
    public void reply(WHMessage message) {
        logger.info("Received a reply in GUI.");
        logger.fine(message.toString());
        // TODO handle the reply correctly!!!

        /**
         * If reply is GRID_REPLY, we start a new Activity using intent
         * the grid data is passed as "Extra"
         */
        if(message.getHeader() == WHProtocol.WHMessageHeader.GRID_REPLY){

            // Intent from DevActivity -> to GameActivity
            Intent gameActivity = new Intent(DevActivity.this, GameActivity.class);

            // We send GRID REPLY
            gameActivity.putExtra(CLASS_ID, ((WHSimpleMessage)message.getContent()).getPayload());

            // We start the Activity
            startActivity(gameActivity);

            return;
        }

        Button b = (Button) findViewById(R.id.button_test_ping);
        b.setText(message.toString());
        b.setClickable(true);

    }

    @Override
    public String ipLocation() {
        return preferences.getString(DevActivity.prefLastIP, "localhost");
    }
}
