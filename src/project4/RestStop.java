package project4;

import java.util.ArrayList;

/**
 * Represents a single rest stop.
 * 
 * This class stores the label of a rest stop along with a list of the supplies
 * that a hiker can collect there and list of obstacles that a hiker may
 * encounter.
 * 
 * @author Ishan Pranav
 */
public class RestStop implements Comparable<RestStop> {
    private final String label;
    private final ArrayList<Supply> supplies = new ArrayList<Supply>();
    private final ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();

    /** Initializes a new instance of the {@link RestStop} class. */
    public RestStop(String label) {
        this.label = label;
    }

    /**
     * Adds the specified supply to the rest stop.
     * 
     * @param supply the supply to add
     */
    public void add(Supply supply) {
        supplies.add(supply);
    }

    /**
     * Adds the specified obstacle to the rest stop.
     * 
     * @param obstacle the obstacle to add
     */
    public void add(Obstacle obstacle) {
        obstacles.add(obstacle);
    }

    /**
     * Compares this object with the specified object for order.
     * 
     * Returns a negative integer, zero, or a positive integer as this object is
     * less than, equal to, or greater than the specified object.
     * 
     * @param o the object to be compared
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it from
     *                              being compared to this object.
     */
    @Override public int compareTo(RestStop o) {
        return 0;
    }
}
