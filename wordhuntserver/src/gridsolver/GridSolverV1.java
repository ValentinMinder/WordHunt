package gridsolver;

import gridhandler.GridGenerator;
import gridsolver.TileGrid.Tile;

import java.util.LinkedList;
import java.util.List;

import whobjects.Grid;

public class GridSolverV1 extends GridSolver {

	private TileGrid grid;

	public GridSolverV1(TileGrid grid) {
		super(grid);
		this.grid = grid;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long sum = 0;
		// TODO: disaster, please have generator return TileGrid !
		TileGrid grid = new TileGrid(4);
		grid.setContent(GridGenerator.getInstance().nextGrid().getContent());
		GridSolverV1 solver = new GridSolverV1(grid);
		for (int i = 1; i < 17; i++) {

			long current = solver.findAWay(i);
			sum += current;
			// TODO: COMPARER AU NOMBRE DE MOTS EXISTANTS DE CE # DE LETTRES.
			System.out.println(i + " - " + current + " - " + sum);
		}
	}

	private long findAWay(int max) {
		int result = 0;
		List<Tile> tiles = grid.getTiles();
		for (Tile tile : tiles) {
			LinkedList<Tile> l = new LinkedList<>();
			l.add(tile);
			result += findAWay(max, l);
		}
		return result;
	}

	private static long findAWay(int max, LinkedList<Tile> usedTiles) {
		long result = 0;
		if (usedTiles.size() == max) {
			return 1;
		} else {
			List<Tile> nextNeighbors = usedTiles.peekLast().getNeighbors();

			label: for (Tile tile : nextNeighbors) {
				// discard used neighbors
				if (usedTiles.contains(tile)) {
					continue label;
				}
				usedTiles.addLast(tile);
				result += findAWay(max, usedTiles);
				usedTiles.pollLast();
			}
		}

		return result;

	}
}
