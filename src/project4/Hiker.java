package project4;

import java.util.ArrayList;

/**
 * Represents a hiker traveling down a mountain.
 * 
 * This class stores all the supplies that the hiker has in their possession.
 * 
 * @author Ishan Pranav
 */
public class Hiker {
    private int foodRations;
    private int rafts;
    private int axes;
    private ArrayList<RestStop> paths;
    
    /** Initializes a new instance of the {@link Hiker} class. */
    public Hiker() {
        paths = new ArrayList<RestStop>();
    }

    /** Initializes a new instance of the {@link Hiker} class. */
    public Hiker(Hiker other) {
        foodRations = other.foodRations;
        rafts = other.rafts;
        axes = other.axes;
        paths = new ArrayList<RestStop>(other.paths);
    }

    public boolean visit(RestStop restStop) {
        foodRations += restStop.getFoodRations() - 1;
        rafts += restStop.getRafts();
        axes += restStop.getAxes();
        
        if (foodRations >= 0 && rafts >= 0 && axes >= 0) {
            paths.add(restStop);
        
            return true;
        } else {
            return false;
        }
    }
}
