package ch.heigvd.wordhuntclient.activities;

import java.util.logging.Logger;

import whprotocol.WHMessage;
import whprotocol.WHProtocol.WHMessageHeader;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import ch.heigvd.gen.wordhuntclient.R;
import ch.heigvd.wordhuntclient.asynctask.WordHuntASyncTask;

public class MainActivity extends Activity implements IWHView {

	private Logger logger = Logger.getLogger(getClass().getName());

	private WordHuntASyncTask iotask;
	public static SharedPreferences preferences;

	public static final String prefLocation = "ch.heigvd.wordhuntclient.PREFERENCES";
	public static final String prefLastIP = "LAST_USED_IP";

	/*
	 * ANDROID ON CREATE ACTIVITY (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		logger.info("--- Launching the App ! --- ");

		// init preferences
		preferences = getSharedPreferences(prefLocation, Context.MODE_PRIVATE);
		iotask = new WordHuntASyncTask(this);

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

	/*
	 * ANDROID ON CREATE OPTION MENU. (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// ------- UI CALLBACKS ------------ //

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
		iotask.execute(new WHMessage(WHMessageHeader.PING, "Ping from client."));
	}

	// ---- IO TASK REPLY ---- //

	@Override
	public void reply(WHMessage message) {
		logger.info("Received a reply in GUI.");
		logger.fine(message.toString());
		// TODO handle the reply correctly!!!
		Button b = (Button) findViewById(R.id.button_test_ping);
		b.setText(message.toString());
		b.setClickable(true);
	}
}