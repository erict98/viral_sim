package simulator;

import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import kdtree.Point;

public class Animation {
    private final Timeline timeline;
    private Bookkeeper bk;

    private final Point[] points;
    private final Circle[] circles;
    private final TranslateTransition[] transitions;

    public Animation(int count, int x, int y, String name) {
        this.bk = new Bookkeeper(count, x, y, name);
        this.points = bk.id();
        this.circles = new Circle[points.length];
        this.transitions = new TranslateTransition[points.length];

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];

            int px = p.x();
            int py = p.y();

            Circle c = new Circle(px, py, 3);
            if (!p.alive()) {
                c.setFill(Color.BLACK);
            } else if (p.infected()) {
                c.setFill(Color.web("#ff6961"));
            } else {
                c.setFill(Color.web("#68BBE3"));
            }
            circles[i] = c;

            TranslateTransition t = new TranslateTransition(Duration.millis(300), c);
            transitions[i] = t;
        }

        KeyFrame kf = new KeyFrame(Duration.millis(300), e -> refreshScene());
        timeline = new Timeline(kf, new KeyFrame(Duration.millis(300)));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private Color color() {
        return null;
    }

    public Group group() {
        return new Group(circles);
    }

    public void playScene() { timeline.play(); }

    public void refreshScene() {
        bk.updateSimulation();
        for (int i = 0; i < circles.length; i++) {
            Point p = points[i];
            Circle c = circles[i];

            TranslateTransition t = transitions[i];
            t.setToX(p.x());
            t.setToY(p.y());
            t.play(); //cause zoom in

            //c.setCenterX(p.x());
            //c.setCenterY(p.y());

            if (!p.alive()) {
                c.setFill(Color.BLACK);
            } else if (!p.infected()) {
                c.setFill(Color.web("#68BBE3"));
            } else {
                c.setFill(Color.web("#ff6961"));
            }
        }
    }

    public void pauseScene() {
        timeline.pause();
    }

    public void stopScene() { timeline.stop(); }

    public Animation restartScene(int count, int x, int y, String name) { return new Animation(count, x, y, name); }

    public Point[] generateReport() {
        return bk.generateReport();
    }
}
