package ch.heigvd.wordhunt.activities.Login;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ch.heigvd.wordhunt.activities.IWHView;
import ch.heigvd.wordhunt.activities.MainActivity;
import ch.heigvd.wordhunt.asynctask.WordHuntASyncTask;
import ch.heigvd.wordhunt.design.R;
import whprotocol.WHAuthMessage;
import whprotocol.WHLogin;
import whprotocol.WHMessage;
import whprotocol.WHProtocol;

/**
 * This Activity allow a registred user to connect
 * The Token and the username are saved in sharedPreference
 * So the app always remember the user
 */
public class LoginActivity extends Activity implements IWHView {

    private Button buttonConnect;
    private TextView password;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setAllowEnterTransitionOverlap(true);
        setContentView(R.layout.activity_login);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutSignIn);
        buttonConnect = (Button) layout.findViewById(R.id.buttonPerformSignIn);
        password = (TextView) layout.findViewById(R.id.textPassword);
        username = (TextView) layout.findViewById(R.id.textUserName);
        username.setText(MainActivity.preferences.getString(MainActivity.prefUsername,""));
    }

    @Override
    public void onBackPressed() {
        finish();
        slideToLeft();
    }

    /**
     * On button sign in clicked
     * @param view: the button
     */
    public void onSignIn(View view) {

        Button b = (Button) view;
        b.setClickable(false);
        b.setText(R.string.processing);

        new WordHuntASyncTask(this).execute(
                new WHMessage(
                        WHProtocol.WHMessageHeader.AUTH_POST,
                        new WHLogin(0, username.getText().toString(), password.getText().toString()))
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    /**
     * Response from server for authentication
     * @param message: message from server
     */
    @Override
    public void reply(WHMessage message) {
        buttonConnect.setClickable(false);
        buttonConnect.setText(R.string.processing);

        switch(message.getHeader()){

            case AUTHENTICATE_BAD_CREDENTIALS:
                // Bad credentials
                Toast.makeText(getApplicationContext(),R.string.bad_credentials, Toast.LENGTH_LONG).show();
                buttonConnect.setClickable(true);
                buttonConnect.setText(R.string.connect);
                break;

            case AUTH_TOKEN:
                //Correct Credentials
                Toast.makeText(this, R.string.connected, Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = MainActivity.preferences.edit();
                editor.putString(MainActivity.prefToken,((WHAuthMessage) message.getContent()).getAuthToken());
                editor.putString(MainActivity.prefUsername, username.getText().toString());
                editor.apply();
                finish();
                slideToLeft();
                break;

            default:
                // Server Error
                Toast.makeText(this, R.string.server_error, Toast.LENGTH_LONG).show();
                buttonConnect.setClickable(true);
                buttonConnect.setText(R.string.connect);
        }
    }

    public void slideToLeft(){
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}
