package ch.heigvd.wordhunt.activities.Game;

import android.animation.Animator;
import android.app.Activity;
import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import ch.heigvd.wordhunt.Interaction.Animator.ScoreAnimator;
import ch.heigvd.wordhunt.design.R;

/**
 * Created by paulnta on 14.06.15.
 */
public class ScoreFragment extends Fragment {

    public final static String USER_SOLUTIONS = "user_solution";
    public final static String USER_SCORE = "user_score";
    public final static String BEST_SCORE = "best_score";
    public final static String LOG = "SCORE";
    private IGameManager gameManager;

    private int userScore;
    private int bestScore;

    private String[] userSolutions;
    private ScoreAnimator score;
    private ListView listView;
    private LinearLayout layoutScore;
    private TextView numWords;

    private LinearLayout bestScoreLayout;
    private ScoreAnimator bestScoreAnimator;

    private TextView winnerTextView;


    public static ScoreFragment newInstance(int score, int bestScore,  String[] userSolutions){

        ScoreFragment scoreFragment = new ScoreFragment();
        Bundle args = new Bundle();
        args.putStringArray(USER_SOLUTIONS, userSolutions);
        args.putInt(USER_SCORE, score);
        args.putInt(BEST_SCORE, bestScore);
        scoreFragment.setArguments(args);

        return scoreFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            gameManager = (IGameManager) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement interface GameManager");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userScore = getArguments().getInt(USER_SCORE);
        userSolutions = getArguments().getStringArray(USER_SOLUTIONS);
        bestScore = getArguments().getInt(BEST_SCORE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView  = inflater.inflate(R.layout.score_fragment,container,false);

        listView = new ListView(getActivity());
        listView.setBackgroundColor(getResources().getColor(R.color.WHITE));
        layoutScore = (LinearLayout) rootView.findViewById(R.id.containerListSolution);
        bestScoreLayout = (LinearLayout) rootView.findViewById(R.id.bestScoreLayout);
        numWords = (TextView) rootView.findViewById(R.id.num_words);
        winnerTextView = (TextView) rootView.findViewById(R.id.textWinner);

        if(bestScore > 0) {
            bestScoreAnimator = new ScoreAnimator((TextView) rootView.findViewById(R.id.bestScore), 200);
            bestScoreAnimator.addAnimationListener(winnerAnimListener);
        }

        score = new ScoreAnimator((android.widget.TextView) rootView.findViewById(R.id.score), 200);
        score.addPoints(userScore);
        score.addAnimationListener(scoreAnimListener);

        ImageButton restartGame = (ImageButton) rootView.findViewById(R.id.buttonRestartGame);
        restartGame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gameManager.restartGame();
            }
        });


        return rootView;
    }

    private void showUserSolution(){

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1, userSolutions);

        listView.setAdapter(adapter);
        layoutScore.addView(listView);
        layoutScore.setVisibility(View.VISIBLE);
    }

    private Animator.AnimatorListener winnerAnimListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

            // User Win
            if(userScore > bestScore){
                winnerTextView.setText(R.string.you_win);

            } else {
                winnerTextView.setText(R.string.you_loose);
            }

            winnerTextView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    private Animator.AnimatorListener scoreAnimListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            Resources res = getResources();
            numWords.setText(String.format(res.getString(R.string.num_words), userSolutions.length));
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            debug("onAnimationEnd");
            showUserSolution();

            if(bestScoreAnimator != null && bestScoreLayout != null){
                bestScoreLayout.setAlpha(0);
                bestScoreLayout.setVisibility(View.VISIBLE);
                bestScoreLayout.startAnimation(AnimationUtils.loadAnimation(getActivity(),android.R.anim.fade_in));
                bestScoreAnimator.addPoints(bestScore);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {}

        @Override
        public void onAnimationRepeat(Animator animation) {}
    };

    private void debug(String text){
        Log.d(LOG, text);
    }

}
