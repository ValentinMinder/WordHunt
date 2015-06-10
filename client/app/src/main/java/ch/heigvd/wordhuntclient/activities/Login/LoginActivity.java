package ch.heigvd.wordhuntclient.activities.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.logging.Logger;

import ch.heigvd.gen.wordhuntclient.R;
import ch.heigvd.wordhuntclient.activities.IWHView;
import ch.heigvd.wordhuntclient.activities.MainActivity;
import ch.heigvd.wordhuntclient.asynctask.WordHuntASyncTask;
import whprotocol.WHAuthMessage;
import whprotocol.WHLogin;
import whprotocol.WHMessage;
import whprotocol.WHProtocol;

public class LoginActivity extends Activity implements IWHView {

    private Logger logger = Logger.getLogger(getClass().getName());

    private Button buttonConnect;
    private TextView password;
    private TextView username;
    private CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutSignIn);
        buttonConnect = (Button) layout.findViewById(R.id.buttonPerformSignIn);
        password = (TextView) layout.findViewById(R.id.textPassword);
        username = (TextView) layout.findViewById(R.id.textUserName);
        rememberMe = (CheckBox) layout.findViewById(R.id.rememberMe);
    }

    public void onSignIn(View view) {

        Button b = (Button) view;
        b.setClickable(false);
        b.setText("Processing...");
        new WordHuntASyncTask(this).execute(new WHMessage(WHProtocol.WHMessageHeader.AUTH_POST, new WHLogin(0, username.getText().toString(), password.getText().toString())));


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


    @Override
    public void reply(WHMessage message) {
        buttonConnect.setClickable(false);
        buttonConnect.setText("Processing...");


        CharSequence badCredentials = "Wrong credentials please retry until you figure out the password!!!";
        CharSequence connected = "Connected!!";
        CharSequence serverError = "Server Error. Please retry later";

        switch(message.getHeader()){
            case AUTHENTICATE_BAD_CREDENTIALS:
                Toast.makeText(getApplicationContext(),badCredentials , Toast.LENGTH_LONG).show();
                buttonConnect.setClickable(true);
                buttonConnect.setText("se connecter");
                break;
            case AUTH_TOKEN:
                //Correct Credentials
                Toast.makeText(getApplicationContext(),connected , Toast.LENGTH_LONG).show();
                AccessToken.getInstance().setToken( ((WHAuthMessage) message.getContent()).getAuthToken());
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

                break;
            default:
                Toast.makeText(getApplicationContext(),serverError , Toast.LENGTH_LONG).show();
                buttonConnect.setClickable(true);
                buttonConnect.setText("se connecter");
        }


    }

    @Override
    public String ipLocation() {
        return "192.168.42.1";
    }
}
