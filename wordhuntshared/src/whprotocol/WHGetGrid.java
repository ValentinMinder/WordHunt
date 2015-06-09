package whprotocol;

import whprotocol.WHProtocol.WHGameType;
import whprotocol.WHProtocol.WHLangage;

/**
 * Class to get grids (games) in specific types of games (eg to play against
 * someone). The use must be authenticated.
 */
public class WHGetGrid extends WHAuthMessage {

	private WHGameType type = WHGameType.TRAINING; // default
	private WHLangage lang = WHLangage.FRENCH; // default
	private String username = null;
	private int gridID = -1;

	/**
	 * Gets a grid with default values: training mode. As you are authenticated,
	 * will give you a grid that you never played before.
	 */
	public WHGetGrid(String authToken) {
		super(authToken);
	}

	/**
	 * Gets a grid with a given type <br>
	 * Note that challenge mode in this case will give you any already played
	 * grid by anyone (except you), with NO guaranty of user/gridID, only that
	 * it has been played by someone beforehand.
	 */
	public WHGetGrid(String authToken, WHGameType type) {
		super(authToken);
		this.type = type;
	}

	/** gets a grid with challenge type, to play against a given user */
	public WHGetGrid(String authToken, String username) {
		super(authToken);
		this.type = WHGameType.CHALLENGE;
		this.username = username;
	}

	/** gets a grid with challenge type, to play a specific grid */
	public WHGetGrid(String authToken, int gridID) {
		super(authToken);
		this.type = WHGameType.CHALLENGE;
		this.gridID = gridID;
	}

	/**
	 * @return the type
	 */
	public WHGameType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(WHGameType type) {
		this.type = type;
	}

	/**
	 * @return the lang
	 */
	public WHLangage getLang() {
		return lang;
	}

	/**
	 * @param lang the lang to set
	 */
	public void setLang(WHLangage lang) {
		this.lang = lang;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the gridID
	 */
	public int getGridID() {
		return gridID;
	}

	/**
	 * @param gridID the gridID to set
	 */
	public void setGridID(int gridID) {
		this.gridID = gridID;
	}
}