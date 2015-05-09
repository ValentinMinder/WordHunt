import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

import clienthandler.ClientHandler;

import whprotocol.WHMessage;
import whprotocol.WHProtocol.WHMessageHeader;
import whprotocol.WHSimpleMessage;

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
		while (true) {
			try {
				// ClientWorkers to implement, just ping test
				System.out.println("Hello. I'm listening on one of these IP");
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
				Socket clientSocket;
				clientSocket = serverSocket.accept();
				new Thread(new ClientHandler(clientSocket)).start();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
