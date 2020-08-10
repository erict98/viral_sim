package diseases;

import kdtree.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Disease {
    String diseaseName;
    int distance;

    int incubationRange; // Represents the length of incubation period
    int infectiousRange; // Represents the length of infectious period
    int presymptomatic;  // Determines if infectious symptoms show

    double percentContagious; // Probability of showing symptoms
    double percentDeath;      // Probability of showing showing symptoms and dying

    Point point;

    File text;
    Scanner scanner;
    Random rand = new Random();

    public Disease(String disease, Point point) throws FileNotFoundException {
        diseaseName = disease;
        this.point = point;
        text = new File("src/diseases/Diseases.txt");

        scanner = new Scanner(text);
        while (scanner.hasNextLine()) {
            String name = scanner.nextLine();
            if (name.equals(disease)) {
                Scanner info = new Scanner(scanner.nextLine());

                int incubationMean = info.nextInt();
                for (int i = 0; i < 2; i++) {
                    incubationRange += rand.nextInt((incubationMean / 2) + 1);
                }

                int incubationMin = info.nextInt();
                int incubationMax = info.nextInt();
                incubationRange = Math.max(incubationMin, Math.min(incubationMax, incubationRange));

                int infectiousMin = info.nextInt();
                int infectiousMax = info.nextInt();
                infectiousRange = rand.nextInt(infectiousMax - infectiousMin + 1) + infectiousMin;
                presymptomatic = rand.nextInt(info.nextInt() + 1);

                percentContagious = info.nextDouble();
                percentDeath = info.nextDouble();
                distance = info.nextInt();
                break;
            }
        }
    }

    public double contagious() { return percentContagious; }

    public double mortality() { return percentDeath; }

    public int distance() { return distance; }

    public int recoveryDate() { return incubationRange + infectiousRange; }

    /**
     * Default method for spreading diseases. Ignores all conditions besides immunity.
     * An infected and symptomatic point can spread the infection only during the infectious period or presymptomatic.
     */
    public void spread(List<Point> nearest) {
        if ((point.daysSinceInfection() >= incubationRange - presymptomatic) ||
                (point.daysSinceInfection() <= incubationRange + infectiousRange)) {
            for (Point other : nearest) {
                other.getInfected(0.5);
            }
        }
    }
}
