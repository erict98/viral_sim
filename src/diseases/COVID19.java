package diseases;

import kdtree.Point;

import java.io.FileNotFoundException;
import java.util.List;

public class COVID19 extends Disease {

    public COVID19(String disease, Point point) throws FileNotFoundException {
        super("COVID19", point);
    }

    @Override
    public void spread(List<Point> nearest) {
        //TODO affects the age
    }
}
