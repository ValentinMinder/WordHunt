package gridhandler;

import whobjects.Grid;

public class GridHandler {

	private static GridHandler instance;

	private GridGenerator generator;

	public static GridHandler getInstance() {
		if (null == instance) {
			synchronized (instance) {
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
	 * Returns a grid, that can be new or from storage (aka already generated).
	 * 
	 * @return
	 */
	public Grid getGrid() {
		Grid grid;
		// for the moment, only returns new generated grid.
		boolean newGrid = true;
		if (newGrid) {
			grid = generator.nextGrid();
		} else {
			grid = null;
		}
		return grid;
	}
}