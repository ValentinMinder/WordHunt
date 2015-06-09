package tests;

import org.junit.After;
import org.junit.Before;

/**
 * Abstract class that creates data for a random user (but do not register it).
 */
public abstract class TestAbstractRegister extends TestAbstractWordHunt {

	protected String email;
	protected String name;
	protected String password;

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();
		email = "user" + rand.nextInt() + "@example.com";
		name = "user" + rand.nextInt();
		password = "myPassword" + rand.nextInt();
	}

	@After
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
