package ch.heigvd.wordhuntclient.activities.Game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import ch.heigvd.gen.wordhuntclient.R;
import ch.heigvd.wordhuntclient.activities.MainActivity;
import whobjects.Grid;
import whprotocol.WHGridReplyMessage;

public class GameActivity extends Activity {

    private Grid dataGrid;
    private GameGrid gameGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent i = getIntent();

        dataGrid = new Gson().fromJson(
                i.getStringExtra(MainActivity.CLASS_ID),
                WHGridReplyMessage.class).getGrid();

        initGrid();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("START", "appstart");
//        Log.d("START", "grid left: " + String.valueOf(gameGrid.ge));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Start", "app restart");
    }

    private void initGrid(){

        final TextView result = (TextView) findViewById(R.id.textResult);
        LinearLayout layout = (LinearLayout) findViewById(R.id.mainContent);
        gameGrid = new GameGrid(this, result, dataGrid);
        layout.addView(gameGrid);


        Button b = (Button) findViewById(R.id.clear);
        b.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                result.setText("");
                GameGridAdapter.resetButtonsStates();

            }
        });


        Button finish = (Button) findViewById(R.id.buttonFinish);
        finish.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(result.getText().length() < 3) {
                    Animation animation = AnimationUtils.loadAnimation(GameActivity.this, R.anim.shaking);
                    result.startAnimation(animation);
                }
                else{
                    result.setText("WHY NOT ?");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
