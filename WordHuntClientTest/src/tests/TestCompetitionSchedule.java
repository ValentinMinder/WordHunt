package tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import whprotocol.WHCompetScheduling;
import whprotocol.WHLogin;
import whprotocol.WHMessage;
import whprotocol.WHProtocol.WHMessageHeader;
import whprotocol.WHSimpleMessage;

/**
 * Class that test administration functionalities, like scheduling a
 * competition.
 */
public class TestCompetitionSchedule extends TestAbstractAuthenticated {

	private String adminToken;

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}

	/** Utily method that provide admin connection */
	private void connectAsAdmin() throws IOException {
		// creates admin
		// WHMessage.writeMessage(pw, new WHMessage(WHMessageHeader.REGISTER,
		// new WHRegister(0, "admin", "adminPassword", "admin@wordhunt.com")));
		// WHMessage.readMessage(br);
		WHMessage.writeMessage(pw, new WHMessage(WHMessageHeader.AUTH_POST,
				new WHLogin(0, "admin", "adminPassword")));
		WHMessage message = WHMessage.readMessage(br);
		System.out.println(message);
		adminToken = ((WHSimpleMessage) message.getContent()).getPayload();
	}

	/**
	 * Admin creates a competition.
	 */
	@Test
	public void testScheduleOKWhenAdmin() throws IOException {

		connectAsAdmin();

		// scheduling a competition: ok.
		WHMessage.writeMessage(pw, new WHMessage(
				WHMessageHeader.SCHEDULE_COMPET, new WHCompetScheduling(
						adminToken, 1000, 500)));
		WHMessage message = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.SCHEDULE_COMPET_ACK, message.getHeader());

		// schedulin another competition: not ok.
		// because the previous on is still available.
		WHMessage.writeMessage(pw, new WHMessage(
				WHMessageHeader.SCHEDULE_COMPET, new WHCompetScheduling(
						adminToken)));
		message = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.BAD_REQUEST_400, message.getHeader());

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// scheduling another competition: ok. (the previous one is done)
		WHMessage.writeMessage(pw, new WHMessage(
				WHMessageHeader.SCHEDULE_COMPET, new WHCompetScheduling(
						adminToken, 1000, 500)));
		message = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.SCHEDULE_COMPET_ACK, message.getHeader());
	}

	/**
	 * Non-admin can't launch competitions !
	 */
	@Test
	public void testScheduleRefusedWhenNotAdmin() throws IOException {
		// a non-admin random user tries to launch a competition !
		WHMessage
				.writeMessage(pw, new WHMessage(
						WHMessageHeader.SCHEDULE_COMPET,
						new WHCompetScheduling(token)));
		WHMessage message = WHMessage.readMessage(br);
		// TODO: WHMessageHeader.AUTH_REQUIRED_403
		assertEquals(WHMessageHeader.BAD_REQUEST_400, message.getHeader());
	}

}
