package whobjects;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by David on 09.05.2015.
 * TODO: check pertinence of this class !!!
 */
public class LetterOccurences {

    private double[] languageOccurences;

    private static final int nbOfLetters = 26;
    public static enum language {FR, EN};
    private String OccurenceFile ; //here we set the languages
    private static LetterOccurences instance;

    private LetterOccurences(language lang){
        switch (lang){
            case EN:
                OccurenceFile = "wordhuntshared/englishOccurencesToParse.txt";
            default:
                OccurenceFile = "wordhuntshared/frenchOccurencesToParse.txt";
        }

    }

    public static LetterOccurences getInstance(language lang){
        if(instance == null){
            instance = new LetterOccurences(lang);
        }
        return instance;
    }

    public double[] getLanguageOccurences(){
        languageOccurences = new double[nbOfLetters];
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(OccurenceFile)));
            System.out.println();
            for (int i = 0; i < nbOfLetters; i++) {
                languageOccurences[i] = Double.valueOf(br.readLine());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return languageOccurences;
    }
    public static int getNbOfLetters() {
        return nbOfLetters;
    }

}
