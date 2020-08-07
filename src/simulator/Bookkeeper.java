package simulator;

import kdtree.KDTree;
import kdtree.Point;
import kdtree.PointSet;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Responsible for generating and keeping track of all the points in the simulation
 */
public class Bookkeeper {
    public final int x;
    public final int y;
    private Point[] id;              // Index represents unique id
    public final String diseaseName; // Disease

    private List<Point> active;          // Keeps track of active points
    private PointSet set = new KDTree(); // Stores the location of the active points

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

    public List<Point> active() { return new LinkedList<Point>(active); }

    /**
     * Updates the list of currently active points and their list of nearby points.
     */
    public void updateSimulation() {
        active = new LinkedList<>();
        for (Point point : id) { // Updates the list of active points
            if (point.alive()) {
                active.add(point);
            }
        }
        set.reset(active); // Resets the PointSet for all active points

        for (Point point : id) { // Updates the list of nearest points for each point
            if (point.alive()) {
                point.update(set.nearest(point, point.distance()));
            }
        }
    }

    public void updateSimulationTest() {
        List<Point> points = new LinkedList<>();
        for (Point point : id) {
            if (point.alive()) {
                points.add(point);
                System.out.println(point.toString());
            }
        }
        set.reset(points);
        for (Point point : id) {
            if (point.alive()) {
                point.update(set.nearest(point, point.distance()));
            }
        }
    }
}
