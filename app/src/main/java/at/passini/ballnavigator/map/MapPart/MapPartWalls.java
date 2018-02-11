package at.passini.ballnavigator.map.MapPart;

import at.passini.ballnavigator.game.GameManager;
import at.passini.ballnavigator.game.Helper.Vector;
import at.passini.ballnavigator.game.gameobjects.Wall;
import at.passini.ballnavigator.map.MapSegment;
import at.passini.ballnavigator.map.MapTheme;

/**
 * Created by Benedikt on 11.02.2018.
 */

public class MapPartWalls extends MapSegment{
    private int gridRows = GameManager.getInstance().getGridRows();
    private int gridColumns = GameManager.getInstance().getGridColumns();

    public MapPartWalls() throws Exception {
        super(MapTheme.NORMAL,  GameManager.getInstance().getGridRows(), GameManager.getInstance().getGridColumns(), 1);

        //Add wall on the side
        Wall left = new Wall(0, 0, 0, gridRows);
        Wall top = new Wall(0, 0, gridColumns, 0);
        Wall right = new Wall(gridColumns, 0, gridColumns, gridRows);
        Wall bottom = new Wall(0, gridRows, gridColumns, gridRows);

        addGameObject(left);
        addGameObject(right);
        addGameObject(top);
        addGameObject(bottom);
    }

    @Override
    public boolean isOpenTop() {
        return false;
    }

    @Override
    public boolean isOpenBottom() {
        return false;
    }

    @Override
    public boolean isOpenLeft() {
        return false;
    }

    @Override
    public boolean isOpenRight() {
        return false;
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
        return new Vector[0];
    }
}
