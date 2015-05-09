package whobjects;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 09.05.2015.
 */
public class Score {

    private int score;
    private double[] languageOccurences;

    private Score() {
        languageOccurences = new double[LetterOccurences.getNbOfLetters()];
    }

    public int getScore(String word) {
        score = 0;
        languageOccurences = LetterOccurences.getInstance(LetterOccurences.language.FR).getLanguageOccurences();
        score += (word.length() - 2) * 100; //Score start at 100 for a 3 letters word
        for (int i = 0; i < word.length(); i++) { // More points if the letter occurs less in language
            char c = word.charAt(i);
            double letterWeight = 1 / languageOccurences[c - 'A'];
//            System.out.println("letterWeight: " + letterWeight);
            score += (letterWeight / 4) * 100;
        }

        return score;
    }

    public int getScore(List<String> words) {
        score = 0;
        languageOccurences = LetterOccurences.getInstance(LetterOccurences.language.FR).getLanguageOccurences();
        for (String word : words) {
            score += (word.length() - 2) * 100; //Score start at 100 for a 3 letters word
            for (int i = 0; i < word.length(); i++) { // More points if the letter occurs less in language
                char c = word.charAt(i);
                double letterWeight = 1 / languageOccurences[c - 'A'];
//            System.out.println("letterWeight: " + letterWeight);
                score += (letterWeight / 4) * 100;
            }
        }

        return score;
    }


    public static void main(String[] args) {
        Score test = new Score();
        System.out.printf("Score : Tatamis :" + test.getScore("TATAMIS"));
        System.out.printf("Score : ZYGOTE :" + test.getScore("ZYGOTE"));
        List<String> words = new ArrayList<String>();
        words.add("TATAMIS");
        words.add("ZYGOTE");
        System.out.printf("Score : Total :" +  test.getScore(words));
    }


}
