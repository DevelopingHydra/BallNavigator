package at.passini.ballnavigator.map.MapPart;

import android.graphics.Rect;

import at.passini.ballnavigator.game.gameobjects.Brick;
import at.passini.ballnavigator.map.MapSegment;
import at.passini.ballnavigator.map.MapTheme;

/**
 * Created by Benedikt on 08.02.2018.
 */

public class MapPartGemInBricks extends MapSegment{

    public MapPartGemInBricks() throws Exception {
        super(MapTheme.NORMAL,30, 30, 1);
        addGameObject(new Brick(new Rect(0,0,1,1),2));
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
}
