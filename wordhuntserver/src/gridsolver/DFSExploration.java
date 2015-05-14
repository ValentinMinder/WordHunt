package gridsolver;

import gridsolver.TileGrid.Tile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DFSExploration implements Runnable {

	private GridSolverV1 solverInstance;
	private int depthMax;
	private LinkedList<Tile> initList;

	public DFSExploration(GridSolverV1 solverInstance, int depthMax,
			LinkedList<Tile> initList) {
		this.solverInstance = solverInstance;
		this.depthMax = depthMax;
		this.initList = initList;
	}

	/**
	 * Start of the DFS exploration for each 1st Tile, with max recursions
	 * (length).
	 * 
	 * @param solver
	 *            the solver to use (must be V1 or sub-classes, which provides
	 *            test and continuation condition)
	 * @param depthMax
	 *            the maximal depth to go
	 * @param allowMutliThread
	 *            if multi-thread must be used.
	 * @return the number of Tile visited (only in non-threading)
	 */
	public static long DFSStart(GridSolverV1 solver, int depthMax,
			boolean allowMutliThread) {
		int result = 0;
		List<Tile> tiles = solver.grid.getTiles();
		List<Thread> threads = new ArrayList<Thread>();
		for (Tile tile : tiles) {
			LinkedList<Tile> list = new LinkedList<>();
			list.add(tile);
			DFSExploration dfs = new DFSExploration(solver, depthMax, list);
			if (allowMutliThread) {
				threads.add(new Thread(dfs));
			} else {
				result += dfs.DFSRecursion(depthMax, list);
			}
		}

		if (allowMutliThread) {
			for (Thread thread : threads) {
				thread.start();
			}
			for (Thread thread : threads) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	@Override
	public void run() {
		DFSRecursion(depthMax, initList);
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
		String word = TileGrid.toWord(usedTiles);
		if (solverInstance.validateAnswer(word)) {
			result++;
			solverInstance.addSolution(word, usedTiles);
		}

		// testing continuation
		if (solverInstance.continueWithSubstring(word)) {
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
}