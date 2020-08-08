package simulator;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import kdtree.Point;

public class GUI extends Application {
    public final int HEIGHT = 500;
    public final int WIDTH  = 500;
    public final int RADIUS = 3;

    private Bookkeeper bk;
    private Point[] points;
    private Circle[] circles;

    public static void main(String[] args) { launch(args); }

    /**
     * Border Pane (left - paint, right - menus)
     * UI - tick speed, pause, continue, restart, count
     * Log that reports new upates
     *
     * Global time clock
     * */
    @Override
    public void start(Stage stage) throws Exception {
        startUp();
        stage.setTitle("Viral Disease Simulator");
        stage.setResizable(false);
        stage.setMinWidth(WIDTH);
        stage.setMinHeight(HEIGHT);

        KeyFrame kf = new KeyFrame(Duration.ZERO, e -> refreshScene());
        final Timeline timeline = new Timeline(kf, new KeyFrame(Duration.millis(500)));
        timeline.setCycleCount(Timeline.INDEFINITE);

        Pane pane = new Pane();
        Button play = new Button("Play");
        play.setOnAction(e -> {
            timeline.play();
            play.setDisable(true);
        });
        pane.getChildren().addAll(play, new Group(circles));

        stage.setScene(new Scene(pane, WIDTH, HEIGHT));
        stage.show();
    }

    public void startUp() {
        this.bk = new Bookkeeper(300, WIDTH, HEIGHT, "COVID19"); // Too much slows down the program
        this.points = bk.id();
        this.circles = new Circle[points.length];

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];

            int x = p.x();
            int y = p.y();

            Circle c = new Circle(x, y, RADIUS);
            if (!p.alive()) {
                c.setFill(Color.BLACK);
            } else if (p.infected()) {
                c.setFill(Color.web("#ff6961"));
            } else {
                c.setFill(Color.web("#68BBE3"));
            }
            circles[i] = c;
        }
    }

    public void refreshScene() {
        bk.updateSimulation();
        for (int i = 0; i < circles.length; i++) {
            Point p = points[i];
            Circle c = circles[i];

            c.setCenterX(p.x());
            c.setCenterY(p.y());

            if (!p.alive()) {
                c.setFill(Color.BLACK);
            } else if (!p.infected()) {
                c.setFill(Color.web("#68BBE3"));
            } else {
                c.setFill(Color.web("#ff6961"));
            }
        }
    }
}
