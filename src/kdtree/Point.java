package kdtree;

import java.util.List;
import java.util.Random;

/**
 * Future update will make it generics
 */
public class Point {
    /**
     * Can be changed to text file instead to read statistics
     */
    int id;
    int x;
    int y;
    boolean alive = true;
    int distance = 6;

    // Incubation period at time of infection
    int incubationPeriodMean = 6;
    int incubationRange;
    int incubationDays = 0; // days until incubation period is over

    // Infectious period occurs after incubation (symptoms can occur as early as 2 days before incubation)
    int preSymptomatic = 3; // days before incubation period is over to be contagious
    int infectiousRange;
    int infectiousDays = 0; // days until infectious period starts

    boolean infected;
    double symptomaticPercent = 0.20; // 0.15 are mild, 0.05 are critical
    boolean asymptomatic;
    boolean immune; // not enough concrete evidence for re-infection
    int daysSinceInfection; // 14 day incubation period, 8 - 10 day infectious period (starts 1 to 3 days)
    boolean immunocompromised; //4% mortality rate

    List<Point> nearest;

    Random rand = new Random();

    public Point(int id, int x, int y) { this(id, x, y, false); }

    // There can only be one patient zero per simulation
    public Point(int id, int x, int y, boolean infected) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.infected = infected;

        incubationRange = rand.nextInt(12) + 3; // 3 - 14 days
        infectiousRange = rand.nextInt(3) + 8; // 8 - 10 days
        immunocompromised = rand.nextInt(1000) <= 36; // Approximately 3.6% of the population is immunocompromised
    }

    /**
     * Returns the distance between this point and other point.
     */
    public double distance(Point other) {
        double distX = Math.pow(other.x - this.x, 2);
        double distY = Math.pow(other.y - this.y, 2);
        return Math.sqrt(distX + distY);
    }

    /**
     * Stores the list of points that within the specified distance for infection.
     */
    public void nearest(List<Point> points) {
        this.nearest = points;
    }

    /**
     * Updates the point for the next tick. Only infected and symptomatic points can sneeze and spread the infection.
     * Points can be infected if they have an early infectious period or after the incubation period is over.
     * Points cannot be re-infected and will have immunity after the infectious period is over. However points have a
     * small chance to die due to severe infection or immunocompromised.
     * Each update can move the point 1 unit in each direction.
     *
     * @param positions is a list of the current coordinates for all points
     */
    public void update(List<Point> positions, int[][] updatedPositions) {
        nearest(positions);
        if (infected && alive) {
            if (!asymptomatic) {
                sneeze();
            }
            daysSinceInfection += 1;
            if (daysSinceInfection > incubationDays + incubationRange) {
                daysSinceInfection = 0;
                infected = false;
                immune = true;

                if (immunocompromised || Math.random() < 0.04) {
                    alive = false;
                }
            }
        }
        move(updatedPositions);
    }

    /**
     * Points will attempt to avoid occupying the same coordinates as other points. If cannot find a spot after
     * 10 updates, the point will remain in the same spot. Points cannot update to negative coordinates.
     *
     * @param updatedPositions contains the list of points and their future position.
     */
    private void move(int[][] updatedPositions) {
        if (!alive) { //Temporarily moves the point to (-1, -1) until it can be removed fully
            updatedPositions[this.id][0] = -1;
            updatedPositions[this.id][1] = -1;
            return;
        }

        boolean validPlacement = false;
        int newX = this.x;
        int newY = this.y;
        int count = 0;

        // Loops for maximum of 10 attempts, else stay in the same spot
        while (!validPlacement) {
            newX = this.x;
            newX = Math.max(0, newX + rand.nextInt(3) - 1); // move -1 to 1, no negative coordinates
            newY = this.y;
            newY = Math.max(0, newY + rand.nextInt(3) - 1); // move -1 to 1, no negative coordinates

            validPlacement = true;
            for (Point point : nearest) {
                if (updatedPositions[point.id][0] == newX && updatedPositions[point.id][1] == newY) {
                    validPlacement = false;
                    break;
                }
            }
            if (!validPlacement && count == 10) {
                newX = this.x;
                newY = this.y;
                validPlacement = true;
            }
        count += 1;
        }

        this.x = newX;
        this.y = newY;
        updatedPositions[this.id][0] = this.x;
        updatedPositions[this.id][1] = this.y;
    }

    /**
     * An infected and symptomatic point can sneeze and spread the infection only during the infectious period.
     */
    private void sneeze() {
        if (daysSinceInfection >= infectiousDays || daysSinceInfection <= incubationDays + incubationRange) {
            for (Point point : nearest) {
                if (point.infected) {
                    point.infect(0.5);
                }
            }
        }
    }

    /**
     * If a nearby infected point sneezes, probability of this point of becoming infected. A point will have varying
     * incubation period based on the given average, as well as varying infectious period. Infectious period can start
     * earlier before the incubation period is over and can last longer if so.
     */
    private void infect(double probability) {
        if (Math.random() < probability && !immune) { //Probability decreases with other factors
            infected = true;
            immune = true;

            if (Math.random() < symptomaticPercent && !immunocompromised) {
                asymptomatic = true;
            }

            daysSinceInfection = 0;
            for (int i = 0; i < 2; i++) { // weighted towards the mean 6-7 days incubation period
                incubationDays += rand.nextInt(incubationPeriodMean + 1);
            }
            incubationDays = Math.max(3, incubationDays); // minimum 3 days incubation

            //infectious period can occur during incubation and will last until incubation is over + additional days
            infectiousDays = incubationDays - rand.nextInt(preSymptomatic + 1);
        }
    }

    @Override
    public String toString() { return String.format("Point x: %d, y: %d", x, y); }
}
