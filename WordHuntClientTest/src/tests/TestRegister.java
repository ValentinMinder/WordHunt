package tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import whprotocol.WHMessage;
import whprotocol.WHProtocol.WHMessageHeader;
import whprotocol.WHRegister;
import whprotocol.WHSimpleMessage;

/**
 * Class that test the registration of a random user provided by
 * {@link TestAbstractRegister}.
 */
public class TestRegister extends TestAbstractRegister {

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Tries to register a user and receive acknowledgment.
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	@Test
	public void testRegisterSimple() throws UnknownHostException, IOException {
		WHMessage query = new WHMessage(WHMessageHeader.REGISTER,
				new WHRegister(0, name, "secret", email));
		query.writeMessage(pw);
		WHMessage reply = WHMessage.readMessage(br);
		System.out.println(reply);
		assertEquals(reply.getHeader(),
				WHMessageHeader.REGISTER_ACCOUNT_CREATED_201);
		assertEquals(reply.getContent().getClass(), WHSimpleMessage.class);
	}

	/**
	 * Tries to multiple-register the same user and receive bad request
	 * acknowledgment.
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	@Test
	public void testRegisterMutipleMustBeDenied() throws UnknownHostException,
			IOException {
		WHRegister reg = new WHRegister(0, name, "secret", email);
		WHMessage query = new WHMessage(WHMessageHeader.REGISTER, reg);
		query.writeMessage(pw);
		WHMessage reply = WHMessage.readMessage(br);
		assertEquals(reply.getHeader(),
				WHMessageHeader.REGISTER_ACCOUNT_CREATED_201);
		assertEquals(reply.getContent().getClass(), WHSimpleMessage.class);

		// email & username already in use
		query = new WHMessage(WHMessageHeader.REGISTER, reg);
		query.writeMessage(pw);
		reply = WHMessage.readMessage(br);
		assertEquals(reply.getHeader(), WHMessageHeader.BAD_REQUEST_400);
		assertEquals(reply.getContent().getClass(), WHSimpleMessage.class);

		// email already in use
		reg = new WHRegister(0, name + rand.nextInt(), "secret", email);
		query = new WHMessage(WHMessageHeader.REGISTER, reg);
		query.writeMessage(pw);
		reply = WHMessage.readMessage(br);
		assertEquals(reply.getHeader(), WHMessageHeader.BAD_REQUEST_400);
		assertEquals(reply.getContent().getClass(), WHSimpleMessage.class);

		// username already in use
		reg = new WHRegister(0, name, "secret", email + rand.nextInt());
		query = new WHMessage(WHMessageHeader.REGISTER, reg);
		query.writeMessage(pw);
		reply = WHMessage.readMessage(br);
		assertEquals(reply.getHeader(), WHMessageHeader.BAD_REQUEST_400);
		assertEquals(reply.getContent().getClass(), WHSimpleMessage.class);
	}

}
