package at.passini.ballnavigator.map;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import at.passini.ballnavigator.game.GameManager;
import at.passini.ballnavigator.map.MapPart.MapPartGemInBricks;
import at.passini.ballnavigator.map.MapPart.MapPartWalls;

/**
 * Created by Benedikt on 08.02.2018.
 */

public class MapGeneration {
    private static MapGeneration INSTANCE;

    private MapSegment[] mapSegments;

    private final int gridRows;
    private final int gridColumns;

    private String seed;
    private Random rand = new Random();

    private MapGeneration() throws Exception {
        gridRows = GameManager.getInstance().getGridRows();
        gridColumns = GameManager.getInstance().getGridColumns();
        mapSegments = new MapSegment[]{new MapPartGemInBricks()};
    }

    public static MapGeneration getINSTANCE()throws Exception {
        if(INSTANCE==null){
            INSTANCE = new MapGeneration();
        }
        return INSTANCE;
    }

    private ArrayList<MapSegment> filterSegments(boolean openLeft,boolean openTop, boolean openRight, boolean openBottom) {
        ArrayList<MapSegment> filteredSegments = new ArrayList<>();
        for (MapSegment segment : mapSegments) {
            if (segment.isOpenLeft() == openLeft && segment.isOpenTop() == openTop && segment.isOpenRight() == openRight && segment.isOpenBottom() == openBottom)
                filteredSegments.add(segment);
        }
        return filteredSegments;
    }

    private void findOptimalLeftPart(){

    }



    private MapSegment findFirstPart(){
        ArrayList<MapSegment> filteredSegments = new ArrayList<>();
        for (MapSegment segment : mapSegments) {
            if (segment.isOpenRight() || segment.isOpenBottom()) {
                filteredSegments.add(segment);
            }
        }
        Log.d("GameElements",filteredSegments.toString());
        return filteredSegments.get(rand.nextInt(filteredSegments.size()));
    }

    public Map generateNewMap(int difficulty) throws Exception {
        Map map =new Map();
        map.addSegment(new MapPartWalls());
        map.addSegment(findFirstPart());

        
        return map;
    }

    public int getGridRows() {
        return gridRows;
    }

    public int getGridColumns() {
        return gridColumns;
    }

    public String getSeed() {
        return seed;
    }
}
