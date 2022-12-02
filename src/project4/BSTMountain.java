package project4;

/**
 * Represents a mountain implemented as a binary search tree of rest stops.
 * 
 * @author Ishan Pranav
 */
public class BSTMountain extends BST<RestStop> {
    /** Initializes a new instance of the {@link BSTMountain} class. */
    public BSTMountain() {
    }

    /**
     * Traverses the mountain and discovers all viable paths that bring a hiker
     * safely from its summit to its base.
     * 
     * @return a matrix containing paths (rows) comprised of rest-stop steps
     *         (columns)
     */
    public ArrayMatrix<RestStop> findPaths() {
        // Use a node array, a hiker array, a path array, a depth array, a rest stop
        // matrix, and one index to simulate a recursive function stack trace with a
        // node argument, a hiker argument, a path argument, a depth argument, and a
        // rest stop matrix return value; the buffer size must be height + 1 to
        // accommodate both the left side and the right side pushed at the same time

        final int height = height();
        final int maxDepth = height - 1;
        final int bufferSize = height + 1;
        final int[] depths = new int[bufferSize];
        final Object[] nodes = new Object[bufferSize];
        final Hiker[] hikers = new Hiker[bufferSize];
        final RestStop[][] paths = new RestStop[bufferSize][];
        final ArrayMatrix<RestStop> results = new ArrayMatrix<RestStop>(0, height);

        int index = 1;

        // Begin with the root node, a new hiker, and an empty path with as many steps
        // as the height of the tree (the current working path)

        nodes[0] = getRoot();
        hikers[0] = new Hiker();
        paths[0] = new RestStop[height];

        while (index > 0) {
            // Process the current node, path, rest stop, depth, and hiker

            index--;

            final Node node = (Node) nodes[index];

            if (node == null) {
                continue;
            }

            final RestStop[] path = paths[index];
            final RestStop restStop = node.getValue();
            final Hiker hiker = hikers[index];

            int depth = depths[index];

            // Exit early If the bottom of the mountain was reached successfully

            if (depth == maxDepth) {
                if (hiker.isAlive()) {
                    // Include the last rest stop in the current working path if the hiker has
                    // survived thus far

                    path[depth] = restStop;

                    // Copy the rest-stop steps of the current working path into the results matrix

                    results.add(path);
                }

                continue;
            }

            // Collect supplies and overcome obstacles

            hiker.visit(restStop);

            // Exit early if the hiker does not survive the visit to the rest stop

            if (!hiker.isAlive()) {
                continue;
            }

            // Add the current rest stop to the current working path
            // Push the right and left sides (stack is a last-in, first-out collection)
            // Create shallow clones of the hikers and paths to avoid interference across
            // calls when using mutable reference types

            path[depth] = restStop;
            depth++;
            depths[index] = depth;
            nodes[index] = node.getRight();
            hikers[index] = new Hiker(hiker);
            paths[index] = new RestStop[height];

            System.arraycopy(path, 0, paths[index], 0, height);

            index++;
            depths[index] = depth;
            nodes[index] = node.getLeft();
            hikers[index] = new Hiker(hiker);
            paths[index] = new RestStop[height];

            System.arraycopy(path, 0, paths[index], 0, height);

            index++;
        }

        return results;
    }
}
