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

    /** Consumes a single ration of food. */
    public void eat() {
        foodRations--;
    }

    /**
     * Visits a rest stop and collects the supplies available there.
     * 
     * @param restStop the rest stop to visit
     */
    public void visit(RestStop restStop) {
        // Collect the food, rafts, and axes available
        // Negative rafts represent rivers; negative axes represent fallen trees

        foodRations += restStop.getFoodRations();
        rafts += restStop.getRafts();
        axes += restStop.getAxes();
    }

    /**
     * Determines whether the hiker has not survived an obstacle.
     * 
     * @return {@code false} if the hiker survived at the rest stop; {@code true}
     *         otherwise
     */
    public boolean isDead() {
        // If any resource is negative, then the hiker has failed to survive

        return foodRations < 0 || rafts < 0 || axes < 0;
    }
}
