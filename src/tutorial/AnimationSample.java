package tutorial;

import java.util.Date;
import java.util.Random;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/** Simple JavaFX Animation Sample. */
public class AnimationSample extends Application {
    private       Circle[]    circles  = new Circle[10];
    private final TimeCounter counter  = new TimeCounter();
    private final Random      rand     = new Random();

    public static void main(String[] args) throws Exception { launch(args); }

    @Override
    public void start(final Stage stage) throws Exception {
        //Make a new Timeline object and play the timeline, passing the animation

        for (int i = 0; i < circles.length; i++) {
            circles[i] = new Circle(20, 20, 3);
        }

        final Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO,
                e -> refreshScene()),
                new KeyFrame(Duration.millis(1000))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);

        stage.setScene(new Scene(new Group(circles), 100, 100));
        stage.show();

        counter.reset();
        timeline.play();
    }

    private void refreshScene() {
        for (Circle dot : circles) {
            dot.setCenterX(dot.getCenterX() + rand.nextInt(21) - 10);
            dot.setCenterY(dot.getCenterY() + rand.nextInt(21) - 10);
        }
    }

    class TimeCounter {
        private long start = new Date().getTime();
        void reset()   { start = new Date().getTime(); }
        long elapsed() { return new Date().getTime() - start; }
    }
}