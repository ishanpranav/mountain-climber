package project4;

/**
 * Represents a hiker traveling down a mountain.
 * 
 * This class stores all the supplies that the hiker has in their possession.
 * 
 * @author Ishan Pranav
 */
public class Hiker {
    private final SupplyCollection backpack = new SupplyCollection();

    public SupplyCollection getBackpack() {
        return backpack;
    }
    
    /** Initializes a new instance of the {@link Hiker} class. */
    public Hiker() {
    }
}
