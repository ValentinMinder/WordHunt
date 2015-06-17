package ch.heigvd.wordhunt.activities.Game;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;

import ch.heigvd.wordhunt.Interaction.Animator.ScoreAnimator;
import ch.heigvd.wordhunt.design.R;
import whobjects.Grid;
import whprotocol.WHProtocol;


public class GameFragment extends Fragment{

    public String LOG = "GameFragment";
    private WHTextSwitcher result;
    private WHTextSwitcher timer;
    private ProgressBar progressBar;
    private GameGrid gameGrid;
    private IGameManager gameManager;
    private ScoreAnimator scoreAnimator;
    private RelativeLayout rootView;
    private CountDownTimer countDownTimer;
    private boolean started;
    private int mProgressStatus;

    private ScoreAnimator bestScoreAnimator;
    private TextView bestScore;
    private TextView bestScoreLabel;
    private TextView userScoreLabel;

    private LinearLayout bestScoreLayout;
    private LinearLayout gridNotFoundView;
    private LinearLayout countDownLayout;
    private LinearLayout scoreLayout;

    private Handler mHandler = new Handler();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try{
            gameManager = (IGameManager) activity;
            gameManager.onFragmentReady();
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement interface GameManager");
        }
    }

    /**
     * Initialize Essential components of the fragment that you want to retain
     * when the fragment is paused or stopped, then resumed.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        debug("onCreate");

        // Create a countDownTimer only once

        countDownTimer = new CountDownTimer(gameManager.getGameDuration(),1000) {
            @Override
            public void onTick(long l) {
                timer.setText(String.format("%02d:%02d", (l / 1000) / 60, (l / 1000) % 60));
                if(l%5 == 0)
                    debug(String.format("%02d:%02d", (l / 1000) / 60, (l / 1000) % 60));
            }

            @Override
            public void onFinish() {
                started = false;
                gameManager.onGameFinish(scoreAnimator.getCurrentScore());
            }
        };
    }

    /**
     * The system calls this when it's time for the fragment
     * to draw its user interface for the first time.
     *
     * @param inflater to inflate a layout resource defined in XML
     * @param container in which the fragment layout will be inserted
     * @param savedInstanceState
     * @return View that is the root of the fragment's layout.
     *         null if the fragment does not provide a UI
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        debug("onCreateView");
        // Get the fragment layout
        rootView = (RelativeLayout) inflater.inflate(R.layout.game_fragment, container, false);


        // Text result
        TextSwitcher textSwitcher = (TextSwitcher) rootView.findViewById(R.id.resultTextSwitcher);
        result = new WHTextSwitcher(textSwitcher, getActivity());
        result.setInvisible();

        // CountDown
        TextSwitcher timerSwitcher = (TextSwitcher) rootView.findViewById(R.id.timerTextSwitcher);
        timer = new WHTextSwitcher(timerSwitcher, getActivity());

        // ProgressBar
        progressBar = new ProgressBar(getActivity());
        progressBar.setMax(100);

        // User Score
        scoreAnimator = new ScoreAnimator((TextView) rootView.findViewById(R.id.score), 0);

        // Best score
        bestScoreLayout = (LinearLayout) rootView.findViewById(R.id.bestScoreLayout);
        bestScore = (TextView) rootView.findViewById(R.id.bestScore);
        bestScoreLabel = (TextView) rootView.findViewById(R.id.bestScoreLabel);

        // grid not found view
        gridNotFoundView = (LinearLayout) rootView.findViewById(R.id.grid_not_found);
        scoreLayout = (LinearLayout) rootView.findViewById(R.id.scoreLayout);
        countDownLayout = (LinearLayout) rootView.findViewById(R.id.countDownLayout);


        debug("onCreatView Finish (createGrid is processing)");

        return rootView;
    }


    public void createGrid() {
        LinearLayout gridContainer = (LinearLayout) rootView.findViewById(R.id.gridContainer);
        gameGrid = new GameGrid(getActivity(),this, result);
        gridContainer.addView(gameGrid);
    }

    public void fillGrid(Grid grid){

        if (grid.getBestScore() > 0){
            bestScoreAnimator = new ScoreAnimator(bestScore, 1000);
            bestScoreAnimator.addPoints(grid.getBestScore());
            bestScoreAnimator.run();
            showViewSmoothly(bestScoreLayout);
        }

        gameGrid.init(grid);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        debug("onActivityCreated");
        createGrid();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void showViewSmoothly(View v){
        debug("SHOW SMOOTH");
        v.setAlpha(0);
        v.setVisibility(View.VISIBLE);
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(getActivity(), R.anim.layout_smooth_show);
        animation.setInterpolator(new AnticipateOvershootInterpolator());
        ((LinearLayout)v).setLayoutAnimation(animation);
    }

    public void checkResult(){

        String word = result.getText().toString();
        int score = gameManager.onValidateWord(word);

        // Check word
        if(score == 0){
            Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shaking);
            result.startAnimation(shake);

        } else {
            scoreAnimator.addPoints(score);
        }
    }

    private void debug(String text){
        Log.d(LOG, text);
    }

    @Override
    public void onPause() {
        super.onPause();
        debug("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        debug("onStop");
        countDownTimer.cancel();
        started = false;
        gameManager.onGameStoped();
    }

    public void onGridViewReady() {
        if(!started) {
            countDownTimer.start();
            started = true;
        }
        Log.d("GAME", "game started");
    }

    public void requestFinish(){
        countDownTimer.cancel();
        countDownTimer.onFinish();
    }

    public boolean isRunning(){
        return started;
    }

    public void setError(WHProtocol.WHGameType type){

        bestScoreLayout.setVisibility(View.GONE);
        scoreLayout.setVisibility(View.GONE);
        countDownLayout.setVisibility(View.GONE);

        if(type == WHProtocol.WHGameType.COMPETITION){
            ImageView image = (ImageView) gridNotFoundView.findViewById(R.id.image_error);
            image.setImageResource(R.drawable.no_compete);
        }

        gridNotFoundView.setAlpha(0f);
        gridNotFoundView.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.smooth_show);
        gridNotFoundView.startAnimation(anim);

    }
}
