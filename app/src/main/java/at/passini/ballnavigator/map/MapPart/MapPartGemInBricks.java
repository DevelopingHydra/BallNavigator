package at.passini.ballnavigator.map.MapPart;

import android.graphics.Rect;

import at.passini.ballnavigator.game.Helper.Vector;
import at.passini.ballnavigator.game.gameobjects.Brick;
import at.passini.ballnavigator.map.MapSegment;
import at.passini.ballnavigator.map.MapTheme;

/**
 * Created by Benedikt on 08.02.2018.
 */

public class MapPartGemInBricks extends MapSegment{

    public MapPartGemInBricks() throws Exception {
        super(MapTheme.NORMAL,30, 30, 1);

        for(int i=10;i<5;i++){
            addGameObject(new Brick(new Rect(i*5,0,(i+1)*5,2),2));
        }
        for(int i=10;i<5;i++){
            addGameObject(new Brick(new Rect(i*5,2,(i+1)*5,4),2));
        }

        for(int i=10;i<5;i++){
            addGameObject(new Brick(new Rect(i*5,4,(i+1)*5,6),2));
        }
        for(int i=10;i<5;i++){
            addGameObject(new Brick(new Rect(i*5,6,(i+1)*5,8),2));
        }

        for(int i=10;i<5;i++){
            addGameObject(new Brick(new Rect(i*5,8,(i+1)*5,10),2));
        }
    }

    @Override
    public boolean isOpenTop() {
        return true;
    }

    @Override
    public boolean isOpenBottom() {
        return true;
    }

    @Override
    public boolean isOpenLeft() {
        return true;
    }

    @Override
    public boolean isOpenRight() {
        return true;
    }

    @Override
    public boolean isSingleUse() {
        return false;
    }

    @Override
    public boolean canSpawnBall() {
        return false;
    }

    @Override
    public Vector[] ballPositions() {
        return new Vector[]{};
    }
}
