package kdtree;

import java.util.LinkedList;
import java.util.List;

/**
 * Degenerate KD data structure for testing purposes
 */
public class KDArray implements PointSet {
    private List<Point> root;

    public KDArray(List<Point> points) {
        root = new LinkedList<>();
        root.addAll(points);
    }

    /**
     * Finds all points that are within n distance to point p and adds them to nearest.
     */
    public List<Point> nearest(Point p, int distance) {
        List<Point> nearest = new LinkedList<>();
        for (Point q : root) {
            if (p.distance(q) <= distance && p.id != q.id) {
                nearest.add(q);
            }
        }
        return nearest;
    }
}
