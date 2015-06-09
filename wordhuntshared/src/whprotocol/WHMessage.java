package whprotocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import whprotocol.WHProtocol.WHMessageHeader;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Represent a generic message than can me exchanged in both ways. Offers method
 * of manipulation of an existing message, and read/write operations on streams.
 * <p>
 * A message consist of: <br>
 * - a header, which is a keyword defined in {@link WHMessageHeader} <br>
 * - a content payload, which depending on this header, defined in
 * {@link WHMessageContent} <br>
 */
public class WHMessage {
	/**
	 * Header of the message.
	 * 
	 * @see WHMessageHeader
	 */
	private WHMessageHeader header;
	/**
	 * Content of the message.
	 * 
	 * @see WHMessageContent
	 */
	private WHMessageContent content;

	private static Gson gson = new Gson();

	/**
	 * Constructor.
	 * 
	 * @param header
	 *            the header
	 * @param content
	 *            the content
	 */
	public WHMessage(WHMessageHeader header, WHMessageContent content) {
		super();
		this.header = header;
		this.content = content;
	}

	/**
	 * Constructor for a WHSimpleMessage with given string as content.
	 * 
	 * @param header
	 *            the header
	 * @param payload
	 *            the payload
	 */
	public WHMessage(WHMessageHeader header, String payload) {
		super();
		this.header = header;
		this.content = new WHSimpleMessage(0, payload);
	}

	/**
	 * @return the header
	 */
	public WHMessageHeader getHeader() {
		return header;
	}

	/**
	 * @return the content
	 */
	public WHMessageContent getContent() {
		return content;
	}

	/**
	 * To string method.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return header + "\n" + content + "\n";
	}

	/**
	 * Construct a valid WHMessage from a string input. Validate that the given
	 * input respects the protocol and return its WHMessage correspondence, of
	 * null if anything broke the protocol.
	 * 
	 * @param data
	 *            the input
	 * @return a valid WHMessage or null if any protocol break.
	 */
	public static WHMessage validateMessage(String data) {
		int idx = data.indexOf("\n");
		if (idx == -1) { // data should contain a \n
			System.err.println("\n error type 1.");
			return null;
		}
		int idxEnd = data.indexOf("\n", idx + 1);
		if (idx == -1 || idxEnd != data.length() - 1) {
			System.err.println("\n error type 2.");
			return null; // data should terminate by the second \n
		}
		WHMessageHeader header = null;
		try {
			header = WHMessageHeader.valueOf(data.substring(0, idx));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			System.err.println("header error.");
			return null;
		}

		String payload = data.substring(idx + 1, idxEnd);
		WHMessageContent content = null;
		try {
			switch (header) {
			case PING:
			case PING_REPLY:
			case CHEATING_WARNING_400:
			case CHEATING_BANNED_400:
			case AUTHENTICATE_BAD_CREDENTIALS:
			case REGISTER_ACCOUNT_CREATED_201:
			case SCHEDULE_COMPET_ACK:
			case SERVER_ERROR_500:
			case BAD_REQUEST_400:
			case AUTH_REQUIRED_403:
			case SUBMIT_VALIDATE:
			case NETWORK_ERROR:
				content = gson.fromJson(payload, WHSimpleMessage.class);
				break;
			case AUTH_TOKEN: // the token is NOT encapsulated in a simple message !
				content = gson.fromJson(payload, WHAuthMessage.class);
				break;
			case AUTH_POST:
				content = gson.fromJson(payload, WHLogin.class);
				break;
			case REGISTER:
				content = gson.fromJson(payload, WHRegister.class);
				break;
			case GRID_REPLY:
				content = gson.fromJson(payload, WHGridReplyMessage.class);
				break;
			case SUBMIT_POST:
				content = gson.fromJson(payload, WHSubmitPostMessage.class);
				break;
			case GRID_GET:
				// grid get can is a simple message (back ward compatibility a& simplicity)
				// for authenticated message, please see Grid_get_authenticated !
				content = gson.fromJson(payload, WHSimpleMessage.class);
				break;
			case ANSWERS_GET:
				System.err.println("Not implemented yet");
				break;
			case ANSWERS_REPLY:
				System.err.println("Not implemented yet");
				break;
			case SCHEDULE_COMPET:
				content = gson.fromJson(payload, WHCompetScheduling.class);
				break;
			case GRID_GET_AUTHENTICATED:
				content = gson.fromJson(payload, WHGetGrid.class);
				break;
			default:
				System.err.println("header not found / not implemented");
				return null;
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			System.err.println("json de/serialization error.");
			return null;
		}
		return new WHMessage(header, content);
	}

	/**
	 * Read a message from a stream.
	 * 
	 * @param reader
	 *            the stream to read from
	 * @return the message read, or null if protocol error or end of stream reached
	 * @throws IOException
	 */
	public static WHMessage readMessage(BufferedReader reader)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		String tmp;
		// read line by line, with end of stream check
		while (((tmp = reader.readLine()) != null && !tmp.equals(""))) {
			sb.append(tmp);
			sb.append("\n");
		}
		if (null == tmp) {
			// end of stream reached
			return null;
		}
		return validateMessage(sb.toString());
	}

	/**
	 * Write a message to a stream.
	 * 
	 * @param writer
	 *            the stream to write
	 * @return true if everything successful and flushed.
	 */
	public boolean writeMessage(PrintWriter writer) {
		writer.println(header.name());
		writer.println(content);
		writer.println();
		// same as flush with verification
		return !writer.checkError();
	}

	/**
	 * Write a message to a stream.
	 * 
	 * @param writer
	 *            the stream to write
	 * @return true if everything successful and flushed.
	 */
	public static boolean writeMessage(PrintWriter writer, WHMessage request) {
		writer.println(request.header.name());
		writer.println(request.content);
		writer.println();
		// same as flush with verification
		return !writer.checkError();
	}
}