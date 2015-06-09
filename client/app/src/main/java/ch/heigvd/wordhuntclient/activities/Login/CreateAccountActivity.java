package ch.heigvd.wordhuntclient.activities.Login;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.heigvd.gen.wordhuntclient.R;
import ch.heigvd.wordhuntclient.activities.IWHView;
import whprotocol.WHMessage;

public class CreateAccountActivity extends Activity implements IWHView {

    private Button   buttonRegister;
    private TextView password;
    private TextView passwordConfirmation;
    private TextView username;
    private TextView email;
    private CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutSignUp);

        buttonRegister = (Button) findViewById(R.id.buttonSignUp);
        password = (TextView) layout.findViewById(R.id.textPassword);
        passwordConfirmation = (TextView) layout.findViewById(R.id.textPasswordConfirmation);
        username = (TextView) layout.findViewById(R.id.username);
        email = (TextView) layout.findViewById(R.id.textEmail);
        rememberMe = (CheckBox) layout.findViewById(R.id.rememberMe);
    }

    public void onSignUp(View view) {


    }

    @Override
    public void reply(WHMessage message) {

    }

    @Override
    public String ipLocation() {
        return "127.0.0.1";
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
