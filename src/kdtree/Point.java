package kdtree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Future update will make it generics
 */
public class Point {
    List<Point> nearest;
    boolean masked; // temporarily unavailable
    boolean infected;
    boolean immunocompromised;
    int x;
    int y;
    int id;


    public Point(int x, int y) { this(x, y, false); }

    // There can only be one patient zero per simulation
    public Point(int x, int y, boolean infected) {
        this.x = x;
        this.y = y;
        this.infected = infected;

        Random rand = new Random();
        immunocompromised = rand.nextInt(1000) <= 36; // Approximately 3.6% of the population is immunocompromised
    }

    /**
     * Returns the distance between two points.
     */
    public double distance(Point other) {
        double distX = Math.pow(other.x - this.x, 2);
        double distY = Math.pow(other.y - this.y, 2);
        return Math.sqrt(distX + distY);
    }

    /**
     * Generates a list of current points that are within 6 units.
     * For now, all at all points to find the nearest (change to kd tree in future)
     */
    public void generate(List<Point> points) {
        nearest = new ArrayList<>();
        //TODO: create list
    }

    /**
     * Updates the current position of the point. A point can only move 1 unit from its
     * original position or not move at all. Two points cannot occupy the same position and the point that occupies
     * that spot first will be given priority. If another point will occupy its position, then move.
     *
     * Edge cases: point plans to move into a spot of another point that cannot move at all. Give that point priority
     *
     * @param positions is a list of the current coordinates for all points
     */
    public void update(List<Point> positions) {
        //TODO:
    }

    /**
     * If currently infected and incubating the virus, countdown until incubation period is over
     */
    private void incubation() {
        //TODO:
    }

    /**
     * An infected point has a probability to spread the virus. All points within 6 units can become infected.
     * Perform binary search (kd-tree) to find points nearest
     */
    public void sneeze() {
        //TODO:
    }

    /**
     * If next to infected point that sneezed, this point can become infected and sets the incubation period
     */
    public void infect() {
        //TODO:
    }

    @Override
    public String toString() { return String.format("Point x: %d, y: %d", x, y); }
}
