package project4;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Represents the mountain climber application, responsible for parsing and
 * validating the command line arguments, reading and parsing the
 * input file, producing any error messages, handling any exceptions thrown by
 * other classes, and producing output.
 * 
 * @author Ishan Pranav
 */
public class MountainClimb {

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
            main(fileInputStream);
        } catch (IOException ioException) {
            System.err.println("An error occured while attempting to parse the test file.");
        }
    }

    /**
     * Provides a platform-agnostic, stream-based entry point for the application.
     * 
     * @param inputStream the input stream containing instructions to be processed
     */
    public static void main(InputStream inputStream) {
        final BSTMountain mountain = new BSTMountain();

        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();
                final String[] segments = line.split(" ");
                final int count = segments.length;

                int index = 1;
                int foodRations = 0;
                int rafts = 0;
                int axes = 0;
                int rivers = 0;
                int fallenTrees = 0;

                while (index < count) {
                    final String supplySegment = segments[index];

                    if (supplySegment.equals("food")) {
                        foodRations++;
                    } else if (supplySegment.equals("raft")) {
                        rafts++;
                    } else if (supplySegment.equals("axe")) {
                        axes++;
                    } else {
                        break;
                    }

                    index++;
                }

                while (index < count) {
                    final String obstacleSegment = segments[index];

                    if (obstacleSegment.equals("river")) {
                        rivers++;
                        index++;
                    } else if (index + 1 < count && obstacleSegment.equals("fallen")
                            && segments[index + 1].equals("tree")) {
                        fallenTrees++;
                        index += 2;
                    }
                }

                mountain.add(new RestStop(segments[0], foodRations, rafts, axes, rivers, fallenTrees));
            }
        }
    }
}
