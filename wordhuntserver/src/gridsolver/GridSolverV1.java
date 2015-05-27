package gridsolver;

import gridhandler.GridGenerator;
import gridsolver.TileGrid.Tile;

import java.util.LinkedList;
import java.util.logging.Logger;

/**
 * Simple "GRID AGAINST DICTIONNARY" algorithm of finding ALL acceptable
 * solution in a specific grid.
 * <p>
 * 
 * Algorithm is in the "GRID AGAINST DICTIONNARY" family: we generate possible
 * solutions from the grid with a DFS exploration from all starting Tiles, and
 * test these solutions against the dictionary.
 * <p>
 * 
 * Specific algorithm of V1 is the following: <br>
 * - Generate ALL solutions from the Grid. <br>
 * - Then, test them 1 by 1 against the dictionary. <br>
 * 
 * <p>
 * PLEASE NOTE THAT THIS IS THE SIMPLIEST ALGORITHM POSSIBLE, BUT ALSO THE LESS
 * SMART ! ITS COMPLEXITY IS HIGH-ORDER EXPONENTIAL, AND ITS ONLY MEANT AS A
 * PROOF OF CONCEPT. PLEASE HAVE A LOOK AT OPTIMIZED VERSION IN OTHER
 * IMPLEMENTATION.
 */
public class GridSolverV1 extends GridSolver {

	protected TileGrid grid;
	private Logger logger = Logger.getLogger(GridSolverV1.class.getName());

	public GridSolverV1(TileGrid grid) {
		super(grid);
		this.grid = grid;
	}

	/**
	 * Test program for GridSolverV1.
	 */
	public static void main(String[] args) {
		// TODO: disaster, please have generator return TileGrid !
		TileGrid grid = new TileGrid(4);
		grid.setContent(GridGenerator.getInstance().nextGrid().getContent());

		// test a resolution
		GridSolverV1 solver = new GridSolverV1(grid);
		System.out.println(grid.printGrid());
		solver.solve();
		System.out.println(grid.printGrid());
	}

	@Override
	public void solve() {
		int lengthMax = 16;
		logger.info("Start solving of GridSolverV1");
		long time = System.currentTimeMillis();
		long foundWords = DFSExploration.DFSStart(this, lengthMax, true);
		time = System.currentTimeMillis() - time;
		logger.info("End solving of GridSolverV1 in : " + time + " ms");
		System.out.println("k : " + foundWords);
		// TODO: do something with found grids.
	}

	protected boolean validateAnswer(String word) {
		// TODO: connexion to DB with appropriate DBAcesse Object.
		return true;
	}

	protected boolean continueWithSubstring(String substring) {
		// in this version, return TRUE always.
		return true;
	}

	protected void addSolution(String solution, LinkedList<Tile> pathToSolution) {
		// TODO store found solution and path
		// System.out.println("Solution found:" + solution);
	}
}
