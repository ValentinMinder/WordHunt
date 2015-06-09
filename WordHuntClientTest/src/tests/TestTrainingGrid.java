package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import whobjects.Grid;
import whprotocol.WHGridReplyMessage;
import whprotocol.WHMessage;
import whprotocol.WHProtocol.WHMessageHeader;

/**
 * Test functionalities of a simple grid (without connection).
 */
public class TestTrainingGrid extends TestAbstractWordHunt {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		super.setUp();
	}

	@After
	public void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Retrieves a grid.
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	@Test
	public void testGetGridSimple() throws UnknownHostException, IOException {
		String message = "Hello from client!";

		WHMessage query = new WHMessage(WHMessageHeader.GRID_GET, message);
		WHMessage.writeMessage(pw, query);

		WHMessage reply = WHMessage.readMessage(br);
		assertEquals(WHMessageHeader.GRID_REPLY, reply.getHeader());
		assertEquals(WHGridReplyMessage.class, reply.getContent().getClass());
	}

	/**
	 * Get two random grids. They must be different.
	 * 
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	@Test
	public void testGetGridTwoGridsMustBeDifferent()
			throws UnknownHostException, IOException {
		String message = "Hello from client!";

		WHMessage query = new WHMessage(WHMessageHeader.GRID_GET, message);
		query.writeMessage(pw);
		WHMessage reply = WHMessage.readMessage(br);
		WHGridReplyMessage replied = (WHGridReplyMessage) reply.getContent();
		Grid grid1 = replied.getGrid();

		query = new WHMessage(WHMessageHeader.GRID_GET, message);
		query.writeMessage(pw);
		reply = WHMessage.readMessage(br);
		replied = (WHGridReplyMessage) reply.getContent();
		Grid grid2 = replied.getGrid();
		assertNotSame(grid1, grid2);
		// TODO: id of grid...
//		assertNotSame(grid1.getGridID(), grid2.getGridID());
		assertNotSame(grid1.getContent(), grid2.getContent());
	}

}
