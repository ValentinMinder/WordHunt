package whobjects;

import whproperties.WHProperties;

import java.util.HashMap;

/**
 * Created by Karim Ghozlani on 06.05.2015.
 */
public class Grid {

    private char[][] content;
    public Grid() {

    }

    public Grid(int size) {
        content = new char[size][size];
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
     * 3/16 = 18.75%
     * 4/16 = 25%
     * 11/ 16 = 68.75%
     * 12 / 16 = 75%
     * Bounds have to be specified after implementing the solver
     * @return true if the grid is considered valid, false if not
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
