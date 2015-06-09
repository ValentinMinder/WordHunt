package gridhandler;

import gridsolver.TileGrid;
import competition.CompetitionManager;

import database.GridStorage;
import database.User;
import whobjects.Grid;
import whprotocol.WHGetGrid;
import whprotocol.WHGridReplyMessage;
import whprotocol.WHMessage;
import whprotocol.WHProtocol.WHMessageHeader;
import whprotocol.WHSimpleMessage;

public class GridHandler {

	private static GridHandler instance;

	private GridGenerator generator;

	public static GridHandler getInstance() {
		if (null == instance) {
			synchronized (GridHandler.class) { // bug fix: synchronized on null object.
				if (null == instance) {
					instance = new GridHandler();
				}
			}
		}
		return instance;
	}

	private GridHandler() {
		generator = GridGenerator.getInstance();
	}

	/**
	 * Returns a new grid
	 * 
	 * @return
	 */
	public Grid getGrid() {
		return generator.nextValidGrid();
	}

	/**
	 * Returns a new grid
	 * 
	 * @return
	 */
	public WHMessage getGrid(WHGetGrid getGridMessage) {
		int userID = User.isValidToken(getGridMessage.getAuthToken());
		if (-1 == userID) {
			return new WHMessage(WHMessageHeader.AUTH_REQUIRED_403, "Authentication required - bad token.");
		}
		Grid grid;
		switch(getGridMessage.getType()) {
		case TRAINING:
			grid = GridHandler.getInstance().getGrid();
			int id = GridStorage.getInstance().storeGrid((TileGrid) grid);
			grid.setGridID(id);
			break;
		case COMPETITION:
			return CompetitionManager.getInstance().getGrid();
		case CHALLENGE:
			if (getGridMessage.getGridID() >= 0) {
				grid = GridStorage.getInstance().getGridByID(getGridMessage.getGridID());
			} else if (getGridMessage.getUsername() != null) {
				grid = GridStorage.getInstance().getGridByUser(getGridMessage.getUsername(), userID);
			} else {
				grid = GridStorage.getInstance().getGridByNotUser(userID);
			}
			break;
		default:
			return new WHMessage(WHMessageHeader.BAD_REQUEST_400, "Type of game not known, sorry.");
		}

		// if not found
		if (null == grid) {
			return new WHMessage(WHMessageHeader.BAD_REQUEST_400, 
					"Your selection is not available (eg the grid number is wrong, " +
					"or their is no games played by the user that you didn't play). " +
					"Or something wrong happened.");
		}

		// else, found, return
		return new WHMessage(WHMessageHeader.GRID_REPLY, new WHGridReplyMessage(grid));
	}

	/**
	 * Returns a grid, from storage (aka already generated).
	 * @param gridID
	 * @return the grid with ID gridID
	 */
	public Grid getGrid(int gridID){
		//TODO
		return new Grid(4);
	}

    public void storeGrid(){
        //Grid grid = getGrid()
        //db.store(grid.getContent(),grid.getID());
    }
}