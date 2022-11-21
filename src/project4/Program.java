package project4;

import java.util.TreeSet;

public class Program {
    private static BST<Character> bst = new BST<Character>();
    private static TreeSet<Character> set = new TreeSet<Character>();

    public static void main(String[] args) {
        char[] elements = new char[] { 'K', 'D', 'P', 'B', 'J', 'M', 'A', 'L', 'O', 'N' };
        
        for (char e : elements) {
            add(e);
            // display();
        }

        System.out.println(bst.toStringTreeFormat());

        System.out.println(bst.height());

        //display();

        System.out.print('[');

        for (int i = 0; i < elements.length; i++) {
            System.out.print(bst.get(i));

            if (i < elements.length - 1) {
                System.out.print(", ");
            }
        }

        System.out.println(']');

        // for (char e : elements) {
        // remove(e);
        // display();
        // }
    }

    private static void add(Character c) {
        bst.add(c);
        set.add(c);
    }

    private static void remove(Character c) {
        bst.remove(c);
        set.remove(c);
    }

    private static void display() {
        System.out.print("SET: ");
        //System.out.println(set);
        System.out.print("BST: ");
        //System.out.print(bst);
        System.out.println("; BST[1] = " + bst.first() + "; BST[-1] = " + bst.last());
    }
}
