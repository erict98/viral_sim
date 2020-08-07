package simulator;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import kdtree.Point;

import java.sql.Time;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import java.awt.print.Book;
import java.util.List;

public class GUI extends Application {
    public final int HEIGHT = 768;
    public final int WIDTH = 1024;
    public final int RADIUS = 3;

    private Bookkeeper bk;
    private Point[] points;
    private Circle[] circles;

    public static void main(String[] args) { launch(args); }

    /**
     * Border Pane (left - paint, right - menus)
     * UI - tick speed, pause, continue, restart, count
     *
     * For each dot make a new class?
     */
    @Override
    public void start(Stage stage) throws Exception {
        startUp();
        stage.setTitle("Viral Disease Simulator");
        stage.setResizable(false);
        stage.setMinWidth(WIDTH);
        stage.setMinHeight(HEIGHT);

        KeyFrame kf = new KeyFrame(Duration.ZERO, e -> refreshScene());
        final Timeline timeline = new Timeline(kf, new KeyFrame(Duration.millis(1000)));
        timeline.setCycleCount(Timeline.INDEFINITE);

        stage.setScene(new Scene(new Group(circles), WIDTH, HEIGHT));
        stage.show();

        timeline.play();
    }

    public void startUp() {
        this.bk = new Bookkeeper(100, WIDTH, HEIGHT, "COVID19");
        this.points = bk.id();
        this.circles = new Circle[points.length];

        for (int i = 0; i < points.length; i++) {
            int x = points[i].x();
            int y = points[i].y();
            circles[i] = new Circle(x, y, RADIUS);

            if (points[i].infected()) {
                circles[i].setFill(Color.RED);
            } else {
                circles[i].setFill(Color.BLUE);
            }
        }
    }

    public void refreshScene() {
        bk.updateSimulation();
        for (int i = 0; i < circles.length; i++) {
            Point p = points[i];
            Circle c = circles[i];

            c.setCenterX(p.x());
            c.setCenterY(p.y());

            if (p.infected()) {
                c.setFill(Color.RED);
            } else if (!p.alive()) {
                c.setFill(Color.BLACK);
            } else {
                c.setFill(Color.BLUE);
            }
        }
    }
}
