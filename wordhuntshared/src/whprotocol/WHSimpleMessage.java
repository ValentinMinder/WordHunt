package whprotocol;

/**
 * This class aims to represent a "PING" and "PING_REPLY" structure.
 * <p>
 * 
 * @author Valentin MINDER
 */
public class WHSimpleMessage extends WHMessageContent {

	private String payload;

	public WHSimpleMessage(int status, String payload) {
		super(status);
		this.payload = payload;
	}
	
	public WHSimpleMessage(String payload) {
		super();
		this.payload = payload;
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
