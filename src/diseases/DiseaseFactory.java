package diseases;

import kdtree.Point;

import java.io.FileNotFoundException;

public class DiseaseFactory {
    private final String disease;
    private final Point p;

    public DiseaseFactory(String disease, Point p) {
        this.disease = disease;
        this.p = p;
    }

    public Disease getDisease() throws FileNotFoundException {
        if (disease == null) {
            return null;
        }

        if (disease.equalsIgnoreCase("TestDisease")) {
            return new TestDisease(disease, p);
        }

        if (disease.equalsIgnoreCase("COVID19")) {
            return new COVID19(disease, p);
        }

        return null;
    }
}
