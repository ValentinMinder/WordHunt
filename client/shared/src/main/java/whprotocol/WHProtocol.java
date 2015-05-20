package whprotocol;

/**
 * This class aims to represent common data for communication between server and
 * clients.
 * <p>
 * 
 * The whprotocol works this way: <br>
 * - The FIRST line is a "KEYWORD", for example "PING", that defines what is to
 * expect. <br>
 * - The SECOND line is a JSON object, associated with the keyword.<br>
 * - It's followed by TWO return line (\n).
 * <p>
 * 
 * ANY misbehavior should immediately yield a whprotocol error (on server side:
 * send back a "BAD_REQUEST", on client side: a warning message).
 * 
 * @author Valentin MINDER
 */
public class WHProtocol {

	public enum WHMessageHeader {
		NETWORK_ERROR, // in case of IO errors.
		PING, // ping from client
		PING_REPLY, // reply to a ping from server
		GRID_GET, // client ask for a grid
		GRID_REPLY, // server sends a grid back to the client
		ANSWERS_GET, // client asks for the answers
		ANSWERS_REPLY, // server sends back the answers to a grid
		SUBMIT_POST, // clients submits its result to a given grid
		SUBMIT_VALIDATE, // server acknowledges the results given
		CHEATING_WARNING_400, // server detects a cheating and warns the user
		CHEATING_BANNED_400, // server detects a cheating and bans the user
		AUTH_POST, // client tries to authenticate
		AUTH_TOKEN, // server acknowledges the authentication and send the
					// session
		// cookie.
		AUTHENTICATE_BAD_CREDENTIALS, // server denies the authentication
		REGISTER, // client asks for a new account
		REGISTER_ACCOUNT_CREATED_201, // server acknowledges the creation of the
										// account
		SCHEDULE_COMPET, // client admin asks for a competition
		SCHEDULE_COMPET_ACK, // server acknowledges the creation of the
								// competition
		SERVER_ERROR_500, // general error caused by the server
		BAD_REQUEST_400, // general error caused by the client
		AUTH_REQUIRED_403 // access to a private section that requires
							// authentification

	}

	public enum WHLangage {
		FRENCH, // french/francais - only
		ENGLISH, // english/anglais - only
		FRENGLISH // both langages are accepted.
	}

	public enum WHGameType {
		TRAINING, // only for fun
		CHALLENGE, // 1 vs. all in asynchronous way ( previous players)
		COMPETITION // all vs. all in real-time
	}

	public enum WHPointsType {
		WORD, // 1 word = 1 point
		LENGTH, // 1 word of x letter = x points
		LENGTH_SQUARE, // 1 word of x letter = (x-2)(x-2) points
		BONUS // special regime (certain word have a privilege)
	}
}