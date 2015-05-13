package gridsolver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import whobjects.Grid;

public class GridSolverV1 extends GridSolver {

	public GridSolverV1(Grid grid) {
		super(grid);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private static long findAWay(int max) {
		long result = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				LinkedList<Coordinate> l = new LinkedList<>();
				l.add(new Coordinate(i, j));
				result += findAWay(max, l);
			}
		}
		return result;
	}

	private static long findAWay(int max, LinkedList<Coordinate> coords) {
		long result = 0;
		if (coords.size() == max) {
			return 1;
		} else {
			List<Coordinate> next = coords.get(coords.size() - 1)
					.neighborsWihtout(coords);

			for (Coordinate pair : next) {
				coords.addFirst(pair);
				result += findAWay(max, coords);
				coords.remove(pair);
				// pairs.p
			}
		}

		return result;

	}
}

class Coordinate {
	static int xmax = 4;
	static int ymax = 4;
	private int xCoord;
	private int yCoord;

	Coordinate(int x, int y) {
		this.xCoord = x;
		this.yCoord = y;
	}

	List<Coordinate> neighbors() {
		return neighborsWihtout(new ArrayList<Coordinate>());
	}

	List<Coordinate> neighborsWihtout(List<Coordinate> coords) {
		List<Coordinate> list = new ArrayList<Coordinate>();
		for (int nextX = Math.max(0, xCoord - 1); nextX < Math.min(xmax,
				xCoord + 2); nextX++) {
			label: for (int nextY = Math.max(0, yCoord - 1); nextY < Math.min(
					ymax, yCoord + 2); nextY++) {

				// ignore preceeding
				for (Coordinate coord : coords) {
					if (coord.xCoord == nextX && coord.yCoord == nextY)
						continue label;
				}

				// ignore self
				if (xCoord == nextX && yCoord == nextY) {
					continue label;
				}
				list.add(new Coordinate(nextX, nextY));
			}
		}
		return list;
	}
}
