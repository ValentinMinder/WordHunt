package gridsolver;

import gridhandler.GridGenerator;
import gridsolver.TileGrid.Tile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
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
 * Specific algorithm of V1_2 is the following: <br>
 * - When generating ALL solutions from the Grid, stop the recursion when the
 * start substring is not present anymore ("continueWithSubstring"). <br>
 * - Then, test them 1 by 1 against the dictionary. <br>
 * 
 */
public class GridSolverV1 extends GridSolver {

	protected TileGrid grid;
	private Logger logger = Logger.getLogger(GridSolverV1.class.getName());
	protected static WordProvider db;
	private static int[] nbOfWords;

	public GridSolverV1(TileGrid grid) {
		super(grid);
		this.grid = grid;
	}

	/**
	 * Test program for GridSolverV1.
	 */
	public static void main(String[] args) {
		int nbOfGrids = 100;
		nbOfWords = new int[nbOfGrids];
		db = WordProvider.getInstance();
		// TODO: disaster, please have generator return TileGrid !
		TileGrid grid = new TileGrid(4);
		GridGenerator gen = GridGenerator.getInstance();
		grid.setContent(gen.nextValidGrid().getContent());

		// test a resolution
		GridSolverV1 solver = new GridSolverV1(grid);
		System.out.println(grid.printGrid());
		solver.solve();

		long time = System.currentTimeMillis();
		int nbOfGridsThatContainsLessThen50 = 0;
		int nbOfGridsThatContainsLessThen100 = 0;
        int percent = 0;
		for (int i = 0; i < nbOfGrids; i++) {
            if(i > 0 && i % (nbOfGrids/100) == 0){
                System.out.print(++percent +"% ");
            }
			grid = new TileGrid(4);
			grid.setContent(gen.nextValidGrid().getContent());
			solver = new GridSolverV1(grid);
			solver.solve();
			nbOfWords[i] = solver.solutions.size();
			System.out.println(grid.printGrid());
			System.out.println(solver.solutions);
			System.out.println("Nb of words per grid :"+solver.solutions.size());
			if(solver.solutions.size() < 50){
				nbOfGridsThatContainsLessThen50 ++;
				nbOfGridsThatContainsLessThen100++;
				System.out.println("<50 !");
			}else if(solver.solutions.size() < 100){
				nbOfGridsThatContainsLessThen100++;
				System.out.println("<100 !");
			}

		}
		System.out.println("Found: " + solver.solutions.size() + " words.");
		System.out.println(grid.printGrid());
		System.out.println(solver.solutions);

		time = System.currentTimeMillis() - time;
        System.out.flush();
		System.err.println("End solving of " + nbOfGrids+" grids in : " + time + " ms");

		System.out.println("Nb of words per grid :");
		for (int i = 0; i < nbOfGrids; i++) {
//			System.out.println(nbOfWords[i]);
		}
		System.out.println("Nb of grids that contains less then 50 : "+nbOfGridsThatContainsLessThen50);
		System.out.println("Nb of grids that contains less then 100 : "+nbOfGridsThatContainsLessThen100);

        System.out.println("Nb of thrown grids because of Prevalidation : "+gen.getNbOfThrowngridBecauseOfPrevalidation());
        System.out.println("Nb of thrown grids because of Validation : "+gen.getNbOfThrowngridBecauseOfValidation());
        System.out.println("Total nb of thrown grids  : "+gen.getNbOfThrowngrid());
    }

	@Override
	public void solve() {
		int lengthMax = 10;
		// logger.info("Start solving of GridSolverV1");
		// long time = System.currentTimeMillis();
		long foundWords = DFSExploration.DFSStart(this, lengthMax, false);
		// time = System.currentTimeMillis() - time;
		// System.out.println(solutions);

		// logger.info("End solving of GridSolverV1 in : " + time + " ms");
		// System.out.println("k : " + foundWords);
		// System.out.println("Found: " + sol + " words.");
		// TODO: do something with found grids.
	}

	protected boolean validateAnswer(String word) {
		// TODO: connexion to DB with appropriate DBAcesse Object.
		return db.isWord(word);
	}

	protected boolean continueWithSubstring(String substring) {
		// in this version, return TRUE always.
		return db.continueWithSubstring(substring);
	}

	private int sol = 0;
	private List<String> solutions = new ArrayList<>(500);

	public Collection<String> getSolutions() {
		return solutions;
	}

	protected void addSolution(String solution, LinkedList<Tile> pathToSolution) {
		// TODO store found solution and path
		sol++;
		solutions.add(solution);
		// System.out.println("Solution found:" + solution);
	}
}
