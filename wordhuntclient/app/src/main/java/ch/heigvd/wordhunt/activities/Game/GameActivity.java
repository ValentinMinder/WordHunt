package ch.heigvd.wordhunt.activities.Game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import ch.heigvd.wordhunt.activities.IWHView;
import ch.heigvd.wordhunt.activities.MainActivity;
import ch.heigvd.wordhunt.asynctask.WordHuntASyncTask;
import ch.heigvd.wordhunt.design.R;
import whobjects.Grid;
import whobjects.Score;
import whprotocol.WHGetGrid;
import whprotocol.WHGridReplyMessage;
import whprotocol.WHMessage;
import whprotocol.WHProtocol;
import whprotocol.WHSubmitPostMessage;

import static whprotocol.WHProtocol.WHMessageHeader.GRID_GET;
import static whprotocol.WHProtocol.WHMessageHeader.GRID_GET_AUTHENTICATED;


/**
 * Manage The Game
 */
public class GameActivity extends Activity implements IGameManager, IWHView {

    private static final String LOG = "GAMEACTIVITY";
    private boolean mShowingBack;
    private int GameLenght = 60*1000*2;
//    private int GameLenght = 10000;

    // The game view
    public static String GAME_FRAGMENT_TAG = "gameView";
    public static String SCORE_FRAGMENT_TAG = "scoreView";

    private WHProtocol.WHGameType gameType;
    public static boolean USING_SERVER = true;
    private List<String> userSolution = new LinkedList<String>();

    private Grid dataGrid;
    private HashSet<Integer> hashedSolutions = new HashSet<>();
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_flip);
//        userSolution.add("MENTIT");
//        userSolution.add("MENTIR");
//        userSolution.add("MIT");
//        userSolution.add("MIR");
//        userSolution.add("MIT");
//        userSolution.add("MITE");
//        userSolution.add("MITEE");
//        userSolution.add("MITEE");
//        userSolution.add("EMPLI");
//        userSolution.add("EMPLIE");
//        userSolution.add("EMPLIE");
//        userSolution.add("EMPILE");
//        userSolution.add("EMPILEE");
//        userSolution.add("EME");
//        userSolution.add("EME");
//        userSolution.add("EMIE");
//        userSolution.add("EMIEE");
//        userSolution.add("EMIT");
//        userSolution.add("EMIR");
//        userSolution.add("EMIT");
//        userSolution.add("EMIT");

        Intent i = getIntent();
        int gameTypeid = i.getIntExtra(MainActivity.GAME_TYPE_ID, 0);
        Log.d("SERVEUR", "result intent " + gameTypeid);
        gameType =  WHProtocol.WHGameType.values()[gameTypeid];

        // add view to the layout
        if(savedInstanceState == null) {
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, new GameFragment(), GAME_FRAGMENT_TAG)
                    .commit();
        }
    }


    @Override
    public void onBackPressed() {
        finishAfterTransition();
        //slide from left to right
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        if(getGameFragment() != null) {
            Log.d("GAME", "We call super.onBackPressed()");
//            super.onBackPressed();
        }
    }

    @Override
    public void onFragmentReady() {
        Log.d("SERVEUR", "onFragmentReady");
        getGrid();
    }

    public void getGrid(){
        Log.d("SERVEUR", "GET GRID gametype: " + gameType.toString());
        String token = MainActivity.preferences.getString(MainActivity.prefToken, "none");
        if(token == "none"){
            debug("No token");
            return;
        }

        switch (gameType){
            case OFFLINE:
                setTitle("MODE HORS LIGNE");
                new WordHuntASyncTask(this).execute(
                        new WHMessage(GRID_GET, "GET GRID")) ;
                break;
            case TRAINING:
                setTitle("MODE ENTRAINEMENT");
                new WordHuntASyncTask(this).execute(
                        new WHMessage(GRID_GET_AUTHENTICATED,
                        new WHGetGrid(token))) ;
                break;
            case COMPETITION:

                setTitle("MODE COMPETITION");
                new WordHuntASyncTask(this).execute(
                        new WHMessage(GRID_GET_AUTHENTICATED,
                                new WHGetGrid(token, WHProtocol.WHGameType.COMPETITION))) ;

                break;
            case CHALLENGE:

                setTitle("MODE CHALLENGE");

                MainActivity.TYPE_MODE_CHALENGE challenge_type = getTypeChallenge();

                if(challenge_type == MainActivity.TYPE_MODE_CHALENGE.CHALLENGE_VS_USER) {

                    String username = getIntent().getStringExtra(MainActivity.CHALLENGE_CHOICE);
                    Log.d("SERVEUR", "mode CHALLENGE against user:  " + username);
                    new WordHuntASyncTask(this).execute(
                            new WHMessage(GRID_GET_AUTHENTICATED,
                                    new WHGetGrid(token, username )));

                }

                else if(challenge_type == MainActivity.TYPE_MODE_CHALENGE.CHALLENGE_VS_GRID) {

                    String gridId = getIntent().getStringExtra(MainActivity.CHALLENGE_CHOICE);
                    Log.d("SERVEUR", "mode CHALLENGE on specific grid id: " + gridId);
                    new WordHuntASyncTask(this).execute(
                            new WHMessage(GRID_GET_AUTHENTICATED,
                                    new WHGetGrid(token, Integer.parseInt(gridId) )));
                }

                else if(challenge_type == MainActivity.TYPE_MODE_CHALENGE.RANDOM_GRID){
                    Log.d("SERVEUR", "mode CHALLENGE on Random Grid");
                    new WordHuntASyncTask(this).execute(
                            new WHMessage(GRID_GET_AUTHENTICATED,
                                    new WHGetGrid(MainActivity.preferences.getString(MainActivity.prefToken, ""), WHProtocol.WHGameType.CHALLENGE))
                    );
                }
                break;
        }
    }

    MainActivity.TYPE_MODE_CHALENGE getTypeChallenge(){
        int value = getIntent().getIntExtra(MainActivity.CHALLENGE_TYPE, 0);
        return MainActivity.TYPE_MODE_CHALENGE.values()[value];
    }

    public void sendResult(){
        debug("sendResult for " + dataGrid.getGridID());
        if(gameType == WHProtocol.WHGameType.OFFLINE){
            // We don't send result for offline game
            return;
        }

        new WordHuntASyncTask(this).execute(
                new WHMessage((WHProtocol.WHMessageHeader.SUBMIT_POST),
                        new WHSubmitPostMessage(
                                0,
                                MainActivity.preferences.getString(MainActivity.prefToken, ""),
                                dataGrid.getGridID(), userSolution)));
    }

    @Override
    public void reply(WHMessage message) {
        Log.d("SERVEUR", "REPLY " + message.getHeader().toString() + " : "+ message.toString());

        switch (message.getHeader()){

            case GRID_REPLY:
                WHGridReplyMessage gridReplyMessage = (WHGridReplyMessage) message.getContent();
                dataGrid = gridReplyMessage.getGrid();
                hashedSolutions =  dataGrid.getHashSetSolutionsAsHashSet();
                Log.d("SERVEUR", "Receive new Grid id: " + dataGrid.getGridID() + " best score: " + dataGrid.getBestScore());
                getGameFragment().fillGrid(dataGrid);
                break;

            case SUBMIT_VALIDATE:
                Log.d("SERVEUR SUBMIT_VALIDATE", message.getContent().toString());
                break;

            default:
                Toast.makeText(this, message.getContent().toString(), Toast.LENGTH_LONG).show();


        }
    }

    /**
     * Switch views
     * At the end of the game, show scores
     */
    public void flipViews(){

        if(getFragmentManager().findFragmentByTag(GAME_FRAGMENT_TAG) == null) {
            Log.d("GAME", "gamfragment is null");
            return;
        }

        if(mShowingBack){
            debug("filpView : mShowingBack = true");
            getFragmentManager().popBackStack();
            mShowingBack = false;
            return;
        }


        debug("filpView : mShowingBack = false");
        mShowingBack = true;

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.container, ScoreFragment.newInstance(
                        score, dataGrid.getBestScore() ,userSolution.toArray(new String[userSolution.size()])), SCORE_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.manualFinish:
                if(getGameFragment() != null)
                    getGameFragment().requestFinish();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private GameFragment getGameFragment(){
        return (GameFragment) getFragmentManager().findFragmentByTag(GAME_FRAGMENT_TAG);
    }

    @Override
    public void restartGame() {
        userSolution.clear();
        debug("restartGame");
        flipViews();
        onFragmentReady();
    }

    @Override
    public void onGameStoped() {

    }

    @Override
    public void onGameFinish(int score) {
        this.score = score;
        sendResult();
        flipViews();
    }

    @Override
    public int onValidateWord(String word) {

        // Already exist
        if (userSolution.contains(word)) {
            Toast.makeText(this, "Mot déjà touvé", Toast.LENGTH_SHORT).show();
            return 0;
        }

        // Valid
        if(hashedSolutions.contains(word.hashCode()) ) {
            Toast.makeText(this, "Mot valide", Toast.LENGTH_SHORT).show();
            userSolution.add(word);
            return Score.getInstance().getScore(word, WHProtocol.WHPointsType.LENGTH_SQUARE);
        }

        // Invalide
        return 0;
    }

    public int getGameDuration() {
        return GameLenght;
    }
    private void debug(String text){
        Log.d(LOG, text);
    }


}
