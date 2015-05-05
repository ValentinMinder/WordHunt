package whprotocol;

/**
 * This class aims to represent a "PING" and "PING_REPLY" structure.
 * <p>
 * 
 * @author Valentin MINDER
 */
public class Ping {
	private int status;
	private String payload;

	public Ping(int status, String payload) {
		super();
		this.status = status;
		this.payload = payload;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
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
