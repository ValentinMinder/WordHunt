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
	
	public List<Tile> getTiles() { return tiles; }
	
	public Tile getTileByIndex(int x, int y) {
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
	
	public List<Tile> getTileByLetter(char letter) {
		List<Tile> result = new ArrayList<Tile>();
		for (Tile t: tiles) {
			if (t.getLetter() == letter) {
				result.add(t);
			}
		}
		return result;
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
