package clienthandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import whprotocol.WHMessage;
import whprotocol.WHProtocol.WHMessageHeader;
import whprotocol.WHSimpleMessage;

public class ClientHandler implements Runnable {

	private Socket clientSocket = null;
	private BufferedReader reader = null;
	private PrintWriter writer = null;

	public ClientHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO log.
		}
	}

	private void init() throws IOException {
		reader = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		writer = new PrintWriter(new OutputStreamWriter(
				clientSocket.getOutputStream()));
	}

	public void run() {
		WHMessage ar;
		try {
			ar = WHMessage.readMessage(reader);
			WHMessage.writeMessage(writer, handleClient(ar));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			WHMessage.writeMessage(writer,
					handleClient(new WHMessage(
							WHMessageHeader.SERVER_ERROR_500,
							"Internal Server Error.")));
		}

	}

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

}
