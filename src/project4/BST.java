package project4;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
    }

    /**
     * Provides a sequential (inorder) traversal for the binary search tree.
     * 
     * @author Ishan Pranav
     */
    private class BSTSequentialIterator implements Iterator<E> {
        private final int expectedVersion = version;

        /**
         * Initializes a new instance of the {@link BSTSequentialIterator} class.
         */
        public BSTSequentialIterator() {
        }

        /**
         * Returns {@code true} if the traversal has more elements.
         * 
         * @return {@code true} if the traversal has more elements.
         */
        @Override
        public boolean hasNext() {
            return false;
        }

        /**
         * Returns the next element in the traversal.
         * 
         * @throws ConcurrentModificationException if the binary search tree has been
         *                                         modified concurrently with the
         *                                         traversal
         */
        @Override
        public E next() {
            if (version != expectedVersion) {
                throw new ConcurrentModificationException("Collection was modified during iteration.");
            }

            return null;
        }
    }

    /**
     * Provides a preorder traversal for the binary search tree.
     * 
     * @author Ishan Pranav
     */
    private class BSTPreorderIterator implements Iterator<E> {
        private final int expectedVersion = version;

        /**
         * Initializes a new instance of the {@link BSTPreorderIterator} class.
         */
        public BSTPreorderIterator() {
        }

        /**
         * Returns {@code true} if the traversal has more elements.
         * 
         * @return {@code true} if the traversal has more elements.
         */
        @Override
        public boolean hasNext() {
            return false;
        }

        /**
         * Returns the next element in the traversal.
         * 
         * @throws ConcurrentModificationException if the binary search tree has been
         *                                         modified concurrently with the
         *                                         traversal
         */
        @Override
        public E next() {
            if (version != expectedVersion) {
                throw new ConcurrentModificationException("Collection was modified during iteration.");
            }

            return null;
        }
    }

    /**
     * Provides a postorder traversal for the binary search tree.
     * 
     * @author Ishan Pranav
     */
    private class BSTPostorderIterator implements Iterator<E> {
        private final int expectedVersion = version;

        /**
         * Initializes a new instance of the {@link BSTPostorderIterator} class.
         */
        public BSTPostorderIterator() {
        }

        /**
         * Returns {@code true} if the traversal has more elements.
         * 
         * @return {@code true} if the traversal has more elements.
         */
        @Override
        public boolean hasNext() {
            return false;
        }

        /**
         * Returns the next element in the traversal.
         * 
         * @throws ConcurrentModificationException if the binary search tree has been
         *                                         modified concurrently with the
         *                                         traversal
         */
        @Override
        public E next() {
            if (version != expectedVersion) {
                throw new ConcurrentModificationException("Collection was modified during iteration.");
            }

            return null;
        }
    }

    private int count;
    private int top;
    private int version;
    private Object root;

    /**
     * Constructs a new, empty tree, sorted according to the natural ordering of its
     * elements.
     */
    public BST() {
    }

    /**
     * Constructs a new tree containing the elements in the specified collection,
     * sorted according to the natural ordering of its elements.
     */
    public BST(E[] collection) {
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
        version++;
        return false;
    }

    /**
     * Removes the specified element from this tree if it is present.
     * 
     * More formally, removes an element e such that Objects.equals(o, e), if this
     * tree contains
     * such an element. Returns true if this tree contained the element (or
     * equivalently, if this tree changed as a result of the call). (This tree will
     * not contain the element once the call returns.)
     * 
     * @param o object to be removed from this set, if present
     * @return {@code true} if this set contained the specified element
     * @throws ClassCastException   if the specified object cannot be compared with
     *                              the elements currently in this tree
     * @throws NullPointerException if the specified element is null
     */
    public boolean remove(Object o) {
        version++;
        return false;
    }

    /**
     * Removes all of the elements from this set.
     * 
     * The set will be empty after this call returns.
     * 
     * This operation should be O(1).
     */
    public void clear() {
        version++;
    }

    /**
     * Returns true if this set contains the specified element.
     * 
     * More formally, returns true if and only if this set contains an element e
     * such that Objects.equals(o, e).
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
        return top - 1;
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

        return null;
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

        return null;
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

        return null;
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

        return null;
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

        return null;
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

        return null;
    }

    /**
     * Compares the specified object with this tree for equality.
     * 
     * Returns {@code true} if the given object is also a tree, the two trees have
     * the same
     * size, and every member of the given tree is contained in this tree.
     * 
     * This operation should be O(N).
     * 
     * @param obj object to be compared for equality with this tree
     * @return {@code true} if the specified object is equal to this tree
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BST)) {
            return false;
        }

        final BST bst = (BST) obj;

        if (bst.count != count) {
            return false;
        }

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

            if (iterator.hasNext()) {
                result.append(", ");

                hasNext = true;
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
        return null;
    }
}
