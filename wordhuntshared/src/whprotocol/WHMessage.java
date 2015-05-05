package whprotocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import whprotocol.WHProtocol.WHMessageHeader;

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

	/**
	 * Constructor.
	 * 
	 * @param header
	 *            the header
	 * @param content
	 *            the content
	 */
	public WHMessage(WHProtocol.WHMessageHeader header, WHMessageContent content) {
		super();
		this.header = header;
		this.content = content;
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
		switch (header) {
		case PING:
			// TODO: true deserialize
			content = new WHPing(0, payload);
			break;
		case PING_REPLY:
			// TODO: true deserialize
			content = new WHPing(0, payload);
			break;
		default:
			System.out.println("def");
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
	 * @throws IOException
	 */
	public void writeMessage(PrintWriter writer) {
		writer.println(header.name());
		// TODO: true serialize
		writer.println(content);
		writer.println();
		writer.flush();
	}

	/**
	 * Write a message to a stream.
	 * 
	 * @param writer
	 *            the stream to write
	 * @throws IOException
	 */
	public static void writeMessage(PrintWriter writer, WHMessage request) {
		writer.println(request.header.name());
		// TODO: true serialize
		writer.println(request.content);
		writer.println();
		writer.flush();
	}
}