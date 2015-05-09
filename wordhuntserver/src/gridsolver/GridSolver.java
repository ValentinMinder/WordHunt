package gridsolver;

import java.util.ArrayList;
import java.util.List;

import whobjects.Grid;

public class GridSolver {

	private Grid grid;

	public GridSolver(Grid grid) {
		this.grid = grid;
	}

	public void solve() {
		// get the words with the existing letters.
		Provider.getWords(findLetters());
	}

	// find the composing letters...
	private String findLetters() {
		String result = "";
		char[][] content = grid.getContent();
		for (int i = 0; i < content.length; i++) {
			for (int j = 0; j < content.length; j++) {
				// TODO: avoid doubles
				result += content[i][j];
			}
		}
		return result;
	}

}

class Provider {
	static List<String> getWords() {
		List<String> result = new ArrayList<>();
		result.add("Hello");
		result.add("World");
		return result;
	}

	static List<String> getWords(String precond) {
		List<String> result = new ArrayList<>();
		result.add("Hello");
		result.add("World");
		return result;
	}
}
