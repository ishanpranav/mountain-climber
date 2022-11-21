package project4;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Stack;

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
    private class Node {
        private E value;
        private int count;
        private int height;
        private Node left;
        private Node right;

        /**
         * Initializes a new instance of the {@link Node} class.
         * 
         * @param value The node data.
         */
        public Node(E value) {
            this.value = value;
        }

        public void saveChanges() {
            final boolean hasLeft = left != null;
            final boolean hasRight = right != null;
            
            if (hasLeft && hasRight) {
                height = Math.max(left.height, right.height);
            } else if (hasLeft) {
                height = left.height;
            } else if (hasRight) {
                height = right.height;
            } else {
                height = 0;
            }
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
        private final Object[] buffer = new Object[count];

        private int nextIndex;
        private int processIndex;

        /**
         * Called from constructors in derived classes to initialize the
         * {@link BSTIterator} class.
         */
        protected BSTIterator() {
        }

        /**
         * Returns {@code true} if the traversal has more elements.
         * 
         * @return {@code true} if the traversal has more elements
         */
        @Override
        public boolean hasNext() {
            return nextIndex < count;
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

            if (nextIndex >= count) {
                throw new NoSuchElementException("Collection has no more elements.");
            }

            final Object result = buffer[nextIndex];

            nextIndex++;

            return (E) result;
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

                final BSTFixedStack stack = new BSTFixedStack();

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
                        // Move from left to right if the stack is not empty

                        current = stack.pop();

                        process(current.value);

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

                final BSTFixedStack stack = new BSTFixedStack();

                // Begin with the root node

                stack.push(root);

                while (!stack.isEmpty()) {
                    // Process the current node

                    Node current = stack.pop();

                    process(current.value);

                    // Push the right side (stack is a last-in, first-out collection)

                    if (current.right != null) {
                        stack.push(current.right);
                    }

                    // Push the left side

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

                final BSTFixedStack stack = new BSTFixedStack();

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
     * Provides a fixed-size array-based stack implementation optimized for the
     * binary search tree's O(H) operations.
     * 
     * This class simulates a recursive function stack trace.
     * 
     * @author Ishan Pranav
     */
    private class BSTFixedStack {
        private final Object[] buffer;

        private int stackSize;

        /**
         * Initializes a new instance of the {@link BSTFixedStack} class with sufficient
         * capacity for the binary search tree's O(H) operations.
         */
        public BSTFixedStack() {
            buffer = new Object[root.height + 1];
        }

        /**
         * Gets a value indicating whether the collection is empty.
         * 
         * @return {@code true} if the stack is empty; otherwise, {@code false}
         */
        public boolean isEmpty() {
            return stackSize == 0;
        }

        /**
         * Adds the specified item to the front of the collection.
         * 
         * @param item the item to add
         */
        public void push(Node item) {
            buffer[stackSize] = item;
            stackSize++;
        }

        /**
         * Gets the front of the collection.
         * 
         * @return the first item
         */
        public Node peek() {
            return (Node) buffer[stackSize - 1];
        }

        /**
         * Retrieves and removes the first item from the front of the collection.
         * 
         * @return the item removed
         */
        public Node pop() {
            final Node result = peek();

            stackSize--;
            buffer[stackSize] = null;

            return result;
        }
    }

    private int count;
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

        for (E item : collection) {
            add(item);
        }
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

        BSTFixedStack stack = new BSTFixedStack();
        Node current = root;
        Node parent = root;
        int comparison = 0;

        while (current != null) {
            comparison = e.compareTo(current.value);

            if (comparison == 0) {
                // The set already contains the specified element

                return false;
            }

            // Update previous

            parent = current;

            if (comparison < 0) {
                // If the element precedes the current node, advance left

                current = current.left;
            } else {
                // If the element follows the current node, advance right

                current = current.right;
            }
        }

        final Node node = new Node(e);

        if (comparison == 0) {
            // The root is null

            root = node;
        } else {
            if (comparison < 0) {
                // If the last move was left (the new node belongs on the left side of the
                // parent and the parent's left child is null), then the new node is the
                // parent's new left child

                parent.left = node;
            } else {
                // If the last move was right (the new node belongs on the right side of the
                // parent and the parent's right child is null), then the new node is the
                // parent's new right child

                parent.right = node;
            }
        }

        count++;
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

        E e = (E) o;
        Node current = root;
        Node parent = null;

        while (current != null) {
            final int comparison = e.compareTo(current.value);

            if (comparison == 0) {
                // Found the element

                break;
            }

            parent = current;

            if (comparison < 0) {
                // If the element precedes the current node, advance left

                current = current.left;
            } else {
                // If the element follows the current node, advance right

                current = current.right;
            }
        }

        if (current == null) {
            // The root is null or the loop terminated without finding the element

            return false;
        }

        final boolean hasLeft = current.left != null;
        final boolean hasRight = current.right != null;

        if (hasLeft && hasRight) {
            // If the current node has two children, start on the right sub-tree

            Node successor = current.right;
            Node successorParent = current;

            // Move to the extreme left of the right sub-tree

            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }

            if (successorParent == current) {
                // Add the inorder successor's right sub-tree to the right of the current node

                current.right = successor.right;
            } else {
                // Add the inorder succesor's right sub-tree to the left of the current node

                successorParent.left = successor.right;
            }

            current.value = successor.value;
        } else {
            // The current node has one child or no children

            final Node child;

            if (current.left == null) {
                // If the current node has no left child, then it will be replaced by its right
                // child; otherwise, if it has no children, it will be replaced by a null
                // reference

                child = current.right;
            } else {
                // If the current node has a left child, then it will be replaced by its left
                // child

                child = current.left;
            }

            if (parent == null) {
                // If the current node is the root, then its child will become the new root

                root = child;
            } else {
                if (current == parent.left) {
                    // If the current node is its parent's left child, then its parent will adopt
                    // its child (or a null reference) on the left side

                    parent.left = child;
                } else {
                    // If the current node is its parent's right child, then its parent will adopt
                    // its child (or a null reference) on the right side

                    parent.right = child;
                }
            }
        }

        count--;
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
        count = 0;
        version++;
    }

    /**
     * Retrieves the node containing the specified element.
     * 
     * WARNING: This method does not enforce logical preconditions.
     * Precondition: {@code e} is not {@code null}.
     * 
     * @param e the element to find
     * @return the node containing {@code e}, or {@code null} if no such element
     *         exists in the tree
     */
    private Node find(E e) {
        Node current = root;

        while (current != null) {
            int comparison = e.compareTo(current.value);

            if (comparison == 0) {
                // Found the element

                return current;
            } else if (comparison < 0) {
                // If the element precedes the current node, advance left

                current = current.left;
            } else if (comparison > 0) {
                // If the element follows the current node, advance right

                current = current.right;
            }
        }

        // The root is null or the loop terminated without finding the element

        return null;
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

        return find((E) o) != null;
    }

    /**
     * Returns the number of elements in this tree.
     * 
     * This operation should be O(1).
     * 
     * @return the number of elements in this tree
     */
    public int size() {
        return count;
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
        return root.height + 1;
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
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException("Argument is out of range. Argument name: index.");
        }

        Node current = root;

        while (current != null) {
            final int leftCount;

            if (current.left == null) {
                leftCount = 0;
            } else {
                leftCount = current.left.count;
            }

            if (index < leftCount) {
                current = current.left;
            } else if (index < leftCount) {
                current = current.right;
                index -= leftCount + 1;
            } else {
                return current.value;
            }
        }

        // Logically unreachable

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
            // The argument is the current instance

            return true;
        }

        if (!(obj instanceof BST)) {
            // The argument is not a tree

            return false;
        }

        final BST other = (BST) obj;

        if (other.count != count) {
            // The two trees have different sizes

            return false;
        }

        Iterator<E> iterator = iterator();
        Iterator otherIterator = other.iterator();

        while (iterator.hasNext()) {
            if (!Objects.equals(otherIterator.next(), iterator.next())) {
                // The order of the elements is not identical

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
     * String.valueOf(Object).
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
            result.append(iterator.next());

            hasNext = iterator.hasNext();

            if (hasNext) {
                result.append(", ");
            }
        }

        return result
                .append(']')
                .toString();
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
     * @return string containing tree-like representation of this tree.
     */
    public String toStringTreeFormat() {
        // A preorder traversal occurs in root-left-right order

        final Object[] nodes = new Object[count];
        final int[] levels = new int[count];
        final StringBuilder result = new StringBuilder();

        int index = 1;

        // Begin with the root node

        nodes[0] = root;
        levels[0] = 1;

        while (index > 0) {
            // Process the current node

            index--;

            final Node node = (Node) nodes[index];

            int level = levels[index];

            for (int i = 1; i < level; i++) {
                result.append("   ");
            }

            result.append("|--");

            if (node == null) {
                result.append("null");
            } else {
                // Push the right and left sides (stack is a last-in, first-out collection)

                level++;
                nodes[index] = node.right;
                levels[index] = level;
                nodes[index + 1] = node.left;
                levels[index + 1] = level;
                index += 2;

                result.append(node.value);
                result.append(" [");
                result.append(node.height);
                result.append(']');
            }

            result.append('\n');
        }

        return result.toString();
    }
}
