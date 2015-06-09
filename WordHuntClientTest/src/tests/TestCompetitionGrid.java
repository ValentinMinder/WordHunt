package tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.Ignore;
import org.junit.Test;

import whprotocol.WHGetGrid;
import whprotocol.WHGridReplyMessage;
import whprotocol.WHMessage;
import whprotocol.WHProtocol.WHGameType;
import whprotocol.WHProtocol.WHMessageHeader;

public class TestCompetitionGrid extends TestAbstractAuthenticated {
	
	@Test
	public void testTrainingGrid() throws UnknownHostException,
			IOException {
		
		// training grid (by default)
		WHGetGrid gridGet = new WHGetGrid(token);
		WHMessage query = new WHMessage(WHMessageHeader.GRID_GET_AUTHENTICATED, gridGet);
		WHMessage.writeMessage(pw, query);

		WHMessage reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.GRID_REPLY, reply.getHeader());
		assertEquals(WHGridReplyMessage.class, reply.getContent().getClass());

		// training grid (explicit)
		gridGet = new WHGetGrid(token, WHGameType.TRAINING);
		query = new WHMessage(WHMessageHeader.GRID_GET_AUTHENTICATED, gridGet);
		WHMessage.writeMessage(pw, query);

		reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.GRID_REPLY, reply.getHeader());
		assertEquals(WHGridReplyMessage.class, reply.getContent().getClass());
	}
	
	@Test
	public void testCompetGrid() throws UnknownHostException,
			IOException {
		// competition . should fail, as no competition was launched !
		WHGetGrid gridGet = new WHGetGrid(token, WHGameType.COMPETITION);
		WHMessage query = new WHMessage(WHMessageHeader.GRID_GET_AUTHENTICATED, gridGet);
		WHMessage.writeMessage(pw, query);

		WHMessage reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.BAD_REQUEST_400, reply.getHeader());
	}
	
	@Test
	@Ignore
	public void testChallengeGrid() throws UnknownHostException,
			IOException {
		// challenge, by default (any grid not played by me)
		WHGetGrid gridGet = new WHGetGrid(token, WHGameType.CHALLENGE);
		WHMessage query = new WHMessage(WHMessageHeader.GRID_GET_AUTHENTICATED, gridGet);
		WHMessage.writeMessage(pw, query);

		WHMessage reply = WHMessage.readMessage(br);
		System.out.println(reply);
		assertEquals(WHMessageHeader.GRID_REPLY, reply.getHeader());
		assertEquals(WHGridReplyMessage.class, reply.getContent().getClass());
		
		// challenge, by grid id
		gridGet = new WHGetGrid(token, 1);
		query = new WHMessage(WHMessageHeader.GRID_GET_AUTHENTICATED, gridGet);
		WHMessage.writeMessage(pw, query);

		reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.GRID_REPLY, reply.getHeader());
		assertEquals(WHGridReplyMessage.class, reply.getContent().getClass());
		
		// challenge, by username
		gridGet = new WHGetGrid(token, "admin");
		query = new WHMessage(WHMessageHeader.GRID_GET_AUTHENTICATED, gridGet);
		WHMessage.writeMessage(pw, query);

		reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.GRID_REPLY, reply.getHeader());
		assertEquals(WHGridReplyMessage.class, reply.getContent().getClass());
	}
}
