package ch.heigvd.wordhunt.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import ch.heigvd.wordhunt.activities.Admin.CreateCompetition;
import ch.heigvd.wordhunt.activities.Game.GameActivity;
import ch.heigvd.wordhunt.activities.Login.CreateAccountActivity;
import ch.heigvd.wordhunt.activities.Login.LoginActivity;
import ch.heigvd.wordhunt.asynctask.WordHuntASyncTask;
import ch.heigvd.wordhunt.design.R;
import whprotocol.WHMessage;
import whprotocol.WHProtocol;


public class MainActivity extends Activity implements IWHView{

    //identifiant des requêtes
    public static int SIGNUP = 0;
    public static int SIGNIN = 1;

    public static SharedPreferences preferences;
    public static SharedPreferences serveurPreferences;


    public static String prefToken = "ch.heigvd.wordhunt.activities.MainActivity.TOKEN";
    public static String prefUsername = "ch.heigvd.wordhunt.activities.MainActivity.USER_NAME";
    public static String prefServeurIP = "ch.heigvd.wordhunt.activities.MainActivity.SERVEUR_IP";
    public static String prefServeurPort = "ch.heigvd.wordhunt.activities.MainActivity.SERVEUR_PORT";


    public final static String GAME_TYPE_ID = "ch.heigvd.wordhuntclient.activities.MainActivity.GAME_TYPE_ID";
    public final static String CHALLENGE_CHOICE = "ch.heigvd.wordhuntclient.activities.MainActivity.CHALLENGE_CHOICE";
    public final static String CHALLENGE_TYPE = "ch.heigvd.wordhuntclient.activities.MainActivity.CHALLENGE_TYPE";

    private Button pingTestButton;

    public static enum TYPE_MODE_CHALENGE{
        CHALLENGE_VS_USER,
        RANDOM_GRID,
        CHALLENGE_VS_GRID
    }


    private Button buttonTraining;
    private Button buttonChallenge;
    private Button buttonTournament;

    public String challengeChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences(prefToken, MODE_PRIVATE);
        serveurPreferences = getSharedPreferences(prefServeurIP, MODE_PRIVATE);

        setContentView(R.layout.activity_main);
        Button buttonSignOff = (Button) findViewById(R.id.buttonSignOff);

        buttonTraining = (Button) findViewById(R.id.buttonTraining);
        buttonChallenge = (Button) findViewById(R.id.buttonModeChallenge);
        buttonTournament = (Button) findViewById(R.id.buttonModeTournament);


        buttonSignOff.setVisibility(View.GONE);
    }

    private void checkGameButtonState(){

       boolean enable = preferences.contains(prefToken);
       buttonTraining.setEnabled(enable);
       buttonChallenge.setEnabled(enable);
       buttonTournament.setEnabled(enable);
    }


    @Override
    protected void onResume(){
        super.onResume();
        checkGameButtonState();
        if(preferences.contains(prefToken)){
            Button b = (Button) findViewById(R.id.buttonSignIn);
            b.setVisibility(View.GONE);
            b = (Button) findViewById(R.id.buttonSignUp);
            b.setVisibility(View.GONE);
            b = (Button) findViewById(R.id.buttonSignOff);
            b.setVisibility(View.VISIBLE);
        }
    }

    public void signUp(View view) {
        goToActivity(CreateAccountActivity.class);
        slideToRight();
    }

    public void signIn(View view) {
        goToActivity(LoginActivity.class);
        slideToRight();
    }

    public void signOff(View view){

        SharedPreferences.Editor editor= preferences.edit();
        editor.clear();
        editor.apply();
        Log.d("PREF", "removed token " + preferences.contains(prefToken));

        view = (Button) findViewById(R.id.buttonSignIn);
        view.setVisibility(View.VISIBLE);
        Button  b = (Button) findViewById(R.id.buttonSignUp);
        b.setVisibility(View.VISIBLE);
        b = (Button) findViewById(R.id.buttonSignOff);
        b.setVisibility(View.GONE);
        checkGameButtonState();
    }

    public void goToActivity(Class<?> classActivity){
        Intent intent = new Intent(this,classActivity);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(SIGNIN == requestCode){

        }
        else if(SIGNUP == requestCode){

        }
    }

    public void goToTraining(View view) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra(GAME_TYPE_ID, WHProtocol.WHGameType.TRAINING.ordinal());
        startActivity(i);
        slideToLeft();
    }

    public void goToChallenge(View view){

        View promptView = LayoutInflater.from(this).inflate(R.layout.prompt_challenge, null);
        AlertDialog.Builder alertDiaBuilder = new AlertDialog.Builder(this);
        alertDiaBuilder.setView(promptView);

        final EditText textChoice = (EditText) promptView.findViewById(R.id.username);
        final RadioButton radioUser = (RadioButton) promptView.findViewById(R.id.choice_username);
        final RadioButton radioGrid = (RadioButton) promptView.findViewById(R.id.choice_grid);
        final RadioButton radioRandom = (RadioButton) promptView.findViewById(R.id.choice_random);
        radioRandom.setChecked(true);
        textChoice.setVisibility(View.GONE);


        radioRandom.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    textChoice.setHint("");
                    textChoice.setVisibility(View.GONE);
                }
            }
        });

        radioUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    textChoice.setHint("Nom d'utilisateur");
                    textChoice.setVisibility(View.VISIBLE);
                }

            }
        });

        radioGrid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    textChoice.setHint("Numéro de grille");
                    textChoice.setVisibility(View.VISIBLE);
                }
            }
        });


        alertDiaBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        challengeChoice = textChoice.getText().toString();

                        if (radioUser.isChecked()) {
                            startChallenge(TYPE_MODE_CHALENGE.CHALLENGE_VS_USER);

                        } else if (radioGrid.isChecked()) {
                            startChallenge(TYPE_MODE_CHALENGE.CHALLENGE_VS_GRID);

                        } else if (radioRandom.isChecked()) {
                            startChallenge(TYPE_MODE_CHALENGE.RANDOM_GRID);
                        }

                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

        AlertDialog prompt = alertDiaBuilder.create();
        prompt.show();

    }


    public void startChallenge(TYPE_MODE_CHALENGE type){
        Intent i = new Intent(this, GameActivity.class);

        // mode challenge
        i.putExtra(GAME_TYPE_ID, WHProtocol.WHGameType.CHALLENGE.ordinal());

        // challenge type
        i.putExtra(CHALLENGE_TYPE, type.ordinal());

        // challenge type value
        i.putExtra(CHALLENGE_CHOICE, challengeChoice);
        startActivity(i);
        slideToLeft();
    }

    public void goToCompetition(View view){
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra(GAME_TYPE_ID, WHProtocol.WHGameType.COMPETITION.ordinal());
        startActivity(i);
        slideToLeft();
    }

    public void goToOfflineGame(View view){
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra(GAME_TYPE_ID, WHProtocol.WHGameType.OFFLINE.ordinal());
        startActivity(i);
        //slide from right to left
        slideToLeft();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem usernameItem = menu.findItem(R.id.username);

        usernameItem.setTitleCondensed(preferences.getString(prefUsername, "Déconnecté"));
        Log.d("MENU", "onPrepareOptionsMenu");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showPromptSettings();
            return true;
        } else if(id == R.id.compet_sheduling){
            goToActivity(CreateCompetition.class);
            slideToRight();
        }

        return super.onOptionsItemSelected(item);
    }

    public void slideToLeft(){
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void slideToRight(){
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void reply(WHMessage message) {

        if(message.getHeader() == WHProtocol.WHMessageHeader.PING_REPLY) {

            if(pingTestButton != null){
                pingTestButton.setText("Connexion OK !");
                pingTestButton.setBackgroundColor(getResources().getColor(R.color.GREEN));
            }
        }
        else{

            pingTestButton.setBackgroundColor(getResources().getColor(R.color.RED));
            Toast.makeText(this, "ERREUR " +
                    message.getHeader().toString() +
                    "  " +  message.toString(), Toast.LENGTH_LONG).show();

        }
    }


    public void showPromptSettings(){

        AlertDialog.Builder alertDiaBuilder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.prompt_settings,null);
        final EditText serveurIp = (EditText) view.findViewById(R.id.serveur_ip);
        final EditText serveurPort = (EditText) view.findViewById(R.id.serveur_port);

        serveurIp.setText(getPrefServeurIP());
        serveurPort.setText("" + getPrefServeurPort());

        pingTestButton = (Button) view.findViewById(R.id.button_test_ping);
        pingTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int port = getResources().getInteger(R.integer.default_port);
                String portUserInput = serveurPort.getText().toString();

                if(WHUtil.isInteger(portUserInput)) {
                    port =  Integer.parseInt(portUserInput);
                }

                setServeurPreferences(serveurIp.getText().toString(), port);
                testPing();
                pingTestButton.setText("Test en cours...");
            }
        });


        alertDiaBuilder.setView(view);

        alertDiaBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setServeurPreferences(serveurIp.getText().toString(), Integer.parseInt(serveurPort.getText().toString()));
                    }
                })

                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

//                        dialogInterface.cancel();
                    }
                });

        AlertDialog prompt = alertDiaBuilder.create();
        prompt.show();
    }

    public void setServeurPreferences(String ipAdress, int port){
        SharedPreferences.Editor editor= serveurPreferences.edit();
        editor.putString(prefServeurIP, ipAdress);
        editor.putInt(prefServeurPort, port);
        editor.commit();
    }

    public void testPing(){

        new WordHuntASyncTask(this).execute(
                new WHMessage(WHProtocol.WHMessageHeader.PING, "TEST PING")
        );
    }

    public static String getPrefServeurIP(){
        return serveurPreferences.getString(MainActivity.prefServeurIP, "");
    }

    public static int getPrefServeurPort(){
        return serveurPreferences.getInt(MainActivity.prefServeurPort, 1234);
    }
}
