package kdtree;

import diseases.Disease;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;

/**
 * A Point class represents an individual with (x,y) coordinates and information pertaining to infection.
 */
public class Point {
    private final int id;
    private final int HEIGHT = 760;
    private final int WIDTH = 1000;
    private int x;
    private int y;

    private boolean alive = true;
    private boolean infected;
    private boolean immune;

    private Disease disease;
    private int daysSinceInfection = 0;
    private boolean asymptomatic;
    boolean immunocompromised; //4% mortality rate

    Random rand = new Random();

    public Point(int x, int y) { this(-1, x, y, false, null); }

    public Point(int id, int x, int y, String disease) { this(id, x, y, false, disease); }

    public Point(int id, int x, int y, boolean infected, String disease) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.infected = infected;
        try {
            this.disease = new Disease(disease, this);
        } catch (FileNotFoundException e) {
            System.out.println("Diseases.txt not found");
        }
        immunocompromised = Math.random() <= 0.036; // Approximately 3.6% of the population is immunocompromised
    }

    public int id() { return id; }

    public int x() { return x; }

    public int y() { return y; }

    public boolean alive() { return alive; }

    public boolean infected() { return infected; }

    public int daysSinceInfection() { return daysSinceInfection; }

    public int distance() { return disease.distance(); }

    /**
     * Returns the distance between this Point and other Point.
     */
    public double distance(Point other) {
        double distX = Math.pow(other.x - this.x, 2);
        double distY = Math.pow(other.y - this.y, 2);
        return Math.sqrt(distX + distY);
    }

    /**
     * Point can only move 1 unit at a time from (0,0) to (x,y). An infected and symptomatic Point have a probability
     * to spread the disease with each update. After the infectious period is over, symptomatic Points can die according
     * to real-world statistics. Immunocompromised Points will always die after infection.
     */
    public void update(List<Point> nearest) {
        if (!alive) {
            return;
        }
        if (infected) {
            if (!asymptomatic) {
                disease.spread(nearest);
            }
            daysSinceInfection += 1;
            if (daysSinceInfection > disease.recoveryDate()) {
                daysSinceInfection = 0;
                infected = false;
                immune = true;

                if (immunocompromised || (!asymptomatic && Math.random() <= disease.mortality())) {
                    alive = false;
                }
            }
        }
        x = Math.min(WIDTH - 1, Math.max(0, x + rand.nextInt(11) - 5));
        y = Math.min(HEIGHT - 1, Math.max(0, y + rand.nextInt(11) - 5));
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
        if (!immunocompromised && Math.random() >= disease.contagious()) {
            asymptomatic = true;
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("Point %d: (%d, %d)", id, x, y));
        if (infected) {
            s.append(" infected");
        }
        return s.toString();
    }
}