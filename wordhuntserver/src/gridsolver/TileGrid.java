package gridsolver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import whobjects.Grid;

public class TileGrid extends Grid {
	private List<Tile> tiles = new LinkedList<Tile>();
	private List<String> solutions;
	
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
	
	/**
	 * @return the solutions
	 */
	public List<String> getSolutions() {
		return solutions;
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
	
    /**
     * 3/16 = 18.75%
     * 4/16 = 25%
     * 11/ 16 = 68.75%
     * 12 / 16 = 75%
     * Bounds have to be specified after implementing the solver
     * @return true if the grid is considered prevalid, false if not
     */
    public boolean isPrevalid(double lowerBound, double upperBound, int maxCount){
        double vowelRatio = (double) getNbOfVowels()/(double) (content.length * content.length) ;
        if (vowelRatio < lowerBound || vowelRatio > upperBound){
            return false;
        }
        int greaterCount = 0;
        for(Integer i: getLetterCount().values()){
            if(i > greaterCount){
                greaterCount = i;
            }
        }
        if(greaterCount > maxCount){
            return false;
        }
        return true;
    }

    /**
     * checks if the grid is valid by solving it
     * @see hashedSolutions solutions are hashed and stored
     * @param minNbOfWords desired mininum number of words in the grid
     * @param minWordLength desired minimum length of longest word in the grid
     * @return
     */
    public boolean isValid(int minNbOfWords, int minWordLength){
        TileGrid tileGrid = new TileGrid(size);
        tileGrid.setContent(getContent());
        GridSolverV1 solver = new GridSolverV1(tileGrid);
        solver.solve();
        solutions = (List<String>) solver.getSolutions();


        if(solutions.size() < minNbOfWords){
            return false;
        }
        int maxLength = 0;
        for(String s : solutions){
            if(s.length() > maxLength){
                maxLength = s.length();
            }
        }
        if(maxLength < minWordLength ){
            return false;
        }
        // grid is valid
        
        // Add hashed solutions to the grid
        hashedSolutions = new int [solutions.size()];
        int i = 0;
        for(String solution : solutions ){
            hashedSolutions[i++] = solution.hashCode();
        }
        return true;
    }
	
	/**
	 * Construct a String representation of a list of Tile.
	 * 
	 * @param usedTiles
	 * @return
	 */
	public static String toWord(LinkedList<Tile> usedTiles) {
		StringBuilder sb = new StringBuilder(usedTiles.size());
		Iterator<Tile> it = usedTiles.iterator();
		while (it.hasNext()) {
			sb.append(it.next().getLetter());
		}
		return sb.toString();
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
