package ch.heigvd.wordhuntclient.activities;

import java.util.concurrent.ExecutionException;

import protocol.Ping;
import protocol.Protocol.Request;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import ch.heigvd.gen.wordhuntclient.R;
import ch.heigvd.wordhuntclient.asynctask.WordHuntASyncTask;

public class MainActivity extends Activity {
	
	WordHuntASyncTask iotask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// auto-launch
		testPing();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void testPing () {
		String host = "192.168.0.18"; //"10.192.94.246";
		int port = 1234;
		WordHuntASyncTask iotask = new WordHuntASyncTask(host, port);
		System.out.println("Scheduling future iotask.");
		// test ping.
		iotask.execute(Request.PING, new Ping(0, "From client."));
		try {
			// blocking on response.
			Object received = iotask.get();
			System.out.println("Receiving back in GUI: " + received);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}