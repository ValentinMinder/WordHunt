package ch.heigvd.wordhuntclient.activities;

import java.util.concurrent.ExecutionException;

import whprotocol.WHMessage;
import whprotocol.WHPing;
import whprotocol.WHProtocol.WHMessageHeader;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import ch.heigvd.gen.wordhuntclient.R;
import ch.heigvd.wordhuntclient.asynctask.WordHuntASyncTask;

public class MainActivity extends Activity {

	WordHuntASyncTask iotask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final Button b = (Button) findViewById(R.id.button_test_ping);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				b.setClickable(false);
				b.setText("Processing...");
				String reply = testPing();
				if (reply != null) {
					b.setText(reply);
				} else {
					b.setText("Error. Try again?");
				}
				b.setClickable(true);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private String testPing() {
		String host = "10.192.94.186"; // "10.192.94.246";
		final EditText b = (EditText) findViewById(R.id.btn_IP);
		String t = b.getText().toString();
		if (t != null && !t.equals("")) {
			host = t;
		}
		int port = 1234;
		WordHuntASyncTask iotask = new WordHuntASyncTask(host, port);
		System.out.println("Scheduling future iotask.");
		// test ping.
		iotask.execute(WHMessageHeader.PING, new WHPing(0, "Ping from client."));
		try {
			// blocking on response.
			Object received = iotask.get();
			WHMessage actual = (WHMessage) received;
			System.out.println("Receiving back in GUI: " + actual);
			return actual.toString();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassCastException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "error.";
	}
}