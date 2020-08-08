package kdtree;

import diseases.Disease;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

/**
 * A Point class represents an individual with (x,y) coordinates and information pertaining to infection.
 */
public class Point {
    private final int ID; // Each point will have an unique ID
    private int height;
    private int width;
    private int x;
    private int y;

    private boolean alive = true;
    private boolean infected;
    private boolean immune;

    private Disease disease;
    private static int dayCounter = 0;  // Global day tracker
    private int dayInfected = 0;
    private boolean asymptomatic;
    boolean immunocompromised;

    Random rand = new Random();

    public Point(int x, int y) { this(-1, x, y, false, null); }

    public Point(int id, int x, int y, String disease) { this(id, x, y, false, disease); }

    public Point(int id, int x, int y, boolean infected, String disease) {
        this.ID = id;
        this.x = x;
        this.y = y;
        height = 500;
        width = 500;
        this.infected = infected;

        try {
            this.disease = new Disease(disease, this);
        } catch (FileNotFoundException e) {
            System.out.println("Diseases.txt not found");
        }

        if (!infected) {
            immunocompromised = Math.random() <= 0.036; // Approximately 3.6% of the population is immunocompromised
        }
    }

    public int id() { return ID; }

    public int x() { return x; }

    public int y() { return y; }

    public boolean alive() { return alive; }

    public boolean infected() { return infected; }

    public boolean immune() { return immune; }

    public int dayCounter() { return dayCounter; }

    public int dayInfected() { return dayInfected; }

    public int daysSinceInfection() { return dayCounter - dayInfected; }

    public int distance() { return disease.distance(); }


    /**
     * Returns the distance between this Point and other Point.
     */
    public double distance(Point other) {
        double distX = Math.pow(other.x - this.x, 2);
        double distY = Math.pow(other.y - this.y, 2);
        return Math.sqrt(distX + distY);
    }

    public void move() {
        if (alive) {
            x = Math.min(width - 1, Math.max(0, x + rand.nextInt(21) - 10));
            y = Math.min(height - 1, Math.max(0, y + rand.nextInt(21) - 10));
        }
    }

    /**
     * Point can only move 1 unit at a time from (0,0) to (x,y). An infected and symptomatic Point have a probability
     * to spread the disease with each update. After the infectious period is over, symptomatic Points can die according
     * to real-world statistics. Immunocompromised Points will always die after infection.
     */
    public void infectious(List<Point> nearest) {
        if (!asymptomatic) {
            disease.spread(nearest);
        }
        if (dayCounter - dayInfected > disease.recoveryDate()) {
            infected = false;
            immune = true;

            if (immunocompromised || (!asymptomatic && Math.random() <= disease.mortality())) {
                alive = false;
            }
        }
    }

    /**
     * A Point can be infected from nearby Points. If infected, the Point will be determined to be symptomatic or
     * asymptomatic according to real-world statistics. A Point cannot be re-infected.
     */
    public void getInfected(double probability) {
        if (immune || Math.random() >= probability) {
            return;
        }
        infected = true;
        immune = true;
        dayInfected = dayCounter;
        if (!immunocompromised && Math.random() >= disease.contagious()) {
            asymptomatic = true;
        }
    }

    /**
     * BookKeeper will be manually responsible for updating the days
     */
    public void updateDay() {
        dayCounter += 1;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("Point %d: (%d, %d)", ID, x, y));
        if (infected) {
            s.append(" infected");
        }
        return s.toString();
    }
}