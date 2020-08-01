package kdtree;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class KDTest {
    Random rand = new Random();
    List<Point> cluster;
    PointSet kdtree;
    PointSet kdarray;

    public KDTest() {
        cluster = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            cluster.add(new Point(i, rand.nextInt(100), rand.nextInt(100)));
        }
        kdtree = new KDTree(cluster);
        kdarray = new KDArray(cluster);
    }

    @Test
    public void nearestSize() {
        Point test = cluster.get(0);
        assertEquals(kdtree.nearest(test, test.distance).size(), kdarray.nearest(test, test.distance).size());
    }

    @Test
    public void nearestPoints() {
        Point test = cluster.get(0);
        List<Point> nearestTree = kdtree.nearest(test, test.distance);
        List<Point> nearestArray = kdarray.nearest(test, test.distance);
        List<Integer> treeID = new ArrayList<>();
        List<Integer> arrayID = new ArrayList<>();

        for (Point point : nearestTree) {
            treeID.add(point.id);
        }
        Collections.sort(treeID);
        for (Point point : nearestArray) {
            arrayID.add(point.id);
        }
        Collections.sort(arrayID);

        assertEquals(treeID, arrayID);
    }

    @Test
    public void nearestRepeating() {
        int size = cluster.size();
        for (int i = 0; i < 5; i++) {
            cluster.add(new Point(size + i, 6, 6));
        }
        Point test = cluster.get(cluster.size() - 1);
        List<Point> nearestTree = kdtree.nearest(test, test.distance);
        List<Point> nearestArray = kdarray.nearest(test, test.distance);

        List<Integer> treeID = new ArrayList<>();
        List<Integer> arrayID = new ArrayList<>();

        for (Point point : nearestTree) {
            treeID.add(point.id);
        }
        Collections.sort(treeID);
        for (Point point : nearestArray) {
            arrayID.add(point.id);
        }
        Collections.sort(arrayID);

        assertEquals(treeID, arrayID);
    }

    @Test
    public void nearestDifferentID() {
        int size = cluster.size();
        for (int i = 0; i < 5; i++) {
            cluster.add(new Point(size + i, 6, 6));
        }
        Point test1 = cluster.get(cluster.size() - 1);
        Point test2 = cluster.get(cluster.size() - 2);

        kdtree = new KDTree(cluster);
        kdarray = new KDArray(cluster);

        List<Point> nearestTree = kdtree.nearest(test1, test1.distance);
        List<Point> nearestArray = kdarray.nearest(test2, test2.distance);

        List<Integer> treeID = new ArrayList<>();
        List<Integer> arrayID = new ArrayList<>();

        for (Point point : nearestTree) {
            treeID.add(point.id);
        }
        Collections.sort(treeID);
        for (Point point : nearestArray) {
            arrayID.add(point.id);
        }
        Collections.sort(arrayID);

        assertEquals(treeID.size(), arrayID.size());
        assertNotEquals(treeID, arrayID);
    }
}
