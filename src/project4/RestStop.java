package project4;

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
    private final int foodRations;
    private final int rafts;
    private final int axes;

    /**
     * Initializes a new instance of the {@link RestStop} class.
     * 
     * @param label       the unique identifier of the rest stop
     * @param foodRations the number of single food rations available
     * @param rafts       the net rafts, or the difference between the number of
     *                    rafts available and the number of rivers at the rest stop
     * @param axes        the net axes, or the difference between the number of axes
     *                    available and the number of fallen trees at the rest stop
     */
    public RestStop(String label, int foodRations, int rafts, int axes) {
        this.label = label;
        this.foodRations = foodRations;
        this.rafts = rafts;
        this.axes = axes;
    }

    /**
     * Gets the number of single food rations available at this rest stop.
     * 
     * @return the food supplies
     */
    public int getFoodRations() {
        return foodRations;
    }

    /**
     * Gets the net rafts, or the difference between the number of rafts
     * available at this rest stop and the number of rivers at this rest stop.
     * 
     * @return the difference between raft supplies and river obstacles
     */
    public int getRafts() {
        return rafts;
    }

    /**
     * Gets the net axes, or the difference between the number of axes available at
     * this rest stop and the number of fallen trees at this rest stop.
     * 
     * @return the difference between axe supplies and fallen tree obstacles
     */
    public int getAxes() {
        return axes;
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
    @Override
    public int compareTo(RestStop o) {
        if (o == null) {
            throw new NullPointerException("Argument is not null. Argument name: o.");
        }

        return label.compareTo(o.label);
    }

    /**
     * Returns a string representation of this rest stop.
     * 
     * @return the rest stop label
     */
    @Override
    public String toString() {
        return label;
    }

    /**
     * Returns a hash code value for the object.
     * 
     * @return the hash code value of the rest stop label
     */
    @Override
    public int hashCode() {
        return label.hashCode();
    }

    /**
     * Compares the specified object with rest stop tree for equality.
     * 
     * Returns {@code true} if the given object is also a rest stop and the two rest
     * stops have the same label.
     * 
     * @param obj object to be compared for equality with this rest stop
     * @return {@code true} if the specified object is equal to this rest stop
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof RestStop)) {
            return false;
        }

        return label.equals(((RestStop) obj).label);
    }
}
