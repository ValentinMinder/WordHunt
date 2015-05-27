package gridsolver;

/**
 * "GRID AGAINST DICTIONNARY" algorithm of finding ALL acceptable solution in a
 * specific grid.
 * <p>
 * 
 * Algorithm is in the "GRID AGAINST DICTIONNARY" family: we generate possible
 * solutions from the grid with a DFS exploration from all starting Tiles, and
 * test these solutions against the dictionary.
 * <p>
 * 
 * Specific algorithm of V1_2 is the following: <br>
 * - When generating ALL solutions from the Grid, stop the recursion when the
 * start substring is not present anymore ("continueWithSubstring"). <br>
 * - Then, test them 1 by 1 against the dictionary. <br>
 */
public class GridSolverV1_2 extends GridSolverV1 {

	public GridSolverV1_2(TileGrid grid) {
		super(grid);
	}

	protected boolean continueWithSubstring(String substring) {
		// TODO: connexion to DB with appropriate DBAcesse Object.
		return true;
	}

}
