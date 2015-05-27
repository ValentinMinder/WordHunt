package whprotocol;

import java.util.List;

public class WHSubmitPostMessage extends WHAuthMessage {
	private int gridID;
	private List<String> userSolutions;
	
	public WHSubmitPostMessage(int status, String authToken, int gridID,
			List<String> userSolutions) {
		super(status, authToken);
		this.gridID = gridID;
		this.userSolutions = userSolutions;
	}

	public int getGridID() {
		return gridID;
	}

	public void setGridID(int gridID) {
		this.gridID = gridID;
	}

	public List<String> getUserSolutions() {
		return userSolutions;
	}

	public void setUserSolutions(List<String> userSolutions) {
		this.userSolutions = userSolutions;
	}
}
