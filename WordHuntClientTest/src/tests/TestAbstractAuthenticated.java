package tests;

import org.junit.After;
import org.junit.Before;

import whprotocol.WHLogin;
import whprotocol.WHMessage;
import whprotocol.WHProtocol.WHMessageHeader;
import whprotocol.WHSimpleMessage;

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
				new WHLogin(0, name, email)));
		WHSimpleMessage reply = (WHSimpleMessage) WHMessage.readMessage(br)
				.getContent();
		token = reply.getPayload();
	}

	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
