package tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import whprotocol.WHAuthMessage;
import whprotocol.WHLogin;
import whprotocol.WHMessage;
import whprotocol.WHProtocol.WHMessageHeader;
import whprotocol.WHSimpleMessage;

/**
 * Class that test the login of an already registered random user provided by
 * {@link TestAbstractLogin}.
 */
public class TestLogin extends TestAbstractLogin {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Right user, false password
	 */
	@Test
	public void testLoginRightUserFalsePassword() throws UnknownHostException,
			IOException {
		WHMessage.writeMessage(pw, new WHMessage(WHMessageHeader.AUTH_POST,
				new WHLogin(0, name, "false password")));
		WHMessage reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.AUTHENTICATE_BAD_CREDENTIALS,
				reply.getHeader());
		assertEquals(WHSimpleMessage.class, reply.getContent().getClass());
	}

	/**
	 * False user, right password
	 */
	@Test
	public void testLoginFalseUserRightPassword() throws UnknownHostException,
			IOException {
		WHMessage.writeMessage(pw, new WHMessage(WHMessageHeader.AUTH_POST,
				new WHLogin(0, "falseuser", password)));
		WHMessage reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.AUTHENTICATE_BAD_CREDENTIALS,
				reply.getHeader());
		assertEquals(WHSimpleMessage.class, reply.getContent().getClass());
	}

	/**
	 * Right user, right password
	 */
	@Test
	public void testLoginRightUserRightPassword() throws UnknownHostException,
			IOException {
		WHMessage.writeMessage(pw, new WHMessage(WHMessageHeader.AUTH_POST,
				new WHLogin(0, name, password)));
		WHMessage reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.AUTH_TOKEN, reply.getHeader());
		assertEquals(WHAuthMessage.class, reply.getContent().getClass());
	}

}
