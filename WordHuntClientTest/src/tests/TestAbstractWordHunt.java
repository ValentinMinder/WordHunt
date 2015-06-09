package tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import org.junit.After;
import org.junit.Before;

/**
 * Top-class for all client-test: set up and ensure a connection to a server. It
 * closes the connection at the end.
 */
public abstract class TestAbstractWordHunt {
	/** socket and streams */
	private Socket socket;
	protected PrintWriter pw;
	protected BufferedReader br;

	/** Source of randomness */
	protected static Random rand = new Random();

	@Before
	// no @Override, topclass
	public void setUp() throws Exception {
		try {
			socket = new Socket("localhost", 1234);
			InputStream is = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			pw = new PrintWriter(out);
			br = new BufferedReader(new InputStreamReader(is));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@After
	// no @Override, topclass
	public void tearDown() throws Exception {
		pw.close();
		br.close();
		socket.close();
	}
}