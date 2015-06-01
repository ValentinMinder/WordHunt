package whobjects;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Represents the word grid
 * content is set via setContent()
 * isValid() solves a grid and add its hashed solutions using hashCode() to solutionsHashed
 */
public class Grid {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new Gson().toJson(toJsonTree());
	}

	/** manually to GSON to avoid sub-classes (TileGrid) */
	public JsonObject toJsonTree() {
		Gson moteurJson = new Gson();
		JsonObject root = new JsonObject();
		root.add("content", moteurJson.toJsonTree(content));
		root.add("solutionsHashed", moteurJson.toJsonTree(solutionsHashed));
		root.add("gridID", moteurJson.toJsonTree(gridID));
		root.add("size", moteurJson.toJsonTree(size));
		return root;
	}

	protected char[][] content;
    protected ArrayList<Integer> solutionsHashed;
    private int gridID;
    protected int size;

    public Grid() {

    }

    public Grid(int size) {
        content = new char[size][size];
        this.size = size;
    }

    public void setContent(char[][] content) {
        this.content = new char[content.length][content.length];
        for (int i = 0; i < content.length ; i++) {
            for (int j = 0; j < content.length ; j++) {
                this.content[i][j] = content[i][j];
            }
        }
    }

    /**
	 * @return the content
	 */
	public char[][] getContent() {
		return content;
	}

    public int getGridID() {
        return gridID;
    }

    public void setGridID(int gridID) {
        this.gridID = gridID;
    }

    public ArrayList<Integer> getHashedSolutions() {
        return solutionsHashed;
    }

    public int getNbOfVowels() {
        int nbOfVowels = 0;
        char[][] content = getContent();
        for (int i = 0; i < getContent().length; i++) {
            for (int j = 0; j < getContent().length; j++) {
                if (content[i][j] == 'A' ||
                        content[i][j] == 'E' ||
                        content[i][j] == 'I' ||
                        content[i][j] == 'O' ||
                        content[i][j] == 'U') {

                    nbOfVowels++;
                }
            }
        }
        return nbOfVowels;
    }


    public int getNbOfMaxIdenticalLetter() {
        int nbOfMaxIdenticalLetter = 1;
        char[][] content = getContent();
        HashMap<Character,Integer> hashMap = getLetterCount();
        for(Integer i : hashMap.values()){
            if(i > nbOfMaxIdenticalLetter){
                nbOfMaxIdenticalLetter = i;
            }
        }
        return nbOfMaxIdenticalLetter;
    }

    /**
     *
     * @return an hashMap containing the letters (keys) et the nb of its appearances in the grid
     */

    public HashMap<Character,Integer> getLetterCount(){
        HashMap<Character,Integer> hashMap = new HashMap<Character,Integer>();
        for (int i = 0; i < content.length; i++) {
            for (int j = 0; j < content.length; j++) {
                if(hashMap.containsKey(content[i][j])){
                    int nbOfIdenticalLetter = hashMap.get(content[i][j]);
                    nbOfIdenticalLetter++;
                    hashMap.remove(content[i][j]);
                    hashMap.put(content[i][j],nbOfIdenticalLetter);
                }else {
                    hashMap.put(content[i][j],1);
                }
            }
        }
        return hashMap;
    }

    /**
     * @return FALSE
     */
    public boolean isPrevalid(double lowerBound, double upperBound, int maxCount){
        return false;
    }

    /**
     * @return FALSE
     */
    public boolean isValid(int minNbOfWords, int minWordLength){
        return false;
    }

    public String printGrid() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < content.length; i++) {
            sb.append('\n');
            for (int j = 0; j < content.length; j++) {
                sb.append(content[i][j] + " | ");
            }
        }
        sb.append('\n');
        return sb.toString();
    }
}
