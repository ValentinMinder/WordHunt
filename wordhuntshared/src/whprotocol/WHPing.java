package whprotocol;

/**
 * This class aims to represent a "PING" and "PING_REPLY" structure.
 * <p>
 * 
 * @author Valentin MINDER
 */
public class WHPing extends WHMessageContent {

	private String payload;

	public WHPing(int status, String payload) {
		super(status);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WHPing [payload=" + payload + "]";
	}

}
