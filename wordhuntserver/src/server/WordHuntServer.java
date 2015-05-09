package server;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Enumeration;

import clienthandler.ClientHandler;

/**
 * Created by Karim Ghozlani on 29.04.2015.
 */

public class WordHuntServer implements Runnable {

	private ServerSocket serverSocket = null;

	public WordHuntServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
	}

	public static void main(String[] args) {
		try {
			WordHuntServer wordHuntServer = new WordHuntServer(1234);
			new Thread(wordHuntServer).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// ClientWorkers to implement, just ping test
		System.out.println("Hello. I'm listening on one of these IP");
		try {
			Enumeration e = NetworkInterface.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				NetworkInterface n = (NetworkInterface) e.nextElement();
				Enumeration ee = n.getInetAddresses();
				while (ee.hasMoreElements()) {
					InetAddress i = (InetAddress) ee.nextElement();
					System.out.println(i.getHostAddress() + "-"
							+ i.getCanonicalHostName());
				}
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		while (true) {
			try {
				new Thread(new ClientHandler(serverSocket.accept())).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
