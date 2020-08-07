package kdtree;

import java.util.List;

public interface PointSet {
    List<Point> nearest(Point p, int distance);

    void reset(List<Point> points);
}
