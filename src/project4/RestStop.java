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
    private final SupplyCollection supplies;
    private final int rivers;
    private final int fallenTrees;

    /** Initializes a new instance of the {@link RestStop} class. */
    public RestStop(String label, SupplyCollection supplies, int rivers, int fallenTrees) {
        this.label = label;
        this.supplies = supplies;
        this.rivers = rivers;
        this.fallenTrees = fallenTrees;
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

    @Override
    public String toString() {
        return label;
    }

    @Override
    public int hashCode() {
        return label.hashCode();
    }

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

        return label.equals(((RestStop)obj).label);
    }
}
