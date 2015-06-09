package ch.heigvd.wordhuntclient.activities.Game;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import ch.heigvd.gen.wordhuntclient.R;
import ch.heigvd.wordhuntclient.activities.IWHView;
import ch.heigvd.wordhuntclient.asynctask.WordHuntASyncTask;
import whobjects.Grid;
import whprotocol.WHMessage;
import whprotocol.WHProtocol;

public class GameActivity extends Activity implements IWHView {

    private Grid dataGrid;
    private GameGrid gameGrid = null;
    public static boolean USING_SERVER = false;
    private List<String> userSolution = new LinkedList<String>();
    private TextView result;
    private TextView textCountDown;
    private CountDownTimer countDownTimer;
    private TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


//        getGrid();

//        if(USING_SERVER) {
//            dataGrid = new Gson().fromJson(
//                    i.getStringExtra(DevActivity.CLASS_ID),
//                    WHGridReplyMessage.class).getGrid();
//        }

        initGrid();
    }

    public void getGrid(){
        Log.d("SERVEUR", "GET GRID");
        new WordHuntASyncTask(this).execute(new WHMessage(WHProtocol.WHMessageHeader.PING, "Ping from client.")) ;
//        new WordHuntASyncTask(this).execute(new WHMessage(WHProtocol.WHMessageHeader.GRID_GET, "GIVE ME MORE")) ;
    }


    @Override
    public void reply(WHMessage message) {
        Log.d("SERVEUR", message.toString());
//            initGrid();
    }

    @Override
    public String ipLocation() {
        return "10.192.94.176";
    }


    private void initGrid(){

        result = (TextView) findViewById(R.id.textResult);
        textCountDown = (TextView) findViewById(R.id.countdown);
        score = (TextView) findViewById(R.id.score);
        LinearLayout layout = (LinearLayout) findViewById(R.id.mainContent);
        gameGrid = new GameGrid(this, result, dataGrid);
        layout.addView(gameGrid);

        score.setText("232");

        countDownTimer = new CountDownTimer(60*1000*2,1000) {
            @Override
            public void onTick(long l) {
                textCountDown.setText(String.format("%02d:%02d", (l/1000)/60, (l/1000)%60));
            }

            @Override
            public void onFinish() {
                textCountDown.setText("FINISH");
            }
        };
        countDownTimer.start();
    }

    public void checkresult(){
        String word = result.getText().toString();

        // Check word
        if(wordIsValid(word)){
            userSolution.add(word);
            Log.d("GAMEGRID", word);
            Toast.makeText(this, listword(), Toast.LENGTH_LONG).show();
            animatescore();
        }
    }

    private void animatescore() {
        Animation animation = new TranslateAnimation(10,0, 100, 0);
        animation.setDuration(1000);
        score.startAnimation(animation);
    }

    private String listword() {
       String s = "";
        for(String str: userSolution){
            str += str + "\n";
        }

        return  s;
    }

    private boolean wordIsValid(String word){

        // VALID
        if(!userSolution.contains(word) && word.length() > 3) {
            result.setText("WHY NOT ?");
            return true;
        }

        // INVALID
        Animation animation = AnimationUtils.loadAnimation(GameActivity.this, R.anim.shaking);
        result.startAnimation(animation);

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
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

    public void gameFinished(View view) {
        Log.d("GAME", "FINISHED");

//        new WordHuntASyncTask(this).execute(
//                new WHMessage(WHProtocol.WHMessageHeader.SUBMIT_POST,
//                              new WHSubmitPostMessage(0,"token", dataGrid.getGridID(), userSolution))) ;

    }

}
