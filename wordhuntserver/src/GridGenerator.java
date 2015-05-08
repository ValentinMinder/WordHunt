import java.io.*;
import java.util.Random;

/**
 * Created by Karim Ghozlani on 08.05.2015.
 */
public class GridGenerator {

    private static int size = 4;
    private static final int nbOfLetters = 26;
    private static final int indexOfFirstLetter = 65;
    private double[] languageOccurences; // array of letter occurences,
    private String frenchOccurenceFile = "frenchOccurencesToParse.txt"; //here we set the language
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
        languageOccurences = new double[nbOfLetters];
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(frenchOccurenceFile)));
            for (int i = 0; i < nbOfLetters; i++) {
                languageOccurences[i] = Double.valueOf(br.readLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        }
        System.out.println();
        System.out.println("Grid with french occurences :");
        System.out.println("--------------------------------------------------------------\n");
        for (int i = 0; i < 10; i++) {
            grid = generator.nextGrid();
            System.out.println(grid.printGrid());
        }
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
                    System.out.println("Error in fillGridWithLanguageOccurence");
                    System.exit(-1);
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
