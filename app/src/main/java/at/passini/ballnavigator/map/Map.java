package at.passini.ballnavigator.map;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import at.passini.ballnavigator.game.GameManager;
import at.passini.ballnavigator.game.gameobjects.GameObject;
import at.passini.ballnavigator.game.gameobjects.RectGameObject;
import at.passini.ballnavigator.game.gameobjects.Wall;

/**
 * Created by Benedikt on 08.02.2018.
 */

public class Map implements Iterable<MapSegment>{
    private ArrayList<MapSegment> segments = new ArrayList<>();

    public Map(){
    }

    public ConcurrentLinkedQueue<GameObject> getGameObjects(){
        ConcurrentLinkedQueue<GameObject> gameObjects = new ConcurrentLinkedQueue<>();
        for(MapSegment segment:segments){
            for(GameObject gameObjectInSegment:segment){
                if(gameObjectInSegment instanceof RectGameObject){
                    RectGameObject rco =  (RectGameObject) gameObjectInSegment;
                    rco.setGridX(rco.getGridX()+segment.getGridColumns());
                    rco.setGridY(rco.getGridY()+segment.getGridRows());
                    rco.setGridRight(rco.getGridRight()+segment.getGridColumns());
                    rco.setGridBottom(rco.getGridBottom()+segment.getGridRows());
                }else{
                    gameObjects.add(gameObjectInSegment);
                }

            }
        }

        int gridRows = GameManager.getInstance().getGridRows();
        int gridColumns = GameManager.getInstance().getGridColumns();

        //Add wall on the side
        Wall left = new Wall(0, 0, 0, gridRows);
        Wall top = new Wall(0, 0, gridColumns, 0);
        Wall right = new Wall(gridColumns, 0, gridColumns, gridRows);
        Wall bottom = new Wall(0, gridRows, gridColumns, gridRows);

        gameObjects.add(left);
        gameObjects.add(right);
        gameObjects.add(top);
        gameObjects.add(bottom);

        return gameObjects;
    }

    public Map(ArrayList<MapSegment> segments) {
        this.segments = segments;
    }

    public ArrayList<MapSegment> getSegments() {
        return segments;
    }

    public void setSegments(ArrayList<MapSegment> segments) {
        this.segments = segments;
    }

    public void addSegment(MapSegment segment){
        segments.add(segment);
    }

    @NonNull
    @Override
    public Iterator<MapSegment> iterator() {
        return segments.iterator();
    }

}
