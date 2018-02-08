package at.passini.ballnavigator.map;

import java.util.ArrayList;

import at.passini.ballnavigator.game.gameobjects.GameObject;
import at.passini.ballnavigator.game.gameobjects.Gem;

/**
 * Created by Benedikt on 08.02.2018.
 */

public abstract class MapSegment {
    private ArrayList<GameObject> gameObjects = new ArrayList<>();

    private int gridRows;
    private int gridColumns;
    private int difficulty;
    private MapTheme theme;

    public MapSegment(MapTheme theme,int gridRows, int gridColumns, int difficulty) throws Exception {
        this.theme = theme;
        this.gridRows = gridRows;
        this.gridColumns = gridColumns;
        this.difficulty = difficulty;

        if (gridRows < 25 || gridColumns < 25) {
            throw new Exception("Gridsize must be 25 or higher");
        }

        if (difficulty < 1 || difficulty > 5) {
            throw new Exception("Difficulty must be between 1 and 5");
        }
    }

    public ArrayList<GameObject> getGameObjects(){
        return gameObjects;
    }

    public void addGameObject(GameObject gameObject) {
        this.gameObjects.add(gameObject);
    }

    public boolean hasGem(){
        for(GameObject go : gameObjects){
            if(go instanceof Gem){
                return  true;
            }
        }
        return false;
    }

    public int getGridRows() {
        return gridRows;
    }

    public int getGridColumns() {
        return gridColumns;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public MapTheme getTheme() {
        return theme;
    }

    public abstract boolean isOpenTop();
    public abstract boolean isOpenBottom();
    public abstract boolean isOpenLeft();
    public abstract boolean isOpenRight();
    public abstract boolean isSingleUse();

}
