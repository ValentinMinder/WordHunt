package gridsolver;

import java.util.Arrays;

import whobjects.Grid;

public class GridSolverV2 extends GridSolver {

	public GridSolverV2(Grid grid) {
		super(grid);
	}
	
	// TODO: adapt to TileGrid
	
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
	}*/
	
	/*private boolean checkLetter(String partial, int[] current) {
		for(int i=0;i<partial.length();i++) {
			boolean contains = false;
			for (char c: grid.getNeighbors(current))
				if (c == partial.charAt(0)) contains = true;
			if (!contains) return false;
		}
		return false;
	}*/
	
	public void solve() {
		// TODO: fetch dictionary from db
		// for(String word: dic) checkWord(word);
	}

	public static void main(String[] args) {
		
	}
}
