package at.passini.ballnavigator.map;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Benedikt on 08.02.2018.
 */

public class Map implements Iterable<MapSegment>{
    private ArrayList<MapSegment> segments = new ArrayList<>();


    public Map(){

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
