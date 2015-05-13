package gridsolver;

import gridsolver.TileGrid.Tile;

import java.util.LinkedList;
import java.util.List;

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
		// TODO Auto-generated method stub

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

	private static long findAWay(int max, LinkedList<Tile> coords) {
		long result = 0;
		if (coords.size() == max) {
			return 1;
		} else {
			List<Tile> next = coords.peekLast().getNeighbors();

			label: for (Tile tile : next) {
				if (coords.contains(tile)) {
					continue label;
				}
				coords.addLast(tile);
				result += findAWay(max, coords);
				coords.pollLast();
			}
		}

		return result;

	}
}
