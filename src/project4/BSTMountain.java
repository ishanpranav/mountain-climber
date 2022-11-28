package project4;

/**
 * Represents a mountain implemented using a binary search tree.
 * 
 * @author Ishan Pranav
 */
public class BSTMountain extends BST<RestStop> {
    /** Initializes a new instance of the {@link BSTMountain} class. */
    public BSTMountain() {
    }
    
    public RestStop[][] hike(Hiker hiker) {
        
        return new RestStop[][] {
            new RestStop[] { root.getValue() }
        };
    }
}
