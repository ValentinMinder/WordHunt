package whprotocol;

/**
 * Protocol to register a new user!
 */
public class WHRegister extends WHLogin {
	private String email;

	public WHRegister(int status, String username, String password, String email) {
		super(status, username, password);
		this.email = email;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

}
