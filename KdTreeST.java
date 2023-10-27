import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class KdTreeST<Value> {
    private int size;
    private KdNode root;

    private class KdNode {
        private Point2D key;
        private Value val;
        private RectHV rect;
        private KdNode left;
        private KdNode right;

        public KdNode(Point2D key, Value val, RectHV rect) {
            this.key = key;
            this.val = val;
            this.rect = rect;
        }
    }

    public KdTreeST() {
        int size = 0;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }

    public void put(Point2D p, Value val) {
        root = put(root, p, val, 0,
                   Double.NEGATIVE_INFINITY,
                   Double.NEGATIVE_INFINITY,
                   Double.POSITIVE_INFINITY,
                   Double.POSITIVE_INFINITY);
    }

    private KdNode put(KdNode x, Point2D p, Value val, int level,
                       double xmin, double ymin, double xmax, double ymax) {

        if (x == null) {
            size++;
            return new KdNode(p, val,
                              new RectHV(xmin, ymin, xmax, ymax));
        }


        // Compare by x
        if (level % 2 == 0) {
            double cmp = p.x() - x.key.x();

            if (cmp < 0) x.left = put(x.left, p, val, level++,
                                      xmin, ymin, x.key.x(), ymax);
            else if (cmp > 0) x.right = put(x.right, p, val, level++,
                                            x.key.x(), ymin, xmax, ymax);
            else x.val = val;
        }
        // Compare by y
        else {
            double cmp = p.y() - x.key.y();

            if (cmp < 0) x.left = put(x.left, p, val, level++,
                                      xmin, ymin, xmax, x.key.y());
            else if (cmp > 0) x.right = put(x.right, p, val, level++,
                                            xmin, x.key.y(), xmax, ymax);
            else x.val = val;
        }
        return x;
    }

    public Value get(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        KdNode x = root;
        int level = 0;
        while (x != null) {
            double cmp;
            // even level compare x values
            if (level % 2 == 0) {
                cmp = p.x() - x.key.x();
            }
            // odd level compare y values
            else {
                cmp = p.y() - x.key.y();
            }

            if (cmp < 0) {
                x = x.left;
                level++;
            }
            else if (cmp > 0) {
                x = x.right;
                level++;
            }
            else return x.val;
        }
        return null;
    }

    public boolean contains(Point2D p) {
        if (get(p) != null) return true;
        return false;
    }

    public Iterable<Point2D> points() {
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        Queue<KdNode> q = new Queue<KdNode>();
        q.enqueue(root);
        while (!q.isEmpty()) {
            KdNode c = q.dequeue();
            if (c.left != null) {
                q.enqueue(c.left);
            }
            if (c.right != null) {
                q.enqueue(c.right);
            }
            points.add(c.key);
        }
        return points;
    }

    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> q = new Queue<Point2D>();
        rangeSearch(rect, q, root);
        return q;
    }

    private void rangeSearch(RectHV rect, Queue<Point2D> q, KdNode x) {
        if (x == null) return;
        if (!rect.intersects(x.rect)) return;
        if (rect.contains(x.key))
            q.enqueue(x.key);
        rangeSearch(rect, q, x.left);
        rangeSearch(rect, q, x.right);
    }

    public Point2D nearest(Point2D p) {
        if (size == 0)
            return null;
        return nearestSearch(p, root, root, 0).key;
    }

    private KdNode nearestSearch(Point2D target, KdNode x,
                                 KdNode champNode, int level) {

        if (x == null) return champNode;
        double champDist = target.distanceSquaredTo(champNode.key);

        double dist = target.distanceSquaredTo(x.key);
        if (champDist > dist) {
            champDist = dist;
            champNode = x;
        }

        boolean goLeft;
        if (level % 2 == 0) {
            if (target.x() < x.key.x())
                goLeft = true;
            else
                goLeft = false;
        }
        else {
            if (target.y() < x.key.y())
                goLeft = true;
            else
                goLeft = false;
        }

        if (goLeft && x.left != null) {
            champNode = nearestSearch(target, x.left,
                                      champNode, level++);
            if (x.right != null && champDist >= x.right.rect.distanceSquaredTo(target)) {
                champNode = nearestSearch(target, x.right, champNode, level++);
            }
        }
        else if (!goLeft && x.right != null) {
            champNode = nearestSearch(target, x.right,
                                      champNode, level++);
            if (x.left != null && champDist >= x.left.rect.distanceSquaredTo(target)) {
                champNode = nearestSearch(target, x.left, champNode, level++);
            }
        }

        return champNode;
    }

    public static void main(String[] args) {
        Point2D one = new Point2D(7, 2);
        Point2D two = new Point2D(5, 4);
        Point2D three = new Point2D(9, 6);
        Point2D four = new Point2D(2, 3);
        Point2D five = new Point2D(4, 7);
        RectHV tenRect = new RectHV(0, 0, 10, 10);
        RectHV fiveRect = new RectHV(0, 0, 5, 5);
        RectHV sevenRect = new RectHV(0, 0, 7, 7);
        KdTreeST<String> test = new KdTreeST<String>();
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

        StdOut.println("Iterate through all points in rect (0,0) , (7,7)");
        for (Point2D c : test.range(sevenRect)) {
            StdOut.print(c + " ");
        }
        StdOut.println();

        StdOut.println("Nearest point to (0,0). [Should be (2,3)]: "
                               + test.nearest(new Point2D(0, 0)));

    }
}
