package project4;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * An implementation of a binary search tree. The elements are ordered using
 * their natural ordering.
 * 
 * This implementation provides guaranteed O(H) (H is the height of this tree
 * which could be as low as logN for balanced trees, but could be as large as N
 * for unbalanced trees) time cost for the basic operations ({@code add},
 * {@code remove} and {@code contains}).
 * 
 * @param <E> the type of elements maintained by this set
 * @author Ishan Pranav
 * @author Joanna Klukowska
 */
public class BST<E extends Comparable<E>> implements Iterable<E> {

    /**
     * Provides a node for the binary search tree.
     * 
     * @author Ishan Pranav
     */
    protected class Node {
        // To ensure that we are only exposing information on a need-to-know basis, we
        // can fully encapsulate this class by exposing protected read-only property
        // accessors. A private constructor prevents instantiation of new nodes from
        // outside the class. The root node is exposed through a protected read-only
        // property accessor. The tree can never be mutated outside the BST<E> class.

        private E value;
        private int height = 1;
        private int count = 1;
        private Node left;
        private Node right;

        /**
         * Gets the data element contained within the node.
         * 
         * @return the node data
         */
        protected E getValue() {
            return value;
        }

        /**
         * Gets a reference to the left sub-tree of the node.
         * 
         * @return the child on the left side, containing elements that precede this
         *         node instance
         */
        protected Node getLeft() {
            return left;
        }

        /**
         * Gets a reference to the right sub-tree of the node.
         * 
         * @return the child on the right side, containing elements that follow this
         *         node instance
         */
        protected Node getRight() {
            return right;
        }

        /**
         * Initializes a new instance of the {@link Node} class.
         * 
         * @param value The node data.
         */
        private Node(E value) {
            this.value = value;
        }
    }

    /**
     * Defines the core behavior of a binary search tree traversal and provides a
     * base for derived classes. This class wraps an underlying data structure and
     * adds concurrent modification checks.
     * 
     * @author Ishan Pranav
     */
    private abstract class BSTIterator implements Iterator<E> {
        private final int expectedVersion = version;
        private final Object[] buffer;

        private int nextIndex;
        private int processIndex;

        /**
         * Called from constructors in derived classes to initialize the
         * {@link BSTIterator} class.
         */
        protected BSTIterator() {
            // The iterator uses a fixed array buffer since there will always be N elements
            // A cached empty array avoids allocating mutliple identical array instances

            if (root == null) {
                buffer = EmptyArray.instance();
            } else {
                buffer = new Object[root.count];
            }
        }

        /**
         * Returns {@code true} if the traversal has more elements.
         * 
         * @return {@code true} if the traversal has more elements
         */
        @Override
        public boolean hasNext() {
            return nextIndex < buffer.length;
        }

        /**
         * Returns the next element in the traversal.
         * 
         * @throws ConcurrentModificationException if the binary search tree has been
         *                                         modified concurrently with the
         *                                         traversal
         * @throws NoSuchElementException          if the iteration has no more elements
         */
        @Override
        public E next() {
            if (version != expectedVersion) {
                throw new ConcurrentModificationException("Collection was modified during iteration.");
            }

            if (nextIndex >= buffer.length) {
                throw new NoSuchElementException("Collection has no more elements.");
            }

            final E result = (E) buffer[nextIndex];

            nextIndex++;

            return result;
        }

        /**
         * Appends the given item to the internal data structure.
         * 
         * @param item the item to process
         */
        protected void process(E item) {
            buffer[processIndex] = item;
            processIndex++;
        }
    }

    /**
     * Provides a sequential (inorder) traversal for the binary search tree.
     * 
     * @author Ishan Pranav
     */
    private class BSTSequentialIterator extends BSTIterator {

        /** Initializes a new instance of the {@link BSTSequentialIterator} class. */
        public BSTSequentialIterator() {
            if (root != null) {
                // An inorder traversal occurs in left-root-right order

                final BSTFixedStack stack = new BSTFixedStack(root.count);

                // Begin with the root node

                boolean done = false;
                Node current = root;

                while (!done) {
                    if (current != null) {
                        // Move to the extreme left, reversing the left side in the process (stack is a
                        // last-in, first-out collection)

                        stack.push(current);

                        current = current.left;
                    } else if (stack.isEmpty()) {
                        // The leftmost node has been reached and the stack has been cleared

                        done = true;
                    } else {
                        current = stack.pop();

                        process(current.value);

                        // Move from left to right if the stack is not empty

                        current = current.right;
                    }
                }
            }
        }
    }

    /**
     * Provides a preorder traversal for the binary search tree.
     * 
     * @author Ishan Pranav
     */
    private class BSTPreorderIterator extends BSTIterator {

        /** Initializes a new instance of the {@link BSTPreorderIterator} class. */
        public BSTPreorderIterator() {
            if (root != null) {
                // A preorder traversal occurs in root-left-right order

                final BSTFixedStack stack = new BSTFixedStack(root.count);

                // Begin with the root node

                stack.push(root);

                while (!stack.isEmpty()) {
                    // Process the current node

                    final Node current = stack.pop();

                    process(current.value);

                    // Push the right side first (stack is a last-in, first-out collection)

                    if (current.right != null) {
                        stack.push(current.right);
                    }

                    // Then push the left side

                    if (current.left != null) {
                        stack.push(current.left);
                    }
                }
            }
        }
    }

    /**
     * Provides a postorder traversal for the binary search tree.
     * 
     * @author Ishan Pranav
     */
    private class BSTPostorderIterator extends BSTIterator {

        /** Initializes a new instance of the {@link BSTPostorderIterator} class. */
        public BSTPostorderIterator() {
            if (root != null) {
                // A postorder traversal occurs in left-right-root order

                final BSTFixedStack stack = new BSTFixedStack(root.count);

                // Begin with the root node

                stack.push(root);

                boolean done = false;
                Node current = root;

                while (!done) {
                    if (current != null) {
                        // Move to the extreme left, reversing the left side in the process (stack is a
                        // last-in, first-out collection)

                        stack.push(current);

                        current = current.left;
                    } else if (stack.isEmpty()) {
                        // The leftmost node has been reached and the stack has been cleared

                        done = true;
                    } else {
                        Node right = stack.peek().right;

                        if (right == null) {
                            // If the top element of the stack has no right child, then while the node is
                            // the right child of the top element of the stack, move right-to-left and
                            // process the root node

                            do {
                                right = stack.pop();

                                process(right.value);
                            } while (!stack.isEmpty() && stack.peek().right == right);
                        } else {
                            // If the top element of the stack has a right child, then move to that child

                            current = right;
                        }
                    }
                }
            }
        }
    }

    /**
     * Provides a fixed-size array-based stack implementation to simulate a
     * recursive function stack trace.
     * 
     * @author Ishan Pranav
     */
    private class BSTFixedStack {
        private final Object[] buffer;

        private int count;

        /**
         * Initializes a new instance of the {@link BSTFixedStack} class.
         * 
         * @param capacity the fixed capacity of the internal buffer
         */
        public BSTFixedStack(int capacity) {
            // The stack uses a fixed-size array buffer since there will be fixed number of
            // recursive calls
            // A cached empty array avoids allocating multiple identical array instances

            if (capacity == 0) {
                buffer = EmptyArray.instance();
            } else {
                buffer = new Object[capacity];
            }
        }

        /**
         * Gets a value indicating whether the collection is empty.
         * 
         * @return {@code true} if the stack is empty; otherwise, {@code false}
         */
        public boolean isEmpty() {
            return count == 0;
        }

        /**
         * Adds the specified item to the front of the collection.
         * 
         * @param item the item to add
         */
        public void push(Node item) {
            buffer[count] = item;
            count++;
        }

        /**
         * Gets the front of the collection.
         * 
         * @return the first item
         */
        public Node peek() {
            return (Node) buffer[count - 1];
        }

        /**
         * Retrieves and removes the first item from the front of the collection.
         * 
         * @return the item removed
         */
        public Node pop() {
            final Node result = peek();

            count--;
            buffer[count] = null;

            return result;
        }

        /** Clears the stack, updating each node's height and count from bottom up. */
        private void saveChanges() {
            while (count > 0) {
                final Node node = pop();
                final boolean hasLeft = node.left != null;
                final boolean hasRight = node.right != null;

                if (hasLeft && hasRight) {
                    // The height is based on the maximum of the left and right sub-trees
                    // The count is based on the total size of the left and right sub-trees

                    node.height = Math.max(node.left.height, node.right.height) + 1;
                    node.count = node.left.count + node.right.count + 1;
                } else if (hasLeft) {
                    // If there is only a left sub-tree, then use its height and count plus one

                    node.height = node.left.height + 1;
                    node.count = node.left.count + 1;
                } else if (hasRight) {
                    // If there is only a right sub-tree, then use its height and count plus one

                    node.height = node.right.height + 1;
                    node.count = node.right.count + 1;
                } else {
                    // If this is a leaf node, then its height and count are one by default

                    node.height = 1;
                    node.count = 1;
                }
            }
        }
    }

    /**
     * Provides a minimal linked list node with two data fields used in searching
     * and sorting algorithms to simulate a recursive function stack trace.
     * 
     * @author Ishan Pranav
     */
    private static class BSTIndexNode {
        private int left;
        private int right;
        private BSTIndexNode next;

        /** Initializes a new instance of the {@link BSTIndexNode} class. */
        public BSTIndexNode() {
        }
    }

    private int version;
    private Node root;

    /**
     * Constructs a new, empty tree, sorted according to the natural ordering of its
     * elements.
     */
    public BST() {
    }

    /**
     * Constructs a new tree containing the elements in the specified collection,
     * sorted according to the natural ordering of its elements.
     * 
     * @param collection collection whose elements will comprise the new tree
     * @throws NullPointerException if the specified collection is null
     */
    public BST(E[] collection) {
        if (collection == null) {
            throw new NullPointerException("Argument cannot be null. Argument name: collection.");
        }

        // Create a shallow clone of the array to maintain the integrity of the
        // reference passed as a constructor argument; since this method takes a mutable
        // reference type as an argument, we must ensure that that we do not surprise
        // the user of the class by mutating their object (such as by sorting the array)

        final E[] array = (E[]) new Comparable[collection.length];

        System.arraycopy(collection, 0, array, 0, collection.length);
        sort(array);

        // Use linked-list nodes to simulate a recursive function stack trace with a
        // left and right index

        BSTIndexNode head = new BSTIndexNode();

        // The left (first) index will be zero by default
        // Push the last index of the array as the right index

        head.right = array.length - 1;

        while (head != null) {
            // Peek the left and right indices from the top of the stack and calculate their
            // average

            final int left = head.left;
            final int right = head.right;
            final int center = (right + left) / 2;

            // Pop the top of the stack

            head = head.next;

            // Add one median at a time to keep the tree balanced

            add(array[center]);

            if (left < right) {
                // Push two new nodes onto the stack:
                // Simulate recursive call (left: left, right: center - 1)
                // Simulate recursive call (left: center + 1, right: right)

                BSTIndexNode node = new BSTIndexNode();

                node.left = left;
                node.right = center - 1;
                node.next = head;
                head = node;
                node = new BSTIndexNode();
                node.left = center + 1;
                node.right = right;
                node.next = head;
                head = node;
            }
        }
    }

    /**
     * Gets a reference to the root node of the tree.
     * 
     * @return the root node
     */
    protected Node getRoot() {
        return root;
    }

    /**
     * Adds the specified element to this set if it is not already present.
     * 
     * More formally, adds the specified element e to this tree if the set contains
     * no element e2 such that Objects.equals(e, e2). If this set already contains
     * the element, the call leaves the set unchanged and returns false.
     * 
     * This operation should be O(H).
     * 
     * @param e element to be added to this set
     * @return {@code true} if this set did not already contain the specified
     *         element
     * @throws NullPointerException if the specified element is null and this set
     *                              uses natural ordering, or its comparator does
     *                              not permit null elements
     */
    public boolean add(E e) {
        if (e == null) {
            throw new NullPointerException("Argument cannot be null. Argument name: e.");
        }

        // Initialize an empty tree

        if (root == null) {
            root = new Node(e);
            version++;

            return true;
        }

        // Use a fixed-size stack to guarantee that this operation has O(H) space
        // complexity

        final BSTFixedStack stack = new BSTFixedStack(root.height);

        Node parent = null;
        Node current = root;
        int comparison = 0;

        while (current != null) {
            stack.push(current);

            // Exit early if the element already exists in the tree

            if (Objects.equals(current.value, e)) {
                return false;
            }

            comparison = e.compareTo(current.value);
            parent = current;

            // Binary search: move left or right based on the element's relative order

            if (comparison < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        final Node node = new Node(e);

        // The current node is null and the parent is the leaf
        // Add to the left or right of the leaf depending on the last comparison

        if (comparison < 0) {
            parent.left = node;
        } else {
            parent.right = node;
        }

        stack.saveChanges();

        version++;

        return true;
    }

    /**
     * Removes the specified element from this tree if it is present.
     * 
     * More formally, removes an element e such that {@code Objects.equals(o, e)},
     * if this tree contains such an element. Returns true if this tree contained
     * the element (or equivalently, if this tree changed as a result of the call).
     * (This tree will not contain the element once the call returns.)
     * 
     * @param o object to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws ClassCastException   if the specified object cannot be compared with
     *                              the elements currently in this tree
     * @throws NullPointerException if the specified element is null
     */
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException("Argument cannot be null. Argument name: o.");
        }

        // Exit early if there is nothing to remove

        if (root == null) {
            return false;
        }

        // Use a fixed-size stack to guarantee that this operation has O(H) space
        // complexity

        final E e = (E) o;
        final BSTFixedStack stack = new BSTFixedStack(root.height);

        Node parent = null;
        Node current = root;

        while (current != null) {
            stack.push(current);

            final int comparison = e.compareTo(current.value);

            // Exit early if the element exists in the tree

            if (comparison == 0) {
                break;
            }

            parent = current;

            // Binary search: move left or right based on the element's relative order

            if (comparison < 0) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        // If the loop terminates without reaching the early exit condition, then the
        // element does not already exist in the tree

        if (current == null) {
            return false;
        }

        final boolean hasLeft = current.left != null;
        final boolean hasRight = current.right != null;

        if (hasLeft && hasRight) {
            // If the current node has two children, start on the right sub-tree

            Node successorParent = current;
            Node successor = current.right;

            stack.push(successor);

            // Move to the extreme left of the right sub-tree

            while (successor.left != null) {
                stack.push(successor.left);

                successorParent = successor;
                successor = successor.left;
            }

            if (successorParent == current) {
                // Move the inorder successor's right sub-tree to the right of the current node

                current.right = successor.right;
            } else {
                // Move the inorder succesor's right sub-tree to the left of its parent

                successorParent.left = successor.right;
            }

            // Replace the current node's value with the value of it's inorder successor

            current.value = successor.value;
        } else {
            // If the current node has one child or no children, replace it with its child
            // or a null reference

            final Node child;

            if (hasLeft) {
                // If the current node has a left child but no right, then the left child will
                // be used

                child = current.left;
            } else {
                // If the current node has a right child but no left, then the right child will
                // be used; otherwise, if it has no children, a null reference will be used

                child = current.right;
            }

            if (parent == null) {
                // If the current node is the root, then its child will become the new root

                root = child;
            } else {
                // The parent of the current node adopts the current node's child (its
                // grandchild) to the same side on which the current node used to be

                if (current == parent.left) {
                    parent.left = child;
                } else {
                    parent.right = child;
                }
            }
        }

        stack.saveChanges();

        version++;

        return true;
    }

    /**
     * Removes all of the elements from this set.
     * 
     * The set will be empty after this call returns.
     * 
     * This operation should be O(1).
     */
    public void clear() {
        root = null;
        version++;
    }

    /**
     * Returns true if this set contains the specified element.
     * 
     * More formally, returns true if and only if this set contains an element e
     * such that {@code Objects.equals(o, e)}.
     * 
     * This operation should be O(H).
     * 
     * @param o object to be checked for containment in this set
     * @return {@code true} if this set contains the specified element
     * @throws ClassCastException   if the specified object cannot be compared with
     *                              the elements currently in the set
     * @throws NullPointerException if the specified element is null and this set
     *                              uses natural ordering, or its comparator does
     *                              not permit null elements
     */
    public boolean contains(Object o) {
        if (o == null) {
            throw new NullPointerException("Argument cannot be null. Argument name: o.");
        }

        final E e = (E) o;
        Node current = root;

        while (current != null) {
            int comparison = e.compareTo(current.value);

            if (comparison == 0) {
                // Found the element

                return true;
            } else if (comparison < 0) {
                // If the element precedes the current node, advance left

                current = current.left;
            } else if (comparison > 0) {
                // If the element follows the current node, advance right

                current = current.right;
            }
        }

        // The root is null or the loop terminated without finding the element

        return false;
    }

    /**
     * Returns the number of elements in this tree.
     * 
     * This operation should be O(1).
     * 
     * @return the number of elements in this tree
     */
    public int size() {
        if (root == null) {
            return 0;
        } else {
            return root.count;
        }
    }

    /**
     * Returns true if this set contains no elements.
     * 
     * This operation should be O(1).
     * 
     * @return {@code true} if this set contains no elements
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Returns the height of this tree.
     * 
     * The height of a leaf is 1. The height of the tree is the height of its root
     * node.
     * 
     * @return the height of this tree or zero if the tree is empty
     */
    public int height() {
        if (root == null) {
            return 0;
        } else {
            return root.height;
        }
    }

    /**
     * Returns an iterator over the elements in this tree in ascending order.
     * 
     * This operation should be O(N).
     * 
     * @return an iterator over the elements in this set in ascending order
     */
    public Iterator<E> iterator() {
        return new BSTSequentialIterator();
    }

    /**
     * Returns an iterator over the elements in this tree in order of the preorder
     * traversal.
     * 
     * This operation should be O(N).
     * 
     * @return an iterator over the elements in this tree in order of the preorder
     *         traversal
     */
    public Iterator<E> preorderIterator() {
        return new BSTPreorderIterator();
    }

    /**
     * Returns an iterator over the elements in this tree in order of the postorder
     * traversal.
     * 
     * This operation should be O(N).
     * 
     * @return an iterator over the elements in this tree in order of the postorder
     *         traversal
     */
    public Iterator<E> postorderIterator() {
        return new BSTPostorderIterator();
    }

    /**
     * Returns the element at the specified position in this tree.
     * 
     * The order of the indexed elements is the same as provided by this tree's
     * iterator. The indexing is zero based (i.e., the smallest element in this tree
     * is at index 0 and the largest one is at index {@code size()}-1).
     * 
     * This operation should be O(H).
     * 
     * @param index index of the element to return
     * @return the element at the specified position in this tree
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   ({@code index < 0 || index >= size()})
     */
    public E get(int index) {
        if (index < 0 || root == null || index >= root.count) {
            throw new IndexOutOfBoundsException("Argument is out of range. Argument name: index.");
        }

        Node current = root;

        while (current != null) {
            // Count the number of nodes in the left sub-tree

            final int leftCount;

            if (current.left == null) {
                leftCount = 0;
            } else {
                leftCount = current.left.count;
            }

            if (index < leftCount) {
                // If the index is on the left side, move left

                current = current.left;
            } else if (index > leftCount) {
                // If the index is on the right side, move right and narrow in on the right
                // sub-tree, skipping the entire left sub-tree and the current node

                current = current.right;
                index -= leftCount + 1;
            } else {
                // The index was found

                return current.value;
            }
        }

        return null;
    }

    /**
     * Returns the least element in this tree greater than or equal to the given
     * element, or {@code null} if there is no such element.
     * 
     * This operation should be O(H).
     * 
     * @param e the value to match
     * @return the least element greater than or equal to {@code e}, or {@code null}
     *         if there is no such element
     * @throws ClassCastException   if the specified element cannot be compared with
     *                              the elements currently in the set
     * @throws NullPointerException if the specified element is null
     */
    public E ceiling(E e) {
        if (e == null) {
            throw new NullPointerException("Argument cannot be null. Argument name: e.");
        }

        Node ceiling = null;
        Node current = root;

        while (current != null) {
            final int comparison = e.compareTo(current.value);

            if (comparison <= 0) {
                // If the element is lower than or equal the current node, advance left
                // The current node is lower than or equal to the element

                ceiling = current;
                current = current.left;
            } else {
                // If the element is strictly higher than the current node, advance right

                current = current.right;
            }
        }

        if (ceiling == null) {
            // The root is null or no nodes are higher than or equal to the element

            return null;
        } else {
            return ceiling.value;
        }
    }

    /**
     * Returns the greatest element in this set less than or equal to the given
     * element, or {@code null} if there is no such element.
     * 
     * This operation should be O(H).
     * 
     * @param e the value to match
     * @return the greatest element less than or equal to {@code e}, or {@code null}
     *         if there is no such element
     * @throws ClassCastException   if the specified element cannot be compared with
     *                              the elements currently in the set
     * @throws NullPointerException if the specified element is null
     */
    public E floor(E e) {
        if (e == null) {
            throw new NullPointerException("Argument cannot be null. Argument name: e.");
        }

        Node floor = null;
        Node current = root;

        while (current != null) {
            final int comparison = e.compareTo(current.value);

            if (comparison >= 0) {
                // If the element is higher than or equal the current node, advance right
                // The current node is higher than or equal to the element

                floor = current;
                current = current.right;
            } else {
                // If the element is strictly lower than the current node, advance left

                current = current.left;
            }
        }

        if (floor == null) {
            // The root is null or no nodes are lower than or equal to the element

            return null;
        } else {
            return floor.value;
        }
    }

    /**
     * Returns the first (lowest) element currently in this tree.
     * 
     * This operation should be O(H).
     * 
     * @return the first (lowest) element currently in this tree
     * @throws NoSuchElementException if this set is empty
     */
    public E first() {
        if (root == null) {
            throw new NoSuchElementException("Cannot retrieve the first element of an empty tree.");
        }

        Node current = root;

        // Move to the extreme left

        while (current.left != null) {
            current = current.left;
        }

        return current.value;
    }

    /**
     * Returns the last (highest) element currently in this tree.
     * 
     * This operation should be O(H).
     * 
     * @return the last (highest) element currently in this tree
     * @throws NoSuchElementException if this set is empty
     */
    public E last() {
        if (root == null) {
            throw new NoSuchElementException("Cannot retrieve the last element of an empty tree.");
        }

        Node current = root;

        // Move to the extreme right

        while (current.right != null) {
            current = current.right;
        }

        return current.value;
    }

    /**
     * Returns the greatest element in this set strictly less than the given
     * element, or {@code null} if there is no such element.
     * 
     * This operation should be O(H).
     * 
     * @param e the value to match
     * @return the greatest element less than {@code e}, or {@code null} if there is
     *         no such element
     * @throws ClassCastException   if the specified element cannot be compared with
     *                              the elements currently in the set
     * @throws NullPointerException if the specified element is null
     */
    public E lower(E e) {
        if (e == null) {
            throw new NullPointerException("Argument cannot be null. Argument name: e.");
        }

        Node lower = null;
        Node current = root;

        while (current != null) {
            final int comparison = e.compareTo(current.value);

            if (comparison > 0) {
                // If the element follows the current node, advance right
                // The current node is strictly higher than the element

                lower = current;
                current = current.right;
            } else {
                // If the element precedes the current node, advance left

                current = current.left;
            }
        }

        if (lower == null) {
            // The root is null or no nodes precede the element

            return null;
        } else {
            return lower.value;
        }
    }

    /**
     * Returns the least element in this tree strictly greater than the given
     * element, or {@code null} if there is no such element.
     * 
     * This operation should be O(H).
     * 
     * @param e the value to match
     * @return the least element greater than {@code e}, or {@code null} if there is
     *         no such element
     * @throws ClassCastException   if the specified element cannot be compared with
     *                              the elements currently in the set
     * @throws NullPointerException if the specified element is null
     */
    public E higher(E e) {
        if (e == null) {
            throw new NullPointerException("Argument cannot be null. Argument name: e.");
        }

        Node higher = null;
        Node current = root;

        while (current != null) {
            final int comparison = e.compareTo(current.value);

            if (comparison < 0) {
                // If the element precedes the current node, advance left
                // The current node is strictly lower than the element

                higher = current;
                current = current.left;
            } else {
                // If the element follows the current node, advance right

                current = current.right;
            }
        }

        if (higher == null) {
            // The root is null or no nodes follow the element

            return null;
        } else {
            return higher.value;
        }
    }

    /**
     * Compares the specified object with this tree for equality.
     * 
     * Returns {@code true} if the given object is also a tree, the two trees have
     * the same size, and every member of the given tree is contained in this tree.
     * 
     * This operation should be O(N).
     * 
     * @param obj object to be compared for equality with this tree
     * @return {@code true} if the specified object is equal to this tree
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            // Exit early if comparing the same instance

            return true;
        }

        if (!(obj instanceof BST)) {
            // Exit early if the types are mismatched

            return false;
        }

        final BST other = (BST) obj;

        if (size() != other.size()) {
            // Exit early if the sizes are different

            return false;
        }

        Iterator<E> iterator = iterator();
        Iterator otherIterator = other.iterator();

        while (iterator.hasNext()) {
            if (!Objects.equals(iterator.next(), otherIterator.next())) {
                // A difference was detected

                return false;
            }
        }

        // No differences were detected

        return true;
    }

    /**
     * Returns a string representation of this tree.
     * 
     * The string representation consists of a list of the tree's elements in the
     * order they are returned by its iterator (inorder traversal), enclosed in
     * square brackets ("[]"). Adjacent elements are separated by the characters ",
     * "
     * (comma and space). Elements are converted to strings as by
     * {@code String.valueOf(Object)}.
     * 
     * This operation should be O(N).
     * 
     * @return a string representation of this collection
     */
    @Override
    public String toString() {
        final StringBuilder result = new StringBuilder("[");
        final Iterator<E> iterator = iterator();

        boolean hasNext = iterator.hasNext();

        while (hasNext) {
            // StringBuilder.append(Object) uses String.valueOf(Object)

            result.append(iterator.next());

            hasNext = iterator.hasNext();

            if (hasNext) {
                result.append(", ");
            }
        }

        return result.append(']').toString();
    }

    /**
     * Produces tree like string representation of this tree.
     * 
     * Returns a string representation of this tree in a tree-like format. The
     * string representation consists of a tree-like representation of this tree.
     * Each node is shown in its own line with the indentation showing the depth of
     * the node in this tree. The root is printed on the first line, followed by its
     * left subtree, followed by its right subtree.
     * 
     * This operation should be O(N).
     * 
     * @return a string containing tree-like representation of this tree
     */
    public String toStringTreeFormat() {
        // Exit early if there is no tree

        if (root == null) {
            return "null";
        }

        // A preorder traversal occurs in root-left-right order

        // Use a node array, an integer array, a string builder, and one index to
        // simulate a recursive function stack trace with a node argument, a level
        // argument, and a string return value; the buffer size must be count + 1 to
        // accommodate both the left side and the right side pushed at the same time

        final int bufferSize = root.count + 1;
        final Object[] nodes = new Object[bufferSize];
        final int[] levels = new int[bufferSize];
        final StringBuilder result = new StringBuilder();

        int index = 1;

        // Begin with the root node

        nodes[0] = root;

        while (index > 0) {
            // Process the current node and level

            index--;

            final Node node = (Node) nodes[index];

            int level = levels[index];

            if (level > 0) {
                for (int indent = 0; indent < level - 1; indent++) {
                    result.append("   ");
                }

                result.append("|--");
            }

            if (node == null) {
                result.append("null");
            } else {
                // Push the right and left sides (stack is a last-in, first-out collection)

                level++;
                nodes[index] = node.right;
                levels[index] = level;
                index++;
                nodes[index] = node.left;
                levels[index] = level;
                index++;

                result.append(node.value);
            }

            if (index > 0) {
                result.append('\n');
            }
        }

        return result.toString();
    }

    /**
     * Sorts an array of comparable values using the QuickSort algorithm.
     * 
     * @param array the values to sort
     */
    private void sort(E[] array) {
        // Use linked-list nodes to simulate a recursive function stack trace with a
        // left and right index

        BSTIndexNode head = new BSTIndexNode();

        // The left (first) index will be zero by default
        // Push the last index of the array as the right index

        head.right = array.length - 1;

        while (head != null) {
            // Peek the left and right indices from the top of the stack and select the
            // pivot index (partition)

            final int left = head.left;
            final int right = head.right;
            final int partition = partition(array, left, right);

            // Pop the top of the stack

            head = head.next;

            if (partition - left > 1) {
                // Push a new node onto the stack:
                // Simulate recursive call (left: left, right: partition - 1)

                final BSTIndexNode node = new BSTIndexNode();

                node.left = left;
                node.right = partition - 1;
                node.next = head;
                head = node;
            }

            if (right - partition > 1) {
                // Push a new node onto the stack:
                // Simulate recursive call (left: partition + 1, right: right)

                final BSTIndexNode node = new BSTIndexNode();

                node.left = partition + 1;
                node.right = right;
                node.next = head;
                head = node;
            }
        }
    }

    /**
     * Partitions the array. This method is used within a broader QuickSort
     * algorithm.
     * 
     * @param array the values to sort
     * @param left  the first index to examine
     * @param right the last index to examine
     * @return the index of the pivot element
     */
    private int partition(E[] array, int left, int right) {
        // Select a pivot element and retrieve its index

        int pivot = choosePivot(array, left, right);

        // Swap the pivot element out of the way (to the end of the array)

        swap(array, right, pivot);

        // Update the pointers

        pivot = right;
        right--;

        while (left <= right) {
            // Move the left pointer right until it points to the pivot or an element that
            // follows it

            while (array[left].compareTo(array[pivot]) < 0) {
                left++;
            }

            // Move the right pointer left until it points to an element that precedes the
            // pivot or until it intersects with the left pointer

            while (right >= left && array[right].compareTo(array[pivot]) >= 0) {
                right--;
            }

            // Swap the left and right elements if the pointers have crossed

            if (right > left) {
                swap(array, left, right);
            }
        }

        // Swap the pivot element into its final position

        swap(array, left, pivot);

        // Return the new index of the pivot element

        return left;
    }

    /**
     * Selects a pivot element using the median-of-three-values strategy. This
     * method is used within a broader QuickSort algorithm.
     * 
     * @param array the values to sort
     * @param left  the first index to examine
     * @param right the last index to examine
     * @return the index of the pivot element
     */
    private int choosePivot(E[] array, int left, int right) {
        final int center = (left + right) / 2;

        // If first element precedes the last element, then swap them

        if (array[right].compareTo(array[left]) < 0) {
            swap(array, left, right);
        }

        // If the center element precedes the first element, then swap them

        if (array[center].compareTo(array[left]) < 0) {
            swap(array, center, left);
        }

        // If the right element precedes the center element, then swap them

        if (array[right].compareTo(array[center]) < 0) {
            swap(array, right, center);
        }

        // The first, last, and center elements have been sorted relative to one another
        // It is relatively efficient to use the center element as the pivot

        return center;
    }

    /**
     * Swaps the elements at two given indices in an array.
     * 
     * @param array the array
     * @param index the index of one element to swap
     * @param other the index of the other element to swap
     */
    private void swap(E[] array, int index, int other) {
        final E item = array[index];

        array[index] = array[other];
        array[other] = item;
    }
}
