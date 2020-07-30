import kdtree.Point;

import java.util.Random;

/**
 * Responsible for generating and keeping track of all the points in the simulation
 */
public class Bookkeeper {
    final int x = 100;
    final int y = 100;
    Point[] cluster; // index represents unique id

    /**
     * Keeps track of the points in this simulation. Each point will be given an unique id
     * -1 represents dead
     * @param points
     */
    public Bookkeeper(int points) {
        if (points < 10) {
            throw new IllegalArgumentException("Requires at least 10 participants");
        }
        Random rand = new Random();

        cluster = new Point[points];
        int posX = rand.nextInt(this.x);
        int posY = rand.nextInt(this.y);
        cluster[0] = new Point(posX, posY, true); // patient zero generation

        for (int i = 1; i < points; i++) {
            //TODO
        }
    }

    /**
     * Updates the statuses of all points in the simulation
     */
    public  void updatePoints() {

    }



}