package ch.heigvd.wordhuntclient.activities.Game;

/**
 * Created by paulnta on 08.06.15.
 */
public class GridPoint {

    private int current;

    public GridPoint(){}

    public GridPoint(int current){
        this.current = current;
    }

    public void setCurrent(int current){
        this.current = current;
    }

    public boolean isNear(int other){

        if(        other == posLeft()
                || other == posRight()
                || other == posTop()
                || other == posBottom()
                || other == posRightTop()
                || other == posLeftTop()
                || other == posRightBottom()
                || other == posLeftBottom()
            ){
            return true;
        }

        return false;
    }

    public int posBottom(){
        int pos = current + GameGrid.NB_COL;
        if(pos < GameGrid.NB_COL * GameGrid.NB_ROW)
            return pos;
        return -1;
    }

    public int posTop(){
        int pos = current - GameGrid.NB_COL;
        if(pos >= 0)
            return pos;
        return -1;
    }

    public int posRight(){
        if((current % GameGrid.NB_COL) < GameGrid.NB_COL)
            return current +1;
        return -1;
    }

    public int posLeft(){
        if((current % GameGrid.NB_COL) > 0)
            return current -1;
        return -1;
    }

    public int posLeftTop(){
        GridPoint pointTop = null;
        int top = posTop();
        if(top != -1) {
            pointTop = new GridPoint(top);
            return pointTop.posLeft();
        }
        return -1;
    }

    public int posRightTop(){
        GridPoint pointTop = null;
        int top = posTop();
        if(top != -1) {
            pointTop = new GridPoint(top);
            return pointTop.posRight();
        }
        return -1;
    }

    public int posLeftBottom(){
        GridPoint pointBottom = null;
        int bottom = posBottom();
        if(bottom != -1) {
            pointBottom = new GridPoint(bottom);
            return pointBottom.posLeft();
        }
        return -1;
    }

    public int posRightBottom(){
        GridPoint pointBottom = null;
        int bottom = posBottom();
        if(bottom != -1) {
            pointBottom = new GridPoint(bottom);
            return pointBottom.posRight();
        }
        return -1;
    }

}
