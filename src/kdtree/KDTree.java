package kdtree;

import java.util.LinkedList;
import java.util.List;

public class KDTree {
    private KDNode root;

    /**
     * Instantiates a new KDTree with the given points. Assumes the list of points are non-overlapping.
     *
     */
    public KDTree(List<Point> points) {
        for (Point point : points) {
           root = add(root, point, true);
        }
    }

    private KDNode add(KDNode node, Point other, boolean leftRight) {
        if (node == null) { return new KDNode(other); } // makes a new KDTree if empty

        if (leftRight) { // split vertically
            if (node.point.x > other.x) { // add to left branch
                node.left = add(node.left, other, false);
            } else { // add to the right branch
                node.right = add(node.right, other, false);
            }
        } else { // split horizontally
            if (node.point.y > other.y) { // add to the down branch
                node.down = add(node.down, other, true);
            } else { // add to the up branch
                node.up = add(node.up, other, true);
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

    private void nearestNode(KDNode node, Point p, List<Point> points, int distance, boolean leftRight) {
        if (node == null) {
            return;
        }
        KDNode search; // Search based on partitioning
        KDNode potential; // Search other branch if it may contain points that are within 6 units

        Point q = node.point;
        if (p.distance(q) <= distance) {
            points.add(q);
        }

        // Establishes which branch is objectively the best choice and which branch has potential
        if (leftRight) { // Compare along the x-coordinates
            if (p.x < q.x) { // Search the left subtree
                search = node.left;
                potential = node.right;
            } else { // Search the right subtree
                search = node.right;
                potential = node.left;
            }
        } else { // Compare along the y-coordinates
            if (p.y < q.y) { // Search the down subtree
                search = node.down;
                potential = node.up;
            } else { // Search the up subtree
                search = node.up;
                potential = node.down;
            }
        }

        nearestNode(search, p, points, distance, !leftRight); // Automatically searches the objectively best branch
        if (leftRight && p.distance(new Point(q.x, p.y)) <= distance) { // Checks if the horizontal partition has potential
            nearestNode(potential, p, points, distance, false);
        } else if (!leftRight && p.distance(new Point(p.x, q.y)) <= distance) { // Checks if the vertical partition has potential
            nearestNode(potential, p, points, distance, true);
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
