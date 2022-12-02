package project4;

/**
 * Provides a centralized cache for a single empty array instance.
 * 
 * The {@link ArrayMatrix} class, as well as iterators and internal data
 * structures nested within the {@link BST} class, rely on arrays whose lengths
 * may be zero. Repeated calls to {@code new Object[0]} will allocate several
 * functionally identical empty array instances. This class improves performance
 * by eliminating the need to instantiate, allocate, and garbage-collect many
 * identical array instances.
 */
public final class EmptyArray {
    private static Object[] INSTANCE;

    /**
     * Gets an empty array instance.
     * 
     * @param <E> the type of elements in the array
     * @return the empty array
     */
    public static <E> E[] instance() {
        if (INSTANCE == null) {
            INSTANCE = new Object[0];
        }
        
        return (E[]) INSTANCE;
    }

    /** Initializes a new instance of the {@link EmptyArray} class. */
    private EmptyArray() {
    }
}
