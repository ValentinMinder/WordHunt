package tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import whprotocol.WHAuthMessage;
import whprotocol.WHCompetScheduling;
import whprotocol.WHGetGrid;
import whprotocol.WHGridReplyMessage;
import whprotocol.WHLogin;
import whprotocol.WHMessage;
import whprotocol.WHSimpleMessage;
import whprotocol.WHSubmitPostMessage;
import whprotocol.WHProtocol.WHGameType;
import whprotocol.WHProtocol.WHMessageHeader;
import whprotocol.WHRegister;

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
		// creates an admin user (has no effect if already exists)
		WHMessage.writeMessage(pw, new WHMessage(WHMessageHeader.REGISTER,
				new WHRegister(0, "admin", "adminPassword",
						"admin@wordhunt.com")));
		WHMessage.readMessage(br);

		// login as admin.
		WHMessage.writeMessage(pw, new WHMessage(WHMessageHeader.AUTH_POST,
				new WHLogin(0, "admin", "adminPassword")));
		WHMessage message = WHMessage.readMessage(br);
		adminToken = ((WHAuthMessage) message.getContent()).getAuthToken();
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

		// we wait till the previous compet is finished.
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// scheduling another competition: ok. (the previous one is done)
		WHMessage.writeMessage(pw, new WHMessage(
				WHMessageHeader.SCHEDULE_COMPET, new WHCompetScheduling(
						adminToken, 1000, 50)));
		message = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.SCHEDULE_COMPET_ACK, message.getHeader());

		// we wait till the end of the previous compet.
		try {
			Thread.sleep(1200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
		assertEquals(WHMessageHeader.AUTH_REQUIRED_403, message.getHeader());
	}
	
	@Test
	public void testJoinCompetition() throws IOException {

		connectAsAdmin();

		// scheduling a competition: ok.
		WHMessage.writeMessage(pw, new WHMessage(
				WHMessageHeader.SCHEDULE_COMPET, new WHCompetScheduling(
						adminToken, 1000, 1500)));
		WHMessage message = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.SCHEDULE_COMPET_ACK, message.getHeader());

		// we wait that the competition starts.
		try {
			Thread.sleep(1100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WHGetGrid gridGet = new WHGetGrid(token, WHGameType.COMPETITION);
		WHMessage query = new WHMessage(WHMessageHeader.GRID_GET_AUTHENTICATED,
				gridGet);
		WHMessage.writeMessage(pw, query);

		WHMessage reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.GRID_REPLY, reply.getHeader());
		assertEquals(WHGridReplyMessage.class, reply.getContent().getClass());
		
		// we wait till the end of the previous compet.
		try {
			Thread.sleep(1800);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testChallengeGrid() throws UnknownHostException,
			IOException {
		
		connectAsAdmin();
		
		// training grid (by default)
		WHGetGrid gridGet = new WHGetGrid(token);
		WHMessage query = new WHMessage(WHMessageHeader.GRID_GET_AUTHENTICATED,
				gridGet);
		WHMessage.writeMessage(pw, query);

		WHMessage reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.GRID_REPLY, reply.getHeader());
		assertEquals(WHGridReplyMessage.class, reply.getContent().getClass());

		// sends back no solutions: should be okay!
		List<String> sol = new ArrayList<>();
		int grididAdmin = ((WHGridReplyMessage) reply.getContent()).getGrid().getID();
		query = new WHMessage(WHMessageHeader.SUBMIT_POST,
				new WHSubmitPostMessage(adminToken, grididAdmin, sol));
		WHMessage.writeMessage(pw, query);

		reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.SUBMIT_VALIDATE, reply.getHeader());
		assertEquals(WHSimpleMessage.class, reply.getContent().getClass());
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// challenge, by default (any grid not played by me - cannot garanty that it's the one above
		gridGet = new WHGetGrid(token, WHGameType.CHALLENGE);
		query = new WHMessage(WHMessageHeader.GRID_GET_AUTHENTICATED, gridGet);
		WHMessage.writeMessage(pw, query);

		reply = WHMessage.readMessage(br);
		System.out.println(reply);
		assertEquals(WHMessageHeader.GRID_REPLY, reply.getHeader());
		assertEquals(WHGridReplyMessage.class, reply.getContent().getClass());
		
		// challenge, by grid id (the one from admin)
		gridGet = new WHGetGrid(token, grididAdmin);
		query = new WHMessage(WHMessageHeader.GRID_GET_AUTHENTICATED, gridGet);
		WHMessage.writeMessage(pw, query);

		reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.GRID_REPLY, reply.getHeader());
		assertEquals(WHGridReplyMessage.class, reply.getContent().getClass());
		WHGridReplyMessage gridReply = (WHGridReplyMessage) reply.getContent();
		assertEquals(grididAdmin, gridReply.getGrid().getGridID());
		
		// challenge, by username (the one from admin)
		gridGet = new WHGetGrid(token, "admin");
		query = new WHMessage(WHMessageHeader.GRID_GET_AUTHENTICATED, gridGet);
		WHMessage.writeMessage(pw, query);

		reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.GRID_REPLY, reply.getHeader());
		assertEquals(WHGridReplyMessage.class, reply.getContent().getClass());
		gridReply = (WHGridReplyMessage) reply.getContent();
		// cannot garanty its the one above, because it may have other grid from admin...
		// assertEquals(grididAdmin, gridReply.getGrid().getGridID());
	}

}
