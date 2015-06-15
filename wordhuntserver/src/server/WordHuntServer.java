package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import whproperties.WHProperties;

import clienthandler.ClientHandler;

/**
 * Created by Karim Ghozlani on 29.04.2015.
 */

public class WordHuntServer implements Runnable {

	private ServerSocket serverSocket = null;
    public static final String PROPERTYFILENAMESERVER = "server.properties";

	private Logger logger = Logger.getLogger(WordHuntServer.class.getName());

	private WHProperties serverProperties;

	public WordHuntServer() throws IOException {
		try {
			serverProperties = new WHProperties(PROPERTYFILENAMESERVER);
			int port = serverProperties.getInteger("PORT");
			serverSocket = new ServerSocket(port);
			logger.setLevel(Level.ALL);
			logger.log(Level.INFO, "INIT: Server initialized on port: " + port);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "FATAL: Cannot bind server.");
			throw e;
		}
	}

	public static void main(String[] args) {
		try {
			WordHuntServer wordHuntServer = new WordHuntServer();
			new Thread(wordHuntServer).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		logger.log(Level.INFO, "CONFIG: Listing all available IP on server.");
		try {
			Enumeration e = NetworkInterface.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				NetworkInterface n = (NetworkInterface) e.nextElement();
				Enumeration ee = n.getInetAddresses();
				while (ee.hasMoreElements()) {
					InetAddress i = (InetAddress) ee.nextElement();
					logger.log(Level.INFO,
							"CONFIG: Host: " + i.getHostAddress() + " - Name: "
									+ i.getCanonicalHostName());
				}
			}
		} catch (SocketException e1) {
			logger.log(Level.WARNING,
					"FAILED: Listing all available IP on server.");
			e1.printStackTrace();
		}

		while (true) {
			try {
				logger.log(Level.INFO, "RUN: Waiting for new client.");
				Socket client = serverSocket.accept();
				new Thread(new ClientHandler(client)).start();
				logger.log(Level.INFO, "RUN: New client arrived from: "
						+ client.getInetAddress());
			} catch (IOException e) {
				logger.log(Level.SEVERE,
						"FAILED: failure on new client arrived: skipping.");
				e.printStackTrace();
			}
		}
	}
}
