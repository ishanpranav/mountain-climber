package project4;

public class Program {
    public static void main(String[] args) {

        BST<Integer> e = new BST<Integer>(new Integer[] {
                81, 45, 40, 36, 38, 66, 71, 14, 92, 46, 87, 52, 51, 16, 39, 49, 84, 57, 10, 23, 91, 1, 73, 70, 90, 83,
                76, 68, 80, 65, 54, 13, 11, 79, 74, 69, 75, 42, 34, 93, 25, 3, 78, 6, 21, 19, 59, 24, 12, 67, 94, 58,
                98, 43, 44, 97, 88, 15, 33, 41, 100, 77, 99, 8, 61, 86, 96, 28, 5, 29, 89, 7, 27, 64, 9, 35, 72, 30, 50,
                95, 60, 48, 85, 20, 22, 26, 4, 2, 17, 55, 37, 63, 82, 32, 62, 31, 53, 18, 47, 56
        });

        System.out.println(e.toStringTreeFormat());
    }
}
