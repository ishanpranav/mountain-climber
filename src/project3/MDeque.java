package project3;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

/**
 * <p>
 * A linear collection that supports element insertion and removal at three
 * points: front, middle and back. The name <em>mdeque</em> is short for "double
 * ended queue" (deque) with <em>m</em> for "middle" and is pronounced
 * "em-deck". MDeque has no fixed limits on the number of elements it contains.
 * </p>
 * 
 * <p>
 * The remove operations all return null values if the mdeque is empty. The
 * structure does not allow null as an element.
 * </p>
 * 
 * <p>
 * All {@code pop...}, {@code push...}, and {@code peek...} operations (from all
 * three points of access) are constant time operations.
 * </p>
 * 
 * <p>
 * The <em>middle</em> position is defined as (size+1)/2 when inserting an
 * element in the <em>middle</em> and as (size/2) when retrieving an element
 * from the <em>middle</em>. The position count is zero based.
 * </p>
 * 
 * {@code [A, B, C, D] -- middle element is C, insert at middle would add at index 2
 * (between B and C).}
 * {@code [A, B, C, D, E] -- middle element is C, insert at middle would add at index 3
 * (between C and D).}
 * 
 * @param <E> the type of elements held in this mdeque
 * 
 * @author Ishan Pranav
 * @author Joanna Klukowska
 */
public class MDeque<E> implements Iterable<E> {

    /**
     * Provides a node for the mdeque's linked list.
     * 
     * @author Ishan Pranav
     */
    private class MDequeNode {
        private E value;
        private MDequeNode next;
        private MDequeNode previous;

        /**
         * Initializes a new instance of the {@link MDequeNode} class.
         * 
         * @param value The node data.
         */
        public MDequeNode(E value) {
            this.value = value;
        }

        /**
         * Renders a node unusuable by clearing all outstanding references. This method
         * prepares the node garbage disposal by ensuring that it does not maintain
         * references to client objects (such as its {@code value}).
         */
        public void invalidate() {
            value = null;
            next = null;
            previous = null;
        }
    }

    /**
     * Provides a sequential (front-to-back) iterator for the mdeque.
     * 
     * @author Ishan Pranav
     */
    private class MDequeIterator implements Iterator<E> {
        private final int expectedVersion = version;

        private MDequeNode current = head;

        /** 
         * Returns {@code true} if the iteration has more elements.
         * 
         * @return {@code true} if the iteration has more elements.
         */
        @Override
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next element in the iteration.
         * 
         * @throws ConcurrentModificationException if the mdeque has been modified
         *                                         concurrently with the iteration
         */
        @Override
        public E next() {
            if (version != expectedVersion) {
                throw new ConcurrentModificationException("Collection was modified during iteration.");
            } else {
                final MDequeNode result = current;

                current = current.next;

                return result.value;
            }
        }
    }

    /**
     * Provides a reverse (back-to-front) iterator for the mdeque.
     * 
     * @author Ishan Pranav
     */
    private class MDequeReverseIterator implements Iterator<E> {
        private final int expectedVersion = version;

        private MDequeNode current = tail;

        /** 
         * Returns {@code true} if the iteration has more elements.
         * 
         * @return {@code true} if the iteration has more elements.
         */
        @Override
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next element in the iteration.
         * 
         * @throws ConcurrentModificationException if the mdeque has been modified
         *                                         concurrently with the iteration
         */
        @Override
        public E next() {
            if (version != expectedVersion) {
                throw new ConcurrentModificationException("Collection was modified during iteration.");
            } else {
                final MDequeNode result = current;

                current = current.previous;

                return result.value;
            }
        }
    }

    private int count;
    private int version;
    private MDequeNode head;
    private MDequeNode body;
    private MDequeNode tail;

    /**
     * Returns the number of elements in this mdeque.
     * 
     * @return the number of elements in this mdeque.
     */
    public int size() {
        return count;
    }

    /**
     * Retrieves the first element of this mdeque.
     * 
     * @return the front of this mdeque, or {@code null} if this mdeque is empty
     */
    public E peekFront() {
        if (head == null) {
            return null;
        } else {
            return head.value;
        }
    }

    /**
     * Retrieves the middle element of this mdeque.
     * 
     * @return the middle of this mdeque, or {@code null} if this mdeque is empty
     */
    public E peekMiddle() {
        if (body == null) {
            return null;
        } else {
            return body.value;
        }
    }

    /**
     * Retrieves the back element of this mdeque.
     * 
     * @return the back of this mdeque, or {@code null} if this mdeque is empty
     */
    public E peekBack() {
        if (tail == null) {
            return null;
        } else {
            return tail.value;
        }
    }

    /**
     * Creates an empty MDeque object.
     */
    public MDeque() {
    }

    /**
     * Initializes the linked list with a single node.
     * 
     * @param node The initial node.
     */
    private void initialize(MDequeNode node) {
        head = node;
        body = node;
        tail = node;
        count = 1;
        version++;
    }

    /**
     * Inserts the specified item at the front of this mdeque.
     * 
     * @param item the element to add
     * @throws IllegalArgumentException if {@code item} is {@code null}
     */
    public void pushFront(E item) {
        if (item == null) {
            throw new IllegalArgumentException("Value cannot be null. Argument name: item.");
        } else {
            final MDequeNode node = new MDequeNode(item);

            if (head == null) {
                // Create a single-element list

                initialize(node);
            } else {
                // List:             $[head] ...

                node.next = head;

                // List:  [node] --> $[head] ...

                head.previous = node;

                // List:  [node] <-> $[head] ...

                head = node;

                // List: $[node] <->  [head] ...

                if (count % 2 == 0) {
                    // When adding to an even-length list, shift the body forward
                    // to represent the exact center of the now-odd-length list
    
                    body = body.previous;
                }

                count++;
                version++;
            }
        }
    }

    /**
     * Inserts a new node to the linked list in the position preceding an existing
     * node.
     * 
     * @param newNode      the new node, inserted after the existing node
     * @param existingNode the existing node, after which the new node is inserted
     */
    private void addBefore(MDequeNode newNode, MDequeNode existingNode) {
        // List:
        // ... [existingNode.previous] <-> [existingNode] <-> [existingNode.next] ...

        newNode.next = existingNode;

        // Lists:
        //                                 [newNode] --> [existingNode] <-> [existingNode.next] ...
        // ... [existingNode.previous] <---------------> [existingNode] <-> [existingNode.next] ...

        newNode.previous = existingNode.previous;

        // Lists:
        // ... [existingNode.previous] <-- [newNode] --> [existingNode] <-> [existingNode.next] ...
        // ... [existingNode.previous] <---------------> [existingNode] <-> [existingNode.next] ...

        existingNode.previous.next = newNode;

        // Lists:
        // ... [existingNode.previous] <-> [newNode] --> [existingNode] <-> [existingNode.next] ...
        // ... [existingNode.previous] <---------------- [existingNode] <-> [existingNode.next] ...

        existingNode.previous = newNode;
        
        // List:
        // ... [existingNode.previous] <-> [newNode] <-> [existingNode] <-> [existingNode.next] ...

        count++;
        version++;
    }

    /**
     * Inserts the specified item in the middle of this mdeque.
     * 
     * @param item the element to add
     * @throws IllegalArgumentException if {@code item} is {@code null}
     */
    public void pushMiddle(E item) {
        if (item == null) {
            throw new IllegalArgumentException("Value cannot be null. Argument name: item.");
        } else {
            final MDequeNode node = new MDequeNode(item);

            if (body == null) {
                // Create a single-element list

                initialize(node);
            } else {
                if (count % 2 == 0) {
                    // When adding to an even-length list, add the new center before
                    // the current body to represent the exact center of the
                    // now-odd-length list

                    addBefore(node, body);
                } else if (body.next == null) {
                    // If there is one element and the middle is the tail

                    pushBack(node);
                } else {
                    // When adding to an odd-length list, add the new center after the
                    // current body to represent the rough center of the now-even-length
                    // list

                    addBefore(node, body.next);
                }

                body = node;
            }
        }
    }

    /**
     * Inserts the specified node at the back of this mdeque.
     * 
     * @param node The node.
     */
    private void pushBack(MDequeNode node) {
        // List: ... [tail]$

        tail.next = node;

        // List: ... [tail]$ -> [node]

        node.previous = tail;
        
        // List: ... [tail]$ <-> [node]

        tail = node;

        // List: ... [tail]  <-> [node]$

        count++;
        version++;
    }

    /**
     * Inserts the specified item at the back of this mdeque.
     * 
     * @param item the element to add
     * @throws IllegalArgumentException if {@code item} is {@code null}
     */
    public void pushBack(E item) {
        if (item == null) {
            throw new IllegalArgumentException("Value cannot be null. Argument name: item.");
        } else {
            final MDequeNode node = new MDequeNode(item);

            if (tail == null) {
                // Create a single element list
                
                initialize(node);
            } else {
                pushBack(node);
            }

            if (count % 2 == 0) {
                // When adding to an even-length list, shift the body reference
                // backward to represent the exact center of the now-odd-length list
                
                body = body.next;
            }
        }
    }

    /**
     * Retrieves and removes the first element of this mdeque.
     * 
     * @return the front of this mdeque, or {@code null} if this mdeque is empty
     */
    public E popFront() {
        if (head == null) {
            return null;
        } else if (head.next == null) {
            // Truncate a single-element list

            return clear();
        } else {
            final E result = head.value;
            final MDequeNode removed = head;

            // List: $[removed] <->  [removed.next] ...

            head = head.next;

            // List:  [removed] <-> $[removed.next] ...

            head.previous = null;

            // List:  [removed] --> $[removed.next] ...

            if (count % 2 == 1) {
                // When removing from an odd-length list, shift the body reference
                // backward to represent the rough center of the now-even-length
                // list

                body = body.next;
            }

            count--;
            version++;

            removed.invalidate();

            // List:               $[removed.next] ...

            return result;
        }
    }

    /**
     * Retrieves and removes the middle element of this mdeque.
     * 
     * @return the middle of this mdeque, or {@code null} if this mdeque is empty
     */
    public E popMiddle() {
        if (body == null) {
            // Empty case

            return null;
        } else if (body.previous == null) {
            // If the middle is the head

            return popFront();
        } else if (body.next == null) {
            // If the middle is the tail

            return popBack();
        } else {
            final E result = body.value;
            final MDequeNode removed = body;

            // List:
            // ... [body.previous] <-> [body] <-> [body.next] ...

            body.previous.next = body.next;
            
            // Lists:
            // ... [body.previous] <-- [body] <-> [body.next] ...
            // ... [body.previous] -------------> [body.next] ...

            body.next.previous = body.previous;

            // Lists:
            // ... [body.previous] <-- [body] --> [body.next] ...
            // ... [body.previous] <------------> [body.next] ...

            if (count % 2 == 0) {
                // When removing from an even-length list, shift the body reference
                // forward to represent the exact center of the now-odd-length list

                body = body.previous;
            } else {
                // When removing from an odd-length list, shift the body reference
                // backward to represent the rough center of the now-even-length list

                body = body.next;
            }

            count--;
            version++;

            removed.invalidate();

            // List:
            // ... [body.previous] <------------> [body.next] ...

            return result;
        }
    }

    /**
     * Retrieves and removes the back element of this mdeque.
     * 
     * @return the back of this mdeque, or {@code null} if this mdeque is empty
     */
    public E popBack() {
        if (tail == null) {
            return null;
        } else if (body.previous == null) {
            // Truncate a single-element list

            return clear();
        } else {
            final E result = tail.value;
            final MDequeNode removed = tail;

            // List: ... [removed.previous]  <-> [removed]$

            tail = tail.previous;

            // List: ... [removed.previous]$ <-> [removed]

            tail.next = null;

            // List: ... [removed.previous]$ <-- [removed]

            if (count % 2 == 0) {
                // When removing from an even-length list, shift the body reference
                // forward to represent the exact center of the now-odd-length list

                body = body.previous;
            }

            count--;
            version++;

            removed.invalidate();

            // List: ... [removed.previous]$

            return result;
        }
    }

    /**
     * <p>
     * Truncates the mdeque's linked list and returns the first element.
     * </p>
     * 
     * <p>
     * The method runs in constant time. It should not be used to clear a list with
     * more than three elements since it does not iteratively invalidate all of the
     * nodes. The method guarantees that the first, last, and middle elements are
     * invalidated and resets the data structure to its initial state, but does not
     * reset the modification count (version number).
     * </p>
     * 
     * <p>
     * Precondition: the linked list contains between 0 and 3 elements, inclusive.
     * </p>
     * 
     * @return the front of the mdeque before clearing
     */
    private E clear() {
        final E result = head.value;

        head.invalidate();
        body.invalidate();
        tail.invalidate();

        head = null;
        body = null;
        tail = null;
        count = 0;
        version++;

        return result;
    }

    /**
     * Returns an iterator over the elements in this mdeque in proper sequence. The
     * elements will be returned in order from front to back.
     * 
     * @return an iterator over the elements in this mdeque in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new MDequeIterator();
    }

    /**
     * Returns an iterator over the elements in this mdeque in reverse sequential
     * order. The elements will be returned in order from back to front.
     * 
     * @return an iterator over the elements in this mdeque in reverse sequence
     */
    public Iterator<E> reverseIterator() {
        return new MDequeReverseIterator();
    }

    /**
     * Returns a string representation of this mdeque.
     * 
     * The string representation consists of a list of the collection's elements in
     * the order they are returned by its iterator, enclosed in square brackets
     * ({@code "[]"}). Adjacent elements are separated by the characters
     * {@code ", "} (comma and space).
     * 
     * @return a string representation of this mdeque
     */
    @Override
    public String toString() {
        // Implementation restriction: this method must be implemented using recursion

        // The "String toString()" method wraps the recursive
        // "void toString(StringBuilder, Iterator<E>)" method

        final StringBuilder result = new StringBuilder();
        final Iterator<E> iterator = iterator();

        result.append('[');

        if (iterator.hasNext()) {
            toString(result, iterator);
        }

        return result
            .append(']')
            .toString();
    }

    /**
     * Recursively prepares a string representation of this mdeque.
     * 
     * @param builder  the string builder
     * @param iterator an iterator over the elements of the mdeque
     */
    private void toString(StringBuilder builder, Iterator<E> iterator) {
        // Append next element

        builder.append(iterator.next());

        if (iterator.hasNext()) {
            builder.append(", ");

            // Recursive case: append remainder

            toString(builder, iterator);
        }

        // Base case: terminate
    }
}
