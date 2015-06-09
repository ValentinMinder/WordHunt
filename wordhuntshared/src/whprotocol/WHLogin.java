package whprotocol;

/**
 * Protocol to login a user!
 */
public class WHLogin extends WHMessageContent {	

	private String username;
	private String password;

	public WHLogin(int status, String username, String password) {
		super(status);
		this.username = username;
		this.password = password;
	}

	public WHLogin(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
