package gridhandler;

import java.util.Random;

import whobjects.Grid;
import whproperties.WHProperties;

/**
 * Created by Karim Ghozlani on 08.05.2015.
 */
public class GridGenerator {

    private static int size;
    private static int nbOfLetters;
    private static int indexOfFirstLetter;
    private static double vowelRatioLowerBound;
    private static double vowelRatioUpperBound;
    private static int maxVowelCount;
    private double[] languageOccurences; // array of letter occurences,
    private WHProperties gridProperties;
    private Random random;
    private static GridGenerator instance = null;


    private GridGenerator() {
        initGenerator();
    }

    public static GridGenerator getInstance() {
        if (instance == null) {
            instance = new GridGenerator();
        }
        return instance;
    }


    private void initGenerator() {
        random = new Random();
        gridProperties = new WHProperties("frenchGrid.properties");
        size = gridProperties.getInteger("SIZE");
        nbOfLetters = gridProperties.getInteger("NBOFLETTERS");
        indexOfFirstLetter = gridProperties.getInteger("ASCIIINDEXOFFIRSTLETTER");
        vowelRatioLowerBound = gridProperties.getDouble("VOWELRATIOLOWERBOUND");
        vowelRatioUpperBound = gridProperties.getDouble("VOWELRATIOUPPERBOUND");
        maxVowelCount = gridProperties.getInteger("MAXVOWELCOUNT");
        languageOccurences = new double[nbOfLetters];

        for (int i = 0; i < nbOfLetters; i++) {
            String currentChar = String.valueOf((char) ('A' + i));
            languageOccurences[i] = gridProperties.getDouble(currentChar.concat("_FR"));
        }

        // TODO: check Karim/David !
//        languageOccurences = new double[LetterOccurences.getNbOfLetters()];
//        languageOccurences = LetterOccurences.getInstance(LetterOccurences.language.FR).getLanguageOccurences();

    }

    private int findCorrespondingLetterIndex(double letter) {
        int error = -1;
        double sum = 0.0;
        double[] probabilitiesSum = new double[languageOccurences.length];
        for (int i = 0; i < languageOccurences.length; i++) {
            probabilitiesSum[i] = languageOccurences[i] + sum;
            sum += languageOccurences[i]; //
        }
        //System.out.println(probabilitiesSum[frenchOccurences.length - 1]);
        for (int i = 0; i < languageOccurences.length; i++) {
            if (letter < probabilitiesSum[i]) {
                return i;
            }
        }
        return error; //if letter >= probabilitiesSum (which must be exactly 100);
    }

    public void printFrenchOccurences() {
        for (int i = 0; i < languageOccurences.length; i++) {
            System.out.println(languageOccurences[i]);
        }

    }
    public static void main(String[] args) {

        Grid grid;
        GridGenerator generator = new GridGenerator();
        System.out.println("Grid with random occurences :");
        System.out.println("--------------------------------------------------------------\n");
        for (int i = 0; i < 10; i++) {
            grid = generator.nextRandomGrid();
            System.out.println(grid.printGrid());
            System.out.println("Nb of vowels: "+grid.getNbOfVowels() +
                    "\nNb of max identical letter : " + grid.getNbOfMaxIdenticalLetter());
        }
        System.out.println();
        System.out.println("Grid with french occurences :");
        System.out.println("--------------------------------------------------------------\n");
        for (int i = 0; i < 100000; i++) {
            grid = generator.nextGrid();
            System.out.println(grid.printGrid());
            System.out.println("Nb of vowels: "+grid.getNbOfVowels() +
                    "\nNb of max identical letter : " + grid.getNbOfMaxIdenticalLetter());
            if(!grid.isPrevalid(vowelRatioLowerBound,vowelRatioUpperBound,maxVowelCount)){
                System.out.println("*********** UNVALID GRID ! *************");
            }
        }
    }
    public Grid nextPrevalidGrid(){
        Grid grid;
        do{
           grid = nextGrid();
        }while(!grid.isPrevalid(vowelRatioLowerBound,vowelRatioUpperBound,maxVowelCount));
        return grid;
    }
    public Grid nextGrid() {
        Grid grid = new Grid(size);
        char[][] content = new char[size][size];
        double letter;
        int letterIndex;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                letter = 100 * random.nextDouble();
                letterIndex = findCorrespondingLetterIndex(letter);
                if (letterIndex == -1) {
                    //System.out.println("Error in fillGridWithLanguageOccurence");
                    letterIndex = 4; //we place "E" in case of error
                }
                content[i][j] = (char) (letterIndex + indexOfFirstLetter);
            }
        }
        grid.setContent(content);
        return grid;
    }

    public Grid nextRandomGrid() {
        Grid grid = new Grid(size);
        char[][] content = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                content[i][j] = (char) (random.nextInt(nbOfLetters) + indexOfFirstLetter);
            }
        }
        grid.setContent(content);
        return grid;
    }
}
