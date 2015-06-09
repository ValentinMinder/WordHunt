package ch.heigvd.wordhuntclient.activities.Game;

import android.util.Log;
import android.widget.Button;

/**
 * Created by paulnta on 13.05.15.
 */
public class InfosButton {

    private int position;
    private Button button;
    private int posX;
    private int posY;

    public InfosButton(Button button, int position) {
        this.button = button;
        this.position = position;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button, int position) {

        if(button == null) {
            this.button = button;
        }

        if(this.position != position) {
            Log.w("BUTTON", "different position when replacing button");
            this.position = position;
        }
    }

    public void setPosX(int posX) {
        this.posX = posX;
        Log.d("POSITsION", "at " + position + " : " + posX );
    }

    public void setPosY(int posY) {
        this.posY = posY;
        Log.d("POSITION", "at " + position + " : " + posY );
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
}
