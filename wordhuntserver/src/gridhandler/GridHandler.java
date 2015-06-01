package gridhandler;

import whobjects.Grid;

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