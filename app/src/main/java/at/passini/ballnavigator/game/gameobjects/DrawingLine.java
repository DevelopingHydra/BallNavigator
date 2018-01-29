package at.passini.ballnavigator.game.gameobjects;

import android.graphics.Canvas;

import java.util.LinkedList;

import at.passini.ballnavigator.game.Helper.Vector;

/**
 * Created by xeniu on 29.01.2018.
 */

public class DrawingLine implements DrawableObject {
    private LinkedList<Vector>  positions;

    public DrawingLine() {
        this.positions=new LinkedList<>();
    }

    public void onTouch(int posX, int posY){

    }

    public void addPosition(Vector vPosition){
        this.positions.add(vPosition);
    }

    @Override
    public void onDrawUpdate(Canvas canvas, long timePassed) {

    }
}
