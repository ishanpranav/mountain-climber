package project4;

/**
 * Represents a two-dimensional array with a fixed number of columns and a
 * variable number of rows. Since it uses a single array buffer, in terms of
 * memory and computation, this data structure is more efficient than a jagged
 * array of arrays, an array-list of array-lists, or an array-list of arrays.
 * 
 * @param <E> the type of elements maintained by this matrix
 * @author Ishan Pranav
 */
public class ArrayMatrix<E> {
    private final int columns;

    private int rows;
    private E[] buffer;

    /**
     * Initializes a new instance of the {@link ArrayMatrix} class.
     * 
     * @param rows    the initial number of rows
     * @param columns the fixed number of columns
     */
    public ArrayMatrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        // A cached empty array avoids allocating mutliple identical array instances

        if (rows == 0) {
            buffer = EmptyArray.instance();
        } else {
            buffer = (E[]) new Object[rows * columns];
        }
    }

    /**
     * Gets the number of rows in the matrix.
     * 
     * @return the number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Gets the number of columns in the matrix.
     * 
     * @return the number of columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Gets the element based on its row and column.
     * 
     * @param row    the zero-based index of the row, between 0 (inclusive) and the
     *               number of rows (exclusive)
     * @param column the zero-based index of the column, between 0 (inclusive) and
     *               the number of columns (exclusive)
     * @return the element at the given row and column
     * @throws IllegalArgumentException if the row or column index is less than
     *                                  zero, the row index is greater than or equal
     *                                  to the number of rows, or the column index
     *                                  is greater than or equal to the number of
     *                                  columns
     */
    public E get(int row, int column) {
        if (row < 0 || row >= rows) {
            throw new IllegalArgumentException(
                    "The zero-based row index must be between 0 (inclusive) and the number of rows (exclusive).");
        }

        if (column < 0 || column >= columns) {
            throw new IllegalArgumentException(
                    "The zero-based column index must be between 0 (inclusive) and the number of columns (exclusive).");
        }

        return buffer[(row * columns) + column];
    }

    /**
     * Adds a row to the matrix.
     * 
     * @param row an array containing the elements to add whose length is equal to
     *            the number of columns
     * @throws IllegalArgumentException if the length of the row is not equal to the
     *                                  number of columns
     */
    public void add(E[] row) {
        if (row.length != columns) {
            throw new IllegalArgumentException("The length of the row must equal the number of columns.");
        }

        final int count = rows * columns;

        int capacity = buffer.length;

        if (count == capacity) {
            // Grow the matrix to satisfy the following constraints
            // 1. The capacity of the new matrix is at least double (to allow the add
            // operation to complete in amortized constant time)
            // 2. The new matrix can accommodate at least the length of the new row

            if (capacity == 0) {
                capacity = count + columns;
            } else {
                capacity *= 2;
            }

            final Object[] newMatrix = new Object[capacity];

            // Include the existing elements

            if (count > 0) {
                System.arraycopy(buffer, 0, newMatrix, 0, count);
            }

            buffer = (E[]) newMatrix;
        }

        // Add the new row

        System.arraycopy(row, 0, buffer, count, columns);

        rows++;
    }
}
