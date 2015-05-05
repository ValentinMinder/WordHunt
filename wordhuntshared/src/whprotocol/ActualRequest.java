package whprotocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represent a request (a keyword and a command payload)
 */
public class ActualRequest {
	private String command;
	private String payload;

	private static Collection<WHProtocol.WHRequest> requests = new ArrayList<WHProtocol.WHRequest>();
	{
		WHProtocol.WHRequest[] values = WHProtocol.WHRequest.values();
		for (WHProtocol.WHRequest request : values) {
			requests.add(request);
		}
	}

	public ActualRequest(String command, String payload) {
		super();
		this.command = command;
		this.payload = payload;
	}

	public ActualRequest(WHProtocol.WHRequest command, String payload) {
		super();
		this.command = command.name();
		this.payload = payload;
	}

	public static ActualRequest validateRequest(String data) {
		int idx = data.indexOf("\n");
		if (idx == -1) { // data should contain a \n
			return null;
		}
		int idxEnd = data.indexOf("\n", idx);
		if (idx == -1 || idxEnd != data.length() - 1) {
			return null; // data should terminate by the second \n
		}

		String command = data.substring(0, idx);
		if (!requests.contains(command)) {
			return null;
		}
		String payload = data.substring(idx + 1, idxEnd);
		return new ActualRequest(command, payload);
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
		writer.println(command);
		writer.println(payload);
		writer.println();
		writer.flush();
	}

	public static void writeCommand(PrintWriter writer, ActualRequest request) {
		writer.println(request.command);
		writer.println(request.payload);
		writer.println();
		writer.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ActualRequest [command=" + command + ", payload=" + payload
				+ "]";
	}

	/**
	 * @return the command
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
	}

	/**
	 * @return the payload
	 */
	public String getPayload() {
		return payload;
	}

	/**
	 * @param payload
	 *            the payload to set
	 */
	public void setPayload(String payload) {
		this.payload = payload;
	}
}
