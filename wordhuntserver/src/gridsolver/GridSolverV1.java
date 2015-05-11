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
	
	private static long findAWay(int max, LinkedList<Pair> pairs) {
		long result = 0;
		if (pairs.size() == max) {
			return 1;
		}

		if (pairs.size() == 0) {
//			for (int i = 0; i < 4; i++) {
//				for (int j = 0; j < 4; j++) {
//					result += findAWay(max, new Pair(i, j));
//				}
//			}

		} else {
			List<Pair> next = pairs.get(pairs.size() - 1).neighborsWihtout(pairs);
			
			for (Pair pair : next) {
				pairs.addFirst(pair);
				result += findAWay(max, pairs);
				pairs.remove(pair);
//				pairs.p
			}
		}

		return result;

	}

}

class Pair {
	static int xmax = 4;
	static int ymax = 4;
	private int xCoord;
	private int yCoord;

	Pair(int x, int y) {
		this.xCoord = x;
		this.yCoord = y;
	}

	List<Pair> neighbors() {
		return neighborsWihtout(new ArrayList<Pair>());
	}

	List<Pair> neighborsWihtout(List<Pair> pairs) {
		List<Pair> list = new ArrayList<Pair>();
		for (int nextX = Math.max(0, xCoord - 1); nextX < Math.min(xmax,
				xCoord + 2); nextX++) {
			label: for (int nextY = Math.max(0, yCoord - 1); nextY < Math.min(
					ymax, yCoord + 2); nextY++) {

				// ignore preceeding
				for (Pair pair : pairs) {
					if (pair.xCoord == nextX && pair.yCoord == nextY)
						continue label;
				}

				// ignore self
				if (xCoord == nextX && yCoord == nextY) {
					continue label;
				}
				list.add(new Pair(nextX, nextY));
			}
		}
		return list;
	}
}
