package ch.heigvd.wordhunt.activities.Game;

/**
* Created by paulnta on 13.06.15.
*/
public interface IGameManager {

    public void onGameFinish(int score);

    int onValidateWord(String word);

    int getGameDuration();

    void onFragmentReady();

    void restartGame();

    void onGameStoped();
}
