package whprotocol;

import whobjects.Grid;

import java.util.Collection;

public class WHGridReplyMessage extends WHMessageContent {

	private Grid grid;

	// other stuff to add ? like points mode, score, language, etc, etc...

	public WHGridReplyMessage(int status, Grid grid) {
		super(status);
		this.grid = grid;
	}

	/**
	 * @return the grid
	 */
	public Grid getGrid() {
		return grid;
	}

	/**
	 * @param grid
	 *            the grid to set
	 */
	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public Collection<Integer> getHashedSolution(){
		return grid.getHashedSolutions();
	}
}