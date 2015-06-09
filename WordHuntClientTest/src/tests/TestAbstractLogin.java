package tests;

import org.junit.After;
import org.junit.Before;

import whprotocol.WHMessage;
import whprotocol.WHProtocol.WHMessageHeader;
import whprotocol.WHRegister;

/**
 * Abstract class that creates data for a random registered user (but no logged
 * in).
 */
public abstract class TestAbstractLogin extends TestAbstractRegister {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		WHRegister reg = new WHRegister(0, name, password, email);
		WHMessage
				.writeMessage(pw, new WHMessage(WHMessageHeader.REGISTER, reg));
		WHMessage.readMessage(br);
	}

	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
