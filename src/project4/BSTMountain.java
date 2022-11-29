package project4;

import java.util.Arrays;

/**
 * Represents a mountain implemented using a binary search tree.
 * 
 * @author Ishan Pranav
 */
public class BSTMountain extends BST<RestStop> {
    /** Initializes a new instance of the {@link BSTMountain} class. */
    public BSTMountain() {
    }

    public ArrayMatrix<RestStop> findPaths() {
        final int height = height();
        final int maxDepth = height - 1;
        final int bufferSize = height + 1;
        final ArrayMatrix<RestStop> results = new ArrayMatrix<RestStop>(0, height);
        final Object[] nodes = new Object[bufferSize];
        final Hiker[] hikers = new Hiker[bufferSize];
        final RestStop[][] paths = new RestStop[bufferSize][];
        final int[] depths = new int[bufferSize];
        
        int index = 1;
        
        nodes[0] = getRoot();
        hikers[0] = new Hiker();
        paths[0] = new RestStop[height];
        
        while (index > 0) {
            index--;

            final Node node = (Node) nodes[index];

            if (node == null) {
                continue;
            }
            
            final RestStop[] path = paths[index];
            final RestStop restStop = node.getValue();

            int depth = depths[index];
            
            if (depth == maxDepth) {
                path[depth] = restStop;
                
                results.add(path);
            
                continue;
            }

            final Hiker hiker = hikers[index];

            if (!hiker.visit(restStop)) {
                continue;
            }

            path[depth] = restStop;
            depth++;
            nodes[index] = node.getRight();
            hikers[index] = new Hiker(hiker);
            paths[index] = Arrays.copyOf(path, path.length);
            depths[index] = depth;
            index++;
            nodes[index] = node.getLeft();
            hikers[index] = new Hiker(hiker);
            paths[index] = Arrays.copyOf(path, path.length);
            depths[index] = depth;
            index++;
        }
        
        return results;
    }
}
