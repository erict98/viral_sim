package diseases;

import kdtree.Point;

import java.io.FileNotFoundException;
import java.util.List;

public class TestDisease extends Disease {

    public TestDisease(String disease, Point point) throws FileNotFoundException {
        super("TestDisease", point);
    }

    public void spread(List<Point> nearest) {
        if ((point.daysSinceInfection() >= incubationRange - presymptomatic) ||
                (point.daysSinceInfection() <= incubationRange + infectiousRange)) {
            for (Point other : nearest) {
                other.getInfected(1.0);
            }
        }
    }
}
