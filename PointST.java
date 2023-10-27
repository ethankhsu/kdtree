import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class PointST<Value> {
    private RedBlackBST<Point2D, Value> st;

    // construct an empty symbol table of points
    public PointST() {
        st = new RedBlackBST<Point2D, Value>();
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return st.isEmpty();
    }

    // number of points
    public int size() {
        return st.size();
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new IllegalArgumentException("Argument is null");
        st.put(p, val);
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        return st.get(p);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        return st.contains(p);
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        return st.keys();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Argument is null");
        }

        ArrayList<Point2D> inside = new ArrayList<Point2D>();
        for (Point2D c : st.keys()) {
            if (rect.contains(c)) {
                inside.add(c);
            }
        }
        return inside;
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (st.size() == 0)
            return null;
        double minDist = Double.POSITIVE_INFINITY;
        Point2D close = new Point2D(0, 0);

        for (Point2D c : st.keys()) {
            double distSq = p.distanceSquaredTo(c);
            if (minDist > Math.sqrt(distSq)) {
                minDist = Math.sqrt(distSq);
                close = c;
            }
        }
        return close;
    }


    // unit testing (required)
    public static void main(String[] args) {
        Point2D one = new Point2D(7, 2);
        Point2D two = new Point2D(5, 4);
        Point2D three = new Point2D(9, 6);
        Point2D four = new Point2D(2, 3);
        Point2D five = new Point2D(4, 7);
        RectHV tenRect = new RectHV(0, 0, 10, 10);
        RectHV fiveRect = new RectHV(0, 0, 5, 5);
        RectHV sevenRect = new RectHV(0, 0, 7, 7);
        PointST<String> test = new PointST<String>();
        test.put(one, "a");
        test.put(two, "b");
        test.put(three, "c");
        test.put(four, "d");
        test.put(five, "e");
        boolean sizeTest = (test.size() == 5);
        StdOut.println("Size should be 5: " + sizeTest);

        StdOut.println("Point two should contain b: "
                               + test.get(two).equals("b"));

        StdOut.println("Point four should contain d: "
                               + test.get(four).equals("d"));

        StdOut.println("Test should contain Point four: "
                               + test.contains(four));

        Point2D six = new Point2D(11, 11);

        StdOut.println("Test shouldn't contain Point six: "
                               + test.contains(six));

        StdOut.println("Iterate through all points");
        for (Point2D c : test.points()) {
            StdOut.print(c + " ");
        }
        StdOut.println();

        StdOut.println("Iterate through all points in rect (0,0) , (10,10)");
        for (Point2D c : test.range(tenRect)) {
            StdOut.print(c + " ");
        }
        StdOut.println();

        StdOut.println("Iterate through all points in rect (0,0) , (5,5)");
        for (Point2D c : test.range(fiveRect)) {
            StdOut.print(c + " ");
        }
        StdOut.println();

        StdOut.println("Nearest point to (0,0). [Should be (2,3)]: "
                               + test.nearest(new Point2D(0, 0)));

    }
}
