package ch.heigvd.wordhuntclient.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import ch.heigvd.gen.wordhuntclient.R;
import ch.heigvd.wordhuntclient.activities.Game.GameActivity;
import ch.heigvd.wordhuntclient.activities.Login.CreateAccountActivity;
import ch.heigvd.wordhuntclient.activities.Login.LoginActivity;
import ch.heigvd.wordhuntclient.activities.Test.TestActivity;

public class MainActivity extends Activity{

    //identifiant des requêtes
    public static int SIGNUP = 0;
    public static int SIGNIN = 1;

    public final static String CLASS_ID = "ch.heigvd.wordhuntclient.activities.MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void gotoDev(View view) {
        Intent i = new Intent(this, DevActivity.class);
        startActivity(i);
    }

    public void signUp(View view) {
        Intent i = new Intent(this, CreateAccountActivity.class);
        startActivity(i);
    }

    public void signIn(View view) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    public void goToTest(View view){
        Intent i = new Intent(this, TestActivity.class);
        startActivity(i);
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
        startActivity(i);
    }
}