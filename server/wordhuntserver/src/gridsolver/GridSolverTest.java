package gridsolver;

import gridhandler.GridGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * TESTS ONLY FOR ALGO OPTIMISATION <BR>
 * PLEASE REMOVE FOR PRODUCTION !
 * <P>
 * 
 * PLEASE DONT CODE HERE FOR FINAL USAGE!!!!!
 * 
 */

public class GridSolverTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		char[][] content = GridGenerator.getInstance().nextGrid().getContent();

		int size3 = 0;
		int size2 = 0;
		for (int i1 = 0; i1 < content.length; i1++) {
			for (int j1 = 0; j1 < content.length; j1++) {
				// first level
				for (int i2 = Math.max(0, i1 - 1); i2 < Math.min(
						content.length, i1 + 2); i2++) {
					for (int j2 = Math.max(0, j1 - 1); j2 < Math.min(
							content.length, j1 + 2); j2++) {
						if (i1 == i2 && j1 == j2) {
							continue;
						}
						// second level.
						size2++;

						for (int i3 = Math.max(0, i2 - 1); i3 < Math.min(
								content.length, i2 + 2); i3++) {
							for (int j3 = Math.max(0, j2 - 1); j3 < Math.min(
									content.length, j2 + 2); j3++) {
								if (i1 == i3 && j1 == j3) {
									continue;
								}
								if (i2 == i3 && j2 == j3) {
									continue;
								}
								// third level.
								size3++;
							}
						}
					}
				}

			}
		}
		System.out.println(size2);
		System.out.println(size3);

		// size 2 = 4*3 + 8*5+4 *8 = 84 pas 64 ?!?
		System.out
				.println("length / possibilities for 4x4 grid (withotu reusage)");
		long sum = 0;
		for (int i = 1; i < 17; i++) {
			long current = findAWay(i);
			sum += current;
			// TODO: COMPARER AU NOMBRE DE MOTS EXISTANTS DE CE # DE LETTRES.
			System.out.println(i + " - " + current + " - " + sum);
		}

	}

	private static long findAWay(int max, Pair... pairs) {
		long result = 0;
		if (pairs.length == max) {
			return 1;
		}

		if (pairs.length == 0) {
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					result += findAWay(max, new Pair(i, j));
				}
			}

		} else {
			List<Pair> next = pairs[pairs.length - 1].neighborsWihtout(pairs);
			Pair[] nexts = new Pair[pairs.length + 1];
			for (int i = 0; i < pairs.length; i++) {
				nexts[i] = pairs[i];
			}
			for (Pair pair : next) {
				nexts[pairs.length] = pair;
				result += findAWay(max, nexts);
			}
		}

		return result;

	}

}

class Pair {
	static int xmax = 4;
	static int ymax = 4;
	int x, y;

	Pair(int x, int y) {
		this.x = x;
		this.y = y;
	}

	List<Pair> neighbors() {
		return neighborsWihtout();
	}

	List<Pair> neighborsWihtout(Pair... pairs) {
		List<Pair> list = new ArrayList<Pair>();
		for (int i2 = Math.max(0, x - 1); i2 < Math.min(xmax, x + 2); i2++) {
			label: for (int j2 = Math.max(0, y - 1); j2 < Math.min(ymax, y + 2); j2++) {
				for (Pair pair : pairs) {
					if (pair.x == i2 && pair.y == j2)
						continue label;
				}
				list.add(new Pair(i2, j2));
			}
		}
		return list;
	}
}
