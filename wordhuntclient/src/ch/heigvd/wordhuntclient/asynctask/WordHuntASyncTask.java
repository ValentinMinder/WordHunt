package ch.heigvd.wordhuntclient.asynctask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;

/**
 * <code>WordHuntASyncTask</code> 
 * 
 * @author Valentin MINDER 
 * 
 */

public class WordHuntASyncTask
		extends AsyncTask<Object, Object, Object> {

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	final String charset =  "UTF-8";
	
	private String dstAddress;
	private int port;

	public WordHuntASyncTask(String dstAddress, int port) throws IOException {
		this.dstAddress = dstAddress;
		this.port = port;
	}
	
	private void init () throws UnknownHostException, IOException {
		socket = new Socket(dstAddress, port);
		out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), charset));
		in = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
	}

	@Override
	protected Object doInBackground(Object... params) {
		try {
			if (socket == null) {
				init();
			}
			// sending everyting to server
			for (Object object : params) {
				out.println(object);
				System.out.println("Sending: " + object);
			}
			out.println();
			out.flush();
			
			// waiting for a response
			StringBuilder sb = new StringBuilder();
			String tmp;
			try {
				while (!(tmp = in.readLine()).equals("")) {
					sb.append(tmp);
					sb.append("\n");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return  "null";
			}
			System.out.println("Received back in asynctask: " + sb);
			return sb;
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "null";
		
	}
}