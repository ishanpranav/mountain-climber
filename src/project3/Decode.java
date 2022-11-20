package project3;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

/**
 * This program process a sequence of numbers according to given instructions.
 * The sequence of values provided by the user is a comma and space separated
 * list of integers. For example: {@code 12, 43, 189, 42, 1, 35}.
 * The instructions consist of characters 'F', 'B' and 'R':
 * - F - drop the first element of the sequence
 * - B - drop the last element of the sequence
 * - R - reverse the sequence.
 *
 * The program outputs the resulting numerical sequence after the instructions
 * were processed.
 *
 * For example, if the initial sequence is {@code 12, 43, 189, 42, 1, 35} and
 * the instructions are {@code FRB}, the resulting sequence should be
 * {@code [35, 1, 42, 189]}.
 *
 * @author Joanna Klukowska
 * @author Ishan Pranav
 */
public class Decode {

    /**
     * Provides the main entry point for the application.
     * 
     * @param args the command-line arguments to the program.
     * 
     * @author Joanna Klukowska
     * 
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("Enter the sequence to process: ");
        String sequence = in.nextLine();

        System.out.println("Enter the sequence of instructions: ");
        String instructions = in.nextLine();

        if (!isValid(instructions)) {
            System.out.println("ERROR: instruction sequence is invalid.");
            System.exit(3);
        }

        MDeque<Integer> list = null;

        try {
            list = parseSequence(sequence);
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid numerical sequence. ");
            System.exit(1);
        }

        try {
            decode(list, instructions);
        } catch (NoSuchElementException ex) {
            System.out.println("Instructions are invalid for the given sequence.");
            System.exit(2);
        }

        System.out.println("Decoded sequence is: ");
        System.out.println(list);

        in.close();
    }

    /**
     * Decode the sequence represented by the {@code list} mdeque following the
     * {@code instructions}.
     * 
     * @param list         the mdeque with sequence to decode
     * @param instructions instructions to follow to decode the {@code list}
     * @throws NoSuchElementException when the sequence is empty and the next
     *                                instruction is either 'F' or 'B'
     */
    public static void decode(MDeque<Integer> list, String instructions) throws NoSuchElementException {
        // Record the direction of the list

        boolean isListForward = true;

        for (int i = 0; i < instructions.length(); i++) {
            final char instruction = instructions.charAt(i);

            if (instruction == 'R') {
                // Reverse the list

                isListForward = !isListForward;
            } else {
                // Record the direction of the user's pop instruction

                final boolean isInstructionF = instruction == 'F';

                if (isInstructionF || instruction == 'B') {
                    if (list.size() == 0) {
                        throw new NoSuchElementException("Cannot drop from an empty list.");
                    } else if (isListForward == isInstructionF) {
                        // If the list direction and instruction direction match, then pop the front
                        // For example, when dropping the front of a forward list or
                        // when droping the back from a backward list

                        list.popFront();
                    } else {
                        // If the list direction and instruction direction do not match, then pop the
                        // back
                        // For example, when dropping the back of a forward list or
                        // when dropping the front of a backward list

                        list.popBack();
                    }
                }
            }
        }

        // If the list has been reversed, convert it back to its sequential
        // representation

        if (!isListForward) {
            MDeque<Integer> reversedList = new MDeque<Integer>();

            // list: [1, 2, 3, 4, 5, 6 ...]
            // reversedList: []

            for (Integer front = list.popFront(); front != null; front = list.popFront()) {
                // list: [4, 5, 6 ...]

                reversedList.pushFront(front);

                // reversedList: [... 3, 2, 1]
            }

            // list: []
            // reversedList: [... 6, 5, 4, 3, 2, 1]

            for (Integer front = reversedList.popFront(); front != null; front = reversedList.popFront()) {
                // reversedList: [... 3, 2, 1]

                list.pushBack(front);

                // list: [... 6, 5, 4 ...]
            }

            // list: [... 6, 5, 4, 3, 2, 1 ]
            // reversedList: []
        }
    }

    /**
     * Convert a given sequence from string format to mdeque of interger values.
     * 
     * @param sequence string with comma and space separated values
     * @return mdeque with the same values as the ones listed in the
     *         {@code sequence}
     * @throws IllegalArgumentException when the sequence contains values that
     *                                  cannot be converted to a list of integer due
     *                                  to invalid characters or invalid separators
     */
    public static MDeque<Integer> parseSequence(String sequence) throws IllegalArgumentException {
        MDeque<Integer> list = new MDeque<Integer>();

        try {
            String[] splitSequence = sequence.split(", ");

            for (int i = 0; i < splitSequence.length; i++) {
                list.pushBack(Integer.parseInt(splitSequence[i]));
            }

        } catch (PatternSyntaxException ex) {
            System.err.println("THIS SHOULD NOT HAPPEN!");
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("invalid value in the sequence");
        }

        return list;
    }

    /**
     * Determines if the sequence of instructions is valid. A valid sequence
     * consists of characters:
     * 'R', 'F' and 'B' in any order.
     * 
     * @param instructions instruction string
     * @return {@code true} if instructions are valid, {@code false} otherwise
     */
    public static boolean isValid(String instructions) {
        // Implementation restriction: this method must be implemented using recursion

        // The "boolean isValid(String)" method wraps the recursive
        // "boolean isValid(String, int, int)" method

        return isValid(instructions, 0, instructions.length());
    }

    /**
     * <p>
     * Recursively determines if a sequence of instructions is valid.
     * </p>
     * 
     * <p>
     * This method provides the implementation details for the
     * {@code isValid(String)} method of the public interface.
     * </p>
     * 
     * @param instructions instruction string
     * @param offset       the zero-based index from which validation begins
     * @param length       the pre-computed length of the instruction string
     * @return {@code true} if instructions are valid, {@code false} otherwise
     */
    private static boolean isValid(String instructions, int offset, int length) {
        if (offset == length) {
            // Base case: an empty instruction string is valid

            return true;
        } else {
            char instruction = instructions.charAt(offset);

            if (instruction == 'R' || instruction == 'F' || instruction == 'B') {
                // Recursive case: an instruction string whose first character is R,
                // F, or B is valid if the remainder of its characters form a valid
                // instruction string

                return isValid(instructions, offset + 1, length);
            } else {
                // Base case: an instruction string whose first character is neither
                // R, F, nor B is invalid

                return false;
            }
        }
    }
}
