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

import whprotocol.WHMessage;
import whprotocol.WHProtocol.WHMessageHeader;
import whprotocol.WHSimpleMessage;

/**
 * Created by Karim Ghozlani on 29.04.2015.
 */

public class WordHuntServer implements Runnable {

	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private BufferedReader reader = null;
	private PrintWriter writer = null;

	// private ArrayList<Socket> clientSockets;

	private WHMessage handleClient(WHMessage clientCommand) {
		// do stuff
		System.out.println("RECV: " + clientCommand);

		switch (clientCommand.getHeader()) {
		case PING:
			// replies in echo with a ping reply.
			WHSimpleMessage simple = (WHSimpleMessage) clientCommand
					.getContent();
			return new WHMessage(WHMessageHeader.PING_REPLY,
					simple.getPayload() + " - read by the server.");
		case ANSWERS_GET:
			break;
		case AUTH_POST:
			break;
		case GRID_GET:
			break;
		case REGISTER:
			break;
		case SCHEDULE_COMPET:
			break;
		case SUBMIT_POST:
			break;
		case PING_REPLY:
		case ANSWERS_REPLY:
		case AUTH_TOKEN:
		case GRID_REPLY:
		case CHEATING_WARNING_400:
		case CHEATING_BANNED_400:
		case AUTHENTICATE_BAD_CREDENTIALS:
		case REGISTER_ACCOUNT_CREATED_201:
		case SCHEDULE_COMPET_ACK:
		case SERVER_ERROR_500:
		case BAD_REQUEST_400:
		case AUTH_REQUIRED_403:
		case SUBMIT_VALIDATE:
		default:
			// forbidden command
			return new WHMessage(WHMessageHeader.BAD_REQUEST_400,
					"Request not acceptable from client: "
							+ clientCommand.getHeader());
		}

		// should never arise.
		return null;
	}

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
				clientSocket = serverSocket.accept();
				reader = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));
				writer = new PrintWriter(new OutputStreamWriter(
						clientSocket.getOutputStream()));

				WHMessage ar = WHMessage.readMessage(reader);
				WHMessage.writeMessage(writer, handleClient(ar));

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
