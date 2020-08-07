package kdtree;

import java.util.LinkedList;
import java.util.List;

public class KDTree implements PointSet {
    private KDNode root;

    /**
     * Instantiates a new KDTree with the given points. Assumes the list of points are non-overlapping.
     *
     */
    public KDTree(List<Point> points) {
        /*
        Loop invariant:
        left branch (p.x <= q.x), right branch (p.x > q.x)
        down branch (p.y <= q.y), up branch (p.y > q.y)
         */
        for (Point point : points) {
           root = add(root, point, true);
        }
    }

    public KDTree() {
        this(new LinkedList<Point>());
    }

    private KDNode add(KDNode node, Point p, boolean leftRight) {
        if (node == null) { return new KDNode(p); } // inserts a new node at each leaf node

        Point q = node.point;

        if (leftRight) { // split vertically
            if (p.x() <= q.x()) { // add to left branch
                node.left = add(node.left, p, false);
            } else { // add to the right branch
                node.right = add(node.right, p, false);
            }
        } else { // split horizontally
            if (p.y() <= q.y()) { // add to the down branch
                node.down = add(node.down, p, true);
            } else { // add to the up branch
                node.up = add(node.up, p, true);
            }
        }
        return node;
    }

    /**
     * Returns a list of points that are within n unit distance from the point of interest.
     */
    public List<Point> nearest(Point p, int distance) {
        List<Point> points = new LinkedList<>();
        nearestNode(root, p, points, distance, true);
        return points;
    }

    /**
     * Helper method to find the nearest points. Traverses the objectively best branch first before exploring branches
     * that may contain points that are within n distance.
     */
    private void nearestNode(KDNode node, Point p, List<Point> points, int distance, boolean leftRight) {
        if (node == null) {
            return;
        }
        KDNode search; // Search based on partitioning
        KDNode potential; // Search other branch if it may contain points that are within 6 units

        Point q = node.point;
        if (p.distance(q) <= distance && p.id() != q.id()) { // Cannot add to list if the same point
            points.add(q);
        }

        // Establishes which branch is objectively the best choice and which branch has potential
        if (leftRight) { // Compare along the x-coordinates
            if (p.x() <= q.x()) { // Search the left subtree
                search = node.left;
                potential = node.right;
            } else { // Search the right subtree
                search = node.right;
                potential = node.left;
            }
        } else { // Compare along the y-coordinates
            if (p.y() <= q.y()) { // Search the down subtree
                search = node.down;
                potential = node.up;
            } else { // Search the up subtree
                search = node.up;
                potential = node.down;
            }
        }

        nearestNode(search, p, points, distance, !leftRight); // Automatically searches the objectively best branch
        if (leftRight && p.distance(new Point(q.x(), p.y())) <= distance) { // Checks horizontal partition
            nearestNode(potential, p, points, distance, false);
        } else if (!leftRight && p.distance(new Point(p.x(), q.y())) <= distance) { // Checks vertical partition
            nearestNode(potential, p, points, distance, true);
        }
    }

    public void reset(List<Point> points) {
        root = null;
        for (Point point : points) {
            root = add(root, point, true);
        }
    }

static class KDNode {
        private final Point point;
        KDNode left;
        KDNode right;
        KDNode up;
        KDNode down;

        public KDNode(Point other) {
            this.point = other;
            this.left = null;
            this.right = null;
            this.up = null;
            this.down = null;
        }
    }
}
