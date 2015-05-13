package gridsolver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import whobjects.Grid;

public class TileGrid extends Grid {
	private List<Tile> tiles = new LinkedList<Tile>();
	
	public TileGrid(int size) {
		super(size);
	}
	
	@Override
	public void setContent(char[][] content) {
		super.setContent(content);
		
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				tiles.add(new Tile(i,j,content[i][j]));
			}
		}
		
		for (Tile t: tiles) {
			for (Tile n: tiles) {
				if ((t != n)
						&& (Math.abs(t.getX() - n.getX()) <= 1)
						&& (Math.abs(t.getY() - n.getY()) <= 1)) {
					t.addNeighbors(n);
				}
			}
		}
	}
	
	public Tile getTile(int x, int y) {
		if ((x < 0) || (x >= size) || (y < 0) || (y >= size)) {
			throw new IllegalArgumentException("Tile index out of bounds");
		}

		for (Tile t: tiles) {
			if ((t.getX() == x) && (t.getY() == y)) {
				return t;
			}
		}
		
		throw new RuntimeException("??? Your tile doesn't exist?");
	}
	
	public List<Tile> getTiles() { return tiles; }
	
	/** DEPRECATED DO NOT TOUCH >:( */
	public char[] getNeighbors(int i, int j) {
		char[][] content = getContent();
		char[] neighbors;
		int max = content.length - 1;
		if (i == 0) {
			if (j == 0) neighbors = new char[] {content[0][1],
					content[1][0],content[1][1]};
			else if (j == max) neighbors = new char[] {content[1][max],
					content[0][max-1],content[1][max-1]};
			else neighbors = new char[] {content[0][j+1],content[1][j+1],
					content[1][j],content[0][j-1],content[1][j-1]};
		} else if (i == max) {
			if (j == 0) neighbors = new char[] {content[max][1],
					content[max-1][0],content[max-1][1]};
			else if (j == max) neighbors = new char[] {content[max-1][max],
					content[max][max-1],content[max-1][max-1]};
			else neighbors = new char[] {content[max][j+1],content[max-1][j+1],
					content[max-1][j],content[max][j-1],content[max-1][j-1]};
		} else {
			if (j == 0) neighbors = new char[] {content[i+1][0],content[i+1][1],
					content[i][1],content[i-1][0],content[i-1][1]};
			else if (j == max) neighbors = new char[] {content[i+1][max],content[i+1][max-1],
					content[i][max-1],content[i-1][max],content[i-1][max-1]};
			else neighbors = new char[] {content[i-1][j+1],content[i][j+1],content[i+1][j+1],
					content[i-1][j],content[i+1][j],
					content[i-1][j-1],content[i][j-1],content[i+1][j-1]};
		}
		
		return neighbors;
	}
	
	/** DEPRECATED DO NOT TOUCH >:( */
	public char[] getNeighbors(int[] index) {
		return getNeighbors(index[0],index[1]);
	}
	
	/** DEPRECATED DO NOT TOUCH >:( */
	public int[] getLetterIndex(char letter, int start) {
		char[][] content = getContent();
		for (int i = 0; i < content.length; i++) {
			for (int j = 0; j < content.length; j++){
				if ((content[i][j] == letter) && ((i * content.length + j) > start)) return new int[] {i,j};
			}
		}
		return new int[] {-1,-1}; // if letter is not found
	}
	
	class Tile {
		private int x,y;
		private char letter;
		private ArrayList<Tile> neighbors = new ArrayList<Tile>();
		
		private Tile(int i, int j, char c) {
			x = i;
			y = j;
			letter = c;
		}
		
		public char getLetter() { return letter; }
		public int getX() { return x; }
		public int getY() { return y; }
		public int[] getCoords() { return new int[] {x,y}; }
		public ArrayList<Tile> getNeighbors() { return neighbors; }
		
		public void addNeighbors(Tile ... tiles) {
			for (Tile t: tiles) neighbors.add(t);
		}
		
		@Override
		public boolean equals(Object t) {
			return this == t;
		}
	}
}
