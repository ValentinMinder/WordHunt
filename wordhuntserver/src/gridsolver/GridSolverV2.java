package gridsolver;

import java.util.Arrays;

import whobjects.Grid;

/**
 * Grid Solver, second implementation concept<br>
 * Discontinued after first concept proved efficient enough
 * <br><br>
 * DEPRECATED - code has been commented
 *
 */
public class GridSolverV2 extends GridSolver {

	public GridSolverV2(Grid grid) {
		super(grid);
	}
	
	/*private boolean checkWord(String word) {
		int size = grid.getContent().length;
		boolean[][] used = new boolean[size][size];
		int[] current = grid.getLetterIndex(word.charAt(0), 0);
		if (current[0] != -1) {
			// first letter is in grid
			used[current[0]][current[1]] = true;
			for (int i=1;i<word.length();i++) {
				boolean contains = false;
				for(char c: grid.getNeighbors(current))
					if (c == word.charAt(i)) contains = true;
				if (!contains) return false;
				
			}
		}
		
		return false;
	}
	
	private boolean checkLetter(String partial, int[] current) {
		for(int i=0;i<partial.length();i++) {
			boolean contains = false;
			for (char c: grid.getNeighbors(current))
				if (c == partial.charAt(0)) contains = true;
			if (!contains) return false;
		}
		return false;
	}
	
	public void solve() {
		for(String word: dic) checkWord(word);
	}*/
}
