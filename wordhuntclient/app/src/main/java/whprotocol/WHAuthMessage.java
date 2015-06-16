package whprotocol;

/**
 * Generic authenticated message.
 */
public class WHAuthMessage extends WHMessageContent {

	private String authToken;

	/**
	 * @return the authToken
	 */
	public String getAuthToken() {
		return authToken;
	}

	/**
	 * @param authToken
	 *            the authToken to set
	 */
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public WHAuthMessage(int status, String authToken) {
		super(status);
		this.authToken = authToken;
	}
	
	public WHAuthMessage(String authToken) {
		this.authToken = authToken;
	}
}