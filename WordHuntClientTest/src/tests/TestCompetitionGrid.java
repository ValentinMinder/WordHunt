package tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import whprotocol.WHGetGrid;
import whprotocol.WHGridReplyMessage;
import whprotocol.WHMessage;
import whprotocol.WHProtocol.WHGameType;
import whprotocol.WHProtocol.WHMessageHeader;
import whprotocol.WHSimpleMessage;
import whprotocol.WHSubmitPostMessage;

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
	public void testTrainingGridSubmitSolutions() throws UnknownHostException,
			IOException {
		
		// training grid (by default)
		WHGetGrid gridGet = new WHGetGrid(token);
		WHMessage query = new WHMessage(WHMessageHeader.GRID_GET_AUTHENTICATED, gridGet);
		WHMessage.writeMessage(pw, query);

		WHMessage reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.GRID_REPLY, reply.getHeader());
		assertEquals(WHGridReplyMessage.class, reply.getContent().getClass());

		// sends back no solutions: should be okay!
		List<String> sol = new ArrayList<>();
		int id = ((WHGridReplyMessage) reply.getContent()).getGrid().getID();
		query = new WHMessage(WHMessageHeader.SUBMIT_POST, new WHSubmitPostMessage(token, id, sol));
		WHMessage.writeMessage(pw, query);
		
		reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.SUBMIT_VALIDATE, reply.getHeader());
		assertEquals(WHSimpleMessage.class, reply.getContent().getClass());
		
		// sends back impossible solutions: should say no!
		sol.add("12345678901234567");
		query = new WHMessage(WHMessageHeader.SUBMIT_POST,
				new WHSubmitPostMessage(token, id, sol));
		WHMessage.writeMessage(pw, query);

		reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.CHEATING_WARNING_400, reply.getHeader());
		assertEquals(WHSimpleMessage.class, reply.getContent().getClass());
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
}
