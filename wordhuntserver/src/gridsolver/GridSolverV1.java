package gridsolver;

import gridhandler.GridGenerator;
import gridsolver.TileGrid.Tile;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

	private TileGrid grid;

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
		long foundWords = DFSStart(lengthMax);
		System.out.println("k : " + foundWords);
		// TODO: do something with found grids.
	}

	protected boolean validateAnswer(String word) {
		// TODO: connexion to DB with appropriate DBAcesse Object.
		return true;
	}

	protected boolean continueWithSubstring(String substring) {
		// TODO: connexion to DB with appropriate DBAcesse Object.
		return true;
	}

	protected void addSolution(String solution, LinkedList<Tile> pathToSolution) {
		// TODO store found solution and path
		// System.out.println("Solution found:" + solution);
	}

	/**
	 * Start of the DFS exploration for each 1st Tile, with max recursions
	 * (length).
	 * 
	 * @param max
	 * @return
	 */
	private long DFSStart(int max) {
		int result = 0;
		List<Tile> tiles = grid.getTiles();
		for (Tile tile : tiles) {
			LinkedList<Tile> l = new LinkedList<>();
			l.add(tile);
			result += DFSRecursion(max, l);
		}
		return result;
	}

	/**
	 * Recursive DFS step, with max recursions (length).
	 * 
	 * @param max
	 * @param usedTiles
	 * @return
	 */
	private long DFSRecursion(int max, LinkedList<Tile> usedTiles) {
		long result = 0;
		if (usedTiles.size() > max) {
			return 0;
		}

		// finding current word, and testing validation
		String word = findWord(usedTiles);
		if (validateAnswer(word)) {
			result++;
			addSolution(word, usedTiles);
		}

		// testing continuation
		if (continueWithSubstring(word)) {
			// DFS exploration of all neighbors.
			List<Tile> nextNeighbors = usedTiles.peekLast().getNeighbors();
			label: for (Tile tile : nextNeighbors) {
				// discard already used neighbors
				if (usedTiles.contains(tile)) {
					continue label;
				}

				// adding the current neighbor to the stack
				usedTiles.addLast(tile);

				// recursion
				result += DFSRecursion(max, usedTiles);

				// removing the current neighbor from the stack
				usedTiles.pollLast();
			}

		}

		return result;
	}

	/**
	 * Construct a String representation
	 * 
	 * @param usedTiles
	 * @return
	 */
	private String findWord(LinkedList<Tile> usedTiles) {
		StringBuilder sb = new StringBuilder(usedTiles.size());
		Iterator<Tile> it = usedTiles.iterator();
		while (it.hasNext()) {
			sb.append(it.next().getLetter());
		}
		return sb.toString();
	}
}
