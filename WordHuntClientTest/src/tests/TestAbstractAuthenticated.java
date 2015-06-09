package tests;

import org.junit.After;
import org.junit.Before;

import whprotocol.WHAuthMessage;
import whprotocol.WHLogin;
import whprotocol.WHMessage;
import whprotocol.WHProtocol.WHMessageHeader;

/**
 * Class for all authenticated - test. <br>
 * Abstract class that uses an random user (registered, and logged in).
 */
public abstract class TestAbstractAuthenticated extends TestAbstractLogin {
	protected String token;

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		WHMessage.writeMessage(pw, new WHMessage(WHMessageHeader.AUTH_POST,
				new WHLogin(0, name, password)));
		WHMessage read = WHMessage.readMessage(br);
		if (!WHAuthMessage.class.equals(read.getContent().getClass())) {
			System.err.println("Error - not logged in!");
			System.err.println(read);
			throw new RuntimeException("Error - logged in was a failure !");
		}
		WHAuthMessage reply = (WHAuthMessage) (read.getContent());
		token = reply.getAuthToken();
	}

	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
