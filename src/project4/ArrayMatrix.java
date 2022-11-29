package project4;

public class ArrayMatrix<E> {
    private int rows;
    private int columns;
    private E[] matrix = (E[]) EmptyArray.VALUE;

    public ArrayMatrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        if (rows == 0) {
            matrix = (E[]) EmptyArray.VALUE;
        } else {
            matrix = (E[]) new Object[rows * columns];
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public E get(int row, int column) {
        if (row < 0 || row >= rows) {
            throw new IllegalArgumentException(
                    "The zero-based row index must be between 0 (inclusive) and the number of rows (exclusive).");
        }

        if (column < 0 || column >= columns) {
            throw new IllegalArgumentException(
                    "The zero-based column index must be between 0 (inclusive) and the number of columns (exclusive).");
        }

        return matrix[row * columns + column];
    }

    public void add(E[] row) {
        if (row.length != columns) {
            throw new IllegalArgumentException("The length of the row must equal the number of columns.");
        }

        final int capacity = matrix.length;
        final int count = rows * columns;

        if (count == capacity) {
            final Object[] newMatrix = new Object[Math.max(4, Math.max(capacity * 2, count + columns))];

            if (count > 0) {
                System.arraycopy(matrix, 0, newMatrix, 0, count);
            }

            matrix = (E[]) newMatrix;
        }

        System.arraycopy(row, 0, matrix, count, columns);

        rows++;
    }
}
