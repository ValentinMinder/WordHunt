package tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Test;

import whprotocol.WHMessage;
import whprotocol.WHProtocol.WHMessageHeader;
import whprotocol.WHSimpleMessage;

public class TestPing extends TestAbstractWordHunt {

	/**
	 * Sends and receive a ping (basic two-ways interchange communication).
	 */
	@Test
	public void testPing() throws UnknownHostException, IOException {
		String message = "Hello from client!" + rand.nextInt();

		WHMessage ping = new WHMessage(WHMessageHeader.PING, message);
		ping.writeMessage(pw);

		WHMessage reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.PING_REPLY, reply.getHeader());
		assertEquals(WHSimpleMessage.class, reply.getContent().getClass());

		WHSimpleMessage replied = (WHSimpleMessage) reply.getContent();
		assertEquals(replied.getPayload(), message + " - read by the server.");
	}

}
