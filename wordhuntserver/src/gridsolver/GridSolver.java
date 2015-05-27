package gridsolver;

import whobjects.Grid;

/**
 * Abstract top-level class for all algorithm implementation of finding ALL
 * acceptable solution in a specific grid.
 * <p>
 * 
 * Algorithms are divided into two families: <br>
 * - V1: "GRID AGAINST DICTIONNARY" family: we generate possible solutions from
 * the grid with a DFS exploration from all starting Tiles, and test these
 * solutions against the dictionary.<br>
 * - V2: "DICTIONARY AGAINST GRID" family: from the set of the known words
 * (dictionary), we test them successively against the Grid (if there are in or
 * not).
 * <p>
 */
public abstract class GridSolver {

	protected Grid grid;

	public GridSolver(Grid grid) {
		this.grid = grid;
	}

	abstract public void solve();
}

