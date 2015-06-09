package clienthandler;

import gridhandler.GridHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

import whobjects.Grid;
import whprotocol.WHCompetScheduling;
import whprotocol.WHGridReplyMessage;
import whprotocol.WHLogin;
import whprotocol.WHMessage;
import whprotocol.WHProtocol.WHMessageHeader;
import whprotocol.WHRegister;
import whprotocol.WHSimpleMessage;
import whprotocol.WHSubmitPostMessage;

import competition.CompetitionManager;
import database.User;

public class ClientHandler implements Runnable {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private Socket clientSocket = null;
	private BufferedReader reader = null;
	private PrintWriter writer = null;
	private InetAddress ip = null;

	public ClientHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
		try {
			init();
		} catch (IOException e) {
			logger.severe("FAIL: client cannot be binded / init with reader/writer.");
			e.printStackTrace();
		}
	}

	private void init() throws IOException {
		reader = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));
		writer = new PrintWriter(new OutputStreamWriter(
				clientSocket.getOutputStream()));
		ip = clientSocket.getInetAddress();
	}

	public void run() {
		// infinite loop for client (has long has connection not broken)
		while (true) {
			try {
				logger.fine("Reading query from client");
				
				// in case of broken connection: finish
				if (null == reader) {
					break;
				}
				WHMessage query = WHMessage.readMessage(reader);
				if (null == query) {
					WHMessage.writeMessage(writer, new WHMessage(
							WHMessageHeader.BAD_REQUEST_400,
							"Bad Request - refused."));
					break;
				}
				logger.finest(query.toString());

				WHMessage reply = handleClient(query);
				logger.fine("Reply ready to send to client");
				logger.finest(reply.toString());

				boolean result = reply.writeMessage(writer);
				if (result) {
					logger.fine("Reply sent to client");
				} else {
					logger.warning("Couldn't send to reply to the client");
				}
			} catch (IOException e) {
				logger.severe("FAIL: I/O ex when reading message");
				e.printStackTrace();
				boolean result = WHMessage.writeMessage(writer, new WHMessage(
						WHMessageHeader.SERVER_ERROR_500,
						"Internal Server Error."));
				if (!result) {
					logger.warning("Couldn't send error message to the client");
				}
				break;
			}
		}

		logger.info("Client has disconnected from IP: " + ip);
	}

	/**
	 * Handles a query and produce the appropriate answer.
	 * 
	 * @param clientCommand
	 * @return
	 */
	private WHMessage handleClient(WHMessage clientCommand) {
		logger.finer("RECV: recived query " + clientCommand.getHeader());
		logger.finest(clientCommand.toString());

		switch (clientCommand.getHeader()) {
		case PING:
			// replies in echo with a ping reply.
			WHSimpleMessage simple = (WHSimpleMessage) clientCommand
					.getContent();
			return new WHMessage(WHMessageHeader.PING_REPLY,
					simple.getPayload() + " - read by the server.");
		case GRID_GET:
			WHGridReplyMessage reply;
			Grid grid = GridHandler.getInstance().getGrid();
			reply = new WHGridReplyMessage(0, grid);
			return new WHMessage(WHMessageHeader.GRID_REPLY, reply);
		case ANSWERS_GET:
			// TODO not implemented yet
			logger.warning("WARN: client issued bad request with NOT IMPLEMENTED command:"
					+ clientCommand.getHeader());
			return new WHMessage(WHMessageHeader.BAD_REQUEST_400,
					"NOT IMPLEMENTS YET " + clientCommand.getHeader());
		case AUTH_POST:
			WHLogin message = (WHLogin) clientCommand.getContent();
			User user = new User(message.getUsername(), message.getPassword());
			return user.correctCredentials();
		case REGISTER:
			WHRegister mess = (WHRegister) clientCommand.getContent();
			User u = new User(mess.getUsername(), mess.getEmail(),
					mess.getPassword());
			return u.registerUser();
		case SCHEDULE_COMPET:
			WHCompetScheduling compet = (WHCompetScheduling) clientCommand
					.getContent();
			return CompetitionManager.getInstance().schedule(compet);
		case SUBMIT_POST:
			WHSubmitPostMessage post = (WHSubmitPostMessage) clientCommand.getContent();
			
			//TODO verify token with user
			//post.getAuthToken();
			
			//TODO retrieve grid from id
			int score = 0;
			for (String word: post.getUserSolutions()) {
				// if word is not in solutions -> cheating!
					//return new WHMessage(WHMessageHeader.CHEATING_BANNED_400, "u suck >:(");
				// else count score
			}
			return new WHMessage(WHMessageHeader.SUBMIT_VALIDATE,
					new WHSimpleMessage(score, "Solution accepted"));
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
			// forbidden command
			logger.info("WARN: client issued bad request with forbidden command:"
					+ clientCommand.getHeader());
			return new WHMessage(WHMessageHeader.BAD_REQUEST_400,
					"Request NOT ACCEPTABLE FROM client: "
							+ clientCommand.getHeader());
		default:
			// WTF command ?!?
			logger.info("WARN: client issued bad request with NOT UNDERSTOOD command:"
					+ clientCommand.getHeader());
			return new WHMessage(WHMessageHeader.BAD_REQUEST_400,
					"Request NOT UNDERSTOOD from client: "
							+ clientCommand.getHeader());

		}
	}
}
