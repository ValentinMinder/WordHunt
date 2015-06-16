package ch.heigvd.wordhunt.activities.Admin;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Date;

import ch.heigvd.wordhunt.activities.Game.WHTextSwitcher;
import ch.heigvd.wordhunt.activities.IWHView;
import ch.heigvd.wordhunt.activities.MainActivity;
import ch.heigvd.wordhunt.asynctask.WordHuntASyncTask;
import ch.heigvd.wordhunt.design.R;
import whprotocol.WHCompetScheduling;
import whprotocol.WHMessage;
import whprotocol.WHProtocol;

public class CreateCompetition extends Activity implements IWHView{

    public static int TIME_TO_MS = 1000 * 60;
    private SharedPreferences preferences;
    public final static String prefDateEndCompetition = "ch.heigvd.wordhunt.activities.Admin.CreateCompetition.DATE_END_COMPETITION";

    private LinearLayout infosLayout;

    private Button buttonOk;
    private EditText delay_start_edit;
    private EditText duration_edit;
    private long endCompetitionDate;

    private WHTextSwitcher timer;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_competition);

        preferences = getSharedPreferences(prefDateEndCompetition,MODE_PRIVATE);

        infosLayout = (LinearLayout) findViewById(R.id.resume_competition);
        buttonOk = (Button) findViewById(R.id.button_ok);
        buttonOk.setOnClickListener(buttonOkListener);
        delay_start_edit = (EditText) findViewById(R.id.start_time);
        duration_edit = (EditText) findViewById(R.id.end_time);

        Date currentDate =  new Date(System.currentTimeMillis());
        Timestamp timestamp = new Timestamp(currentDate.getTime());
        Log.d("TIME", currentDate.toString());

    }

    @Override
    protected void onResume() {
        startCountDown();
    }

    public void startCountDown(){

        Log.d("TIME", "startCountDown ");
        super.onResume();
        // CountDown

        if(!preferences.contains(prefDateEndCompetition)){
            infosLayout.setVisibility(View.GONE);
            Log.d("TIME", "prefDateEndCompetition not contains ");
            return;
        }

        Date dateEnd = new Date(preferences.getLong(prefDateEndCompetition,0));
        Date currentDate = new Date(System.currentTimeMillis());

        Long diff = dateEnd.getTime() - currentDate.getTime();
        Log.d("TIME", "difference " + diff);

        if(diff < 0){
            infosLayout.setVisibility(View.GONE);
            Log.d("TIME", "diff < 0");
            return;
        }

        infosLayout.setVisibility(View.VISIBLE);
        TextSwitcher timerSwitcher = (TextSwitcher) findViewById(R.id.timerCompetitionSwitcher);
        timer = new WHTextSwitcher(timerSwitcher, this);

        if(countDownTimer != null)
            countDownTimer.cancel();

        countDownTimer = new CountDownTimer(diff, 1000) {
            @Override
            public void onTick(long l) {
                timer.setText(String.format("%02d:%02d", (l / 1000) / 60, (l / 1000) % 60));
            }

            @Override
            public void onFinish() {
                infosLayout.setVisibility(View.GONE);
            }
        };

        countDownTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(countDownTimer != null)
            countDownTimer.cancel();
    }

    View.OnClickListener buttonOkListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String start_time = delay_start_edit.getText().toString();
            String duration_s = duration_edit.getText().toString();

            if(!(isInteger(start_time) && isInteger(duration_s))) {
                Toast.makeText(CreateCompetition.this, "Vérifier que vous avez bien entré des nombres", Toast.LENGTH_LONG).show();
                return;
            }

            int delay = Integer.parseInt(start_time);
            int duration = Integer.parseInt(duration_s);
            endCompetitionDate = (delay * TIME_TO_MS) + (duration * TIME_TO_MS);

            scheduleCompete(delay*TIME_TO_MS, duration*TIME_TO_MS);
        }
    };

    public void scheduleCompete(int delay, int duration){
        String token = MainActivity.preferences.getString(MainActivity.prefToken, "");
        new WordHuntASyncTask(this).execute(

                // send message
                new WHMessage(

                        // Header (Shedule compete)
                        WHProtocol.WHMessageHeader.SCHEDULE_COMPET,

                        // Delay
                        new WHCompetScheduling(token, delay, duration)
                )
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_competition, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        slideToLeft();
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

    @Override
    public void reply(WHMessage message) {


        Log.d("COMPETE", message.getContent().toString());

        if(message.getHeader() == WHProtocol.WHMessageHeader.SCHEDULE_COMPET_ACK){
            Toast.makeText(CreateCompetition.this, "Compétition lancée", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = preferences.edit();
            Date end = new Date(System.currentTimeMillis() + endCompetitionDate);
            editor.putLong(prefDateEndCompetition, end.getTime());
            editor.commit();

            startCountDown();

        } else if (message.getHeader() == WHProtocol.WHMessageHeader.AUTH_REQUIRED_403){
            Toast.makeText(CreateCompetition.this, "Désolé, vous n'êtes pas administrateur", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, message.getHeader().toString() + " " +message.getContent().toString(), Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

    public void slideToLeft(){
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}
