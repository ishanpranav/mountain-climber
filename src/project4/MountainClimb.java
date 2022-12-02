package project4;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * Represents the mountain climber application, responsible for parsing and
 * validating the command line arguments, reading and parsing the
 * input file, producing any error messages, handling any exceptions thrown by
 * other classes, and producing output.
 * 
 * @author Ishan Pranav
 */
public final class MountainClimb {

    /** Initializes a new instance of the {@link MountainClimb} class. */
    private MountainClimb() {
    }

    /**
     * Provides the main entry point for the application.
     * 
     * @param args the command-line arguments to the program; the first element is a
     *             path to the input test file containing instructions to be
     *             processed, and ny other elements are ignored.
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println(
                    "The program requires a path to an input test file as a command-line argument. Argument name: args.");
        }

        try (FileInputStream fileInputStream = new FileInputStream(args[0])) {
            main(fileInputStream, System.out);
        } catch (IOException ioException) {
            System.err.println("An error occured while attempting to parse the test file.");
        }
    }

    /**
     * Provides a platform-agnostic, stream-based entry point for the application.
     * 
     * @param inputStream  the input stream containing instructions to be processed
     * @param outputStream the output stream to which results are printed
     */
    public static void main(InputStream inputStream, PrintStream outputStream) {
        final BSTMountain mountain = new BSTMountain();

        // Parse file line-by-line using a streaming scanner

        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();
                final String[] segments = line.split(" ");
                final int count = segments.length;

                int index = 1;
                int foodRations = 0;
                int rafts = 0;
                int axes = 0;

                // Parse supplies

                while (index < count) {
                    final String supplySegment = segments[index];

                    if (supplySegment.equals("food")) {
                        foodRations++;
                    } else if (supplySegment.equals("raft")) {
                        rafts++;
                    } else if (supplySegment.equals("axe")) {
                        axes++;
                    } else if (supplySegment.equals("river")
                            || supplySegment.equals("fallen")) {
                        break;
                    }

                    index++;
                }

                // Parse obstacles and use lookahead for two-word obstacles
                // Negative rafts represent rivers; negative axes represent fallen trees

                while (index < count) {
                    final String obstacleSegment = segments[index];

                    if (obstacleSegment.equals("river")) {
                        rafts--;
                    } else if (index + 1 < count && obstacleSegment.equals("fallen")
                            && segments[index + 1].equals("tree")) {
                        axes--;
                        index++;
                    }

                    index++;
                }

                mountain.add(new RestStop(segments[0], foodRations, rafts, axes));
            }
        }

        final ArrayMatrix<RestStop> paths = mountain.findPaths();

        // Iterate over the matrix of viable paths, where the rows represent paths and
        // the columns represent steps along those paths

        for (int row = 0; row < paths.getRows(); row++) {
            for (int column = 0; column < paths.getColumns(); column++) {
                outputStream.print(paths.get(row, column));
                outputStream.print(' ');
            }

            outputStream.println();
        }
    }
}
