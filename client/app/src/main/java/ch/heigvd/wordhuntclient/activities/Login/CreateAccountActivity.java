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
import whprotocol.WHMessage;
import whprotocol.WHProtocol;
import whprotocol.WHRegister;

public class CreateAccountActivity extends Activity implements IWHView {

    private Logger logger = Logger.getLogger(getClass().getName());
    private Button   buttonRegister;
    private TextView password;
    private TextView passwordConfirmation;
    private TextView username;
    private TextView email;
    private CheckBox rememberMe;
    private TextView messageReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutSignUp);

        buttonRegister = (Button) findViewById(R.id.buttonPerformSignUp);
        password = (TextView) layout.findViewById(R.id.textPassword);
        passwordConfirmation = (TextView) layout.findViewById(R.id.textPasswordConfirmation);
        username = (TextView) layout.findViewById(R.id.username);
        email = (TextView) layout.findViewById(R.id.textEmail);

    }

    public void onSignUp(View view) {

        Button b = (Button) view;
        b.setClickable(false);
        b.setText("Processing...");
        if (password.length() < 8) {
            Toast.makeText(getApplicationContext(), "Password should be 8 characters long!", Toast.LENGTH_LONG).show();
            password.setText("");
            passwordConfirmation.setText("");

            b.setClickable(true);
            b.setText("s'enregistrer");
        } else {
            new WordHuntASyncTask(this).execute(new WHMessage(WHProtocol.WHMessageHeader.REGISTER, new WHRegister(0, username.getText().toString(), password.getText().toString(), email.getText().toString())));
        }
    }

    @Override
    public void reply(WHMessage message) {
        logger.info("Received a reply in GUI.");
        logger.fine(message.toString());
        buttonRegister.setClickable(true);
        buttonRegister.setText("s'enregistrer");

        /**
         * If reply is REGISTER_ACCOUNT_CREATED_201, we go back to the main activity
         */
        CharSequence accepted = "Registration accepted";
        CharSequence badRequest = "Email or username already registered";
        switch(message.getHeader()){
            case REGISTER_ACCOUNT_CREATED_201 :
                Toast.makeText(getApplicationContext(),accepted , Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class );
                startActivity(intent);
                break;
            case BAD_REQUEST_400 :
                Toast.makeText(getApplicationContext(), badRequest , Toast.LENGTH_LONG).show();
                password.setText("");
                passwordConfirmation.setText("");
                username.setText("");
                email.setText("");
                break;
            default:

        }

    }

    @Override
    public String ipLocation() {
        return "192.168.42.1";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_account, menu);
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
