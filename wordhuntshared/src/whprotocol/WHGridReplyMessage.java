package whprotocol;

import java.util.Collection;

import whobjects.Grid;

import com.google.gson.JsonObject;

public class WHGridReplyMessage extends WHMessageContent {

	private Grid grid;

	// other stuff to add ? like points mode, score, language, etc, etc...

	public WHGridReplyMessage(int status, Grid grid) {
		super(status);
		this.grid = grid;
	}
	
	public WHGridReplyMessage(Grid grid) {
		super();
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

	public int[] getHashedSolution(){
		return grid.getHashedSolutions();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		JsonObject root = new JsonObject();
		root.add("status", gson.toJsonTree(status));
		root.add("grid", grid.toJsonTree());
		return gson.toJson(root);
	}
}