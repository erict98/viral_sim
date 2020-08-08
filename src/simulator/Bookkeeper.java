package simulator;

import kdtree.KDTree;
import kdtree.Point;
import kdtree.PointSet;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Responsible for generating and keeping track of all the points in the simulation
 */
public class Bookkeeper {
    public  final int     x;
    public  final int     y;
    private final Point[] id; // Each index represents the unique id for any given Point
    public  final String  diseaseName;

    private final List<Point> active;             // Keeps track of active points
    private final PointSet    set = new KDTree(); // Stores the location of the active point
    private int               numberOfUpdates;    // Number of updates that have occurred for this simulation
    private final int         nextDay = 6;        // Number of updates required for the next day to proceed

    Random rand = new Random();

    /**
     * Keeps track of the points in this simulation. Each point will be given an unique id.
     */
    public Bookkeeper(int numberOfPoints, int x, int y, String diseaseName) {
        this.x = x;
        this.y = y;
        this.diseaseName = diseaseName;
        active = new LinkedList<>();

        id = new Point[numberOfPoints];
        Point point = new Point(0, rand.nextInt(x), rand.nextInt(y), true, diseaseName);
        id[0] = point;
        active.add(point);
        for (int i = 1; i < numberOfPoints; i++) {
            point = new Point(i, rand.nextInt(x), rand.nextInt(y), diseaseName);
            id[i] = point;
            active.add(point);
        }
    }

    public Point[] id() { return id; }

    /**
     * Updates the list of currently active points and their list of nearby points.
     */
    public void updateSimulation() {
        if (numberOfUpdates % nextDay == 0) {
            id[0].updateDay();
        }

        List<Point> infected = new LinkedList<>();
        Iterator<Point> itr = active.iterator();
        while (itr.hasNext()) { // Removes all non-active or immune Points
            Point p = itr.next();
            if (p.infected()) {
                infected.add(p);
            }
            if (!p.alive() || p.immune()) {
                itr.remove();
            }
        }

        set.reset(active);             // Resets the PointSet for all non-immune, active points
        for (Point point : infected) { // Updates the list of nearest points for each infected point
            point.infectious(set.nearest(point, point.distance()));
        }

        for (Point point : id) {
            point.move();
        }
        numberOfUpdates += 1;
    }
}
