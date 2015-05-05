package whprotocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

import whprotocol.WHProtocol.WHMessageHeader;

/**
 * Represent a generic request (a keyword as a header and a content payload
 * depending on this header)
 */
public class ActualRequest {
	private WHMessageHeader header;
	private WHMessageContent content;

	public ActualRequest(WHProtocol.WHMessageHeader header,
			WHMessageContent content) {
		super();
		this.header = header;
		this.content = content;
	}

	public static ActualRequest validateRequest(String data) {
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
		return new ActualRequest(header, content);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ActualRequest [header=" + header + ", content=" + content + "]";
	}

	public static ActualRequest readCommand(BufferedReader reader)
			throws IOException {
		StringBuilder sb = new StringBuilder();
		String tmp;
		while (!((tmp = reader.readLine()).equals(""))) {
			sb.append(tmp);
			sb.append("\n");
		}
		return validateRequest(sb.toString());
	}

	public void writeCommand(PrintWriter writer) {
		writer.println(header.name());
		// TODO: true serialize
		writer.println(content);
		writer.println();
		writer.flush();
	}

	public static void writeCommand(PrintWriter writer, ActualRequest request) {
		writer.println(request.header.name());
		// TODO: true serialize
		writer.println(request.content);
		writer.println();
		writer.flush();
	}
}