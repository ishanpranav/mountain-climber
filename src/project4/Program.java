package project4;

import java.util.TreeSet;

public class Program {
    private static BST<Character> bst = new BST<Character>();
    private static TreeSet<Character> set = new TreeSet<Character>();
    private static char[] elements = new char[] { 'K', 'D', 'P', 'B', 'J', 'M', 'A', 'L', 'O', 'N' };

    public static void main(String[] args) {

        for (char e : elements) {
            add(e);
            display();
        }

        System.out.println(bst);
        System.out.println(bst.height());
        System.out.println();

        for (char e : elements) {
            remove(e);
            display();
        }
    }

    private static void add(Character c) {
        System.out.print("ADD ");
        System.out.println(c);

        bst.add(c);
        set.add(c);
    }

    private static void remove(Character c) {
        System.out.print("REMOVE ");
        System.out.println(c);
        bst.remove(c);
        set.remove(c);
        System.out.println(bst.toStringTreeFormat());
    }

    private static void display() {
        System.out.print("SET: ");
        System.out.println(set);
        System.out.print("BST: ");
        System.out.println(bst);
        System.out.print("IDX: [");

        for (int i = 0; i < bst.size(); i++) {
            System.out.print(bst.get(i));

            if (i < bst.size() - 1) {
                System.out.print(", ");
            }
        }
        System.out.println(']');

        // System.out.println("; BST[1] = " + bst.first() + "; BST[-1] = " +
        // bst.last());
    }
}
