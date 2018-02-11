package at.passini.ballnavigator.map;

import java.util.ArrayList;

import at.passini.ballnavigator.game.GameManager;
import at.passini.ballnavigator.map.MapPart.MapPartGemInBricks;

/**
 * Created by Benedikt on 08.02.2018.
 */

public class MapGeneration {
    private static MapGeneration INSTANCE;

    private MapSegment[] mapSegments;

    private final int gridRows;
    private final int gridColumns;

    private String seed;

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

    private void findFirstPart(){

    }

    public Map generateNewMap(int difficulty) {
        Map map =new Map();


        
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
