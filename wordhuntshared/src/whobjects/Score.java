package whobjects;

import static java.lang.Math.ceil;

import java.util.Collection;
import java.util.LinkedList;

import whproperties.WHProperties;
import whprotocol.WHProtocol;

/**
 * Created by David on 09.05.2015.
 */
public class Score {

    private int score;
    private double[] languageOccurences;
    private static int nbOfLetters;
    private WHProperties gridProperties;
    private static Score instance;
    
    public static Score getInstance () {
    	if (instance == null) {
    		synchronized (Score.class) {
    			if (instance == null) {
    				instance = new Score();
    			}
    		}
    	}
    	return instance;
    }
    private Score(){
        gridProperties = new WHProperties("wordhuntserver/frenchGrid.properties");
        nbOfLetters = gridProperties.getInteger("NBOFLETTERS");

        languageOccurences = new double[nbOfLetters];

        for (int i = 0; i < nbOfLetters; i++) {
            String currentChar = String.valueOf((char) ('A' + i));
            languageOccurences[i] = gridProperties.getDouble(currentChar.concat("_FR"));
        }
    }

    public int getScore(String word, WHProtocol.WHPointsType pointsType){
        score = 0;
        switch(pointsType) {
            case WORD:
                score = 1;
                break;
            case LENGTH:
                score = word.length();
                break;
            case LENGTH_SQUARE:
                score = (word.length()-2) * (word.length()-2);
                break;
            case LENGTH_LETTERFREQ:
                languageOccurences = LetterOccurences.getInstance(LetterOccurences.language.FR).getLanguageOccurences();
                score = word.length()*100;
                for (int i = 0; i < word.length(); i++) { // More points if the letter occurs less in language
                    char c = word.charAt(i);
                    double letterWeight = 1 / languageOccurences[c - 'A'];
                    if(letterWeight > 1) {
//                    System.out.println("letterWeight: " + letterWeight);
                        score += ceil((letterWeight/4))*100;
                    }
                }
                break;
            case BONUS:

            default :
                score = 0;


        }

        return score;
    }


    public int getScore(Collection<String> words, WHProtocol.WHPointsType pointsType){
        score = 0;
        for(String s : words){
            score += getScore(s, pointsType);
        }
        return score;
    }


    public static void main(String[] args) {
        Score test = new Score();
        System.out.println("Score LENGTH: TATAMIS :" + test.getScore("TATAMIS", WHProtocol.WHPointsType.LENGTH));
        System.out.println("Score LENGTH: ZYGOTE :" + test.getScore("ZYGOTE", WHProtocol.WHPointsType.LENGTH));

        System.out.println("Score LENGTH_SQUARE: TATAMIS :" + test.getScore("TATAMIS", WHProtocol.WHPointsType.LENGTH_SQUARE));
        System.out.println("Score LENGTH_SQUARE: ZYGOTE :" + test.getScore("ZYGOTE", WHProtocol.WHPointsType.LENGTH_SQUARE));
        System.out.println("Score LENGTH_SQUARE: ANTICONSTITUTIONNELLEMENT :" + test.getScore("ANTICONSTITUTIONNELLEMENT", WHProtocol.WHPointsType.LENGTH_SQUARE));

        System.out.println("Score LENGTH_LETTERFREQ: TATAMIS :" + test.getScore("TATAMIS", WHProtocol.WHPointsType.LENGTH_LETTERFREQ));
        System.out.println("Score LENGTH_LETTERFREQ: ZYGOTE :" + test.getScore("ZYGOTE", WHProtocol.WHPointsType.LENGTH_LETTERFREQ));



        Collection<String> words = new LinkedList<String>();
        words.add("TATAMIS");
        words.add("ZYGOTE");
        words.add("THERMOMETRE");

        System.out.println("Score Collection WORD: " + test.getScore(words, WHProtocol.WHPointsType.WORD));

        System.out.println("Score Collection LENGTH_LETTERFREQ: " + test.getScore(words, WHProtocol.WHPointsType.LENGTH_LETTERFREQ));

    }




}
