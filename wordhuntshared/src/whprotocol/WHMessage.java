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
		return "WHMessage [header=" + header + ", content=" + content + "]";
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
			return null;
		}
		int idxEnd = data.indexOf("\n", idx + 1);
		if (idx == -1 || idxEnd != data.length() - 1) {
			return null; // data should terminate by the second \n
		}
		WHMessageHeader header = null;
		try {
			header = WHMessageHeader.valueOf(data.substring(0, idx));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
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
				content = gson.fromJson(payload, WHSimpleMessage.class);
				break;
			default:
				System.err.println("not implemented yet.");
				return null;
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			System.err.println("json error.");
			return null;
		}
		return new WHMessage(header, content);
	}

	/**
	 * Read a message from a stream.
	 * 
	 * @param reader
	 *            the stream to read from
	 * @return the message read, or null if protocol error.
	 * @throws IOException
	 */
	public static WHMessage readMessage(BufferedReader reader)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		String tmp;
		while (!((tmp = reader.readLine()).equals(""))) {
			sb.append(tmp);
			sb.append("\n");
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