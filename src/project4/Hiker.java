package project4;

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
    
    /** Initializes a new instance of the {@link Hiker} class. */
    public Hiker() {
    }

    /** 
     * Initializes a new instance of the {@link Hiker} class. 
     * 
     * @param other the hiker to clone
    */
    public Hiker(Hiker other) {
        foodRations = other.foodRations;
        rafts = other.rafts;
        axes = other.axes;
    }

    /**
     * Visits a rest stop and collects the supplies available there.
     * 
     * @param restStop the rest stop to visit
     * @return {@code true} if the hiker survived at the rest stop; {@code false} otherwise
     */
    public boolean visit(RestStop restStop) {
        // Collect the food, rafts, and axes available, and consume one food ration
        // Negative rafts represent rivers; negative axes represent fallen trees

        foodRations += restStop.getFoodRations() - 1;
        rafts += restStop.getRafts();
        axes += restStop.getAxes();
        
        // If any resource is negative, then the hiker has failed to survive

        return foodRations >= 0 && rafts >= 0 && axes >= 0;
    }
}
