package simulator;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GUI extends Application {
    public final int HEIGHT = 500;
    public final int WIDTH  = 500;

    private Stage window;
    private HBox configurations;
    private Animation animation;
    private final int MAX_COUNT = 300;
    private final int MIN_COUNT = 100;
    private final int START_COUNT = 100;
    private final String START_DISEASE = "TestDisease";

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
    animation = new Animation(START_COUNT, WIDTH, HEIGHT, START_DISEASE);
        this.window = stage;

        stage();
        configurations();
        setStage();


        window.show();
    }

    public void stage() {
        window.setTitle("Simulator");
        window.setResizable(false);
        window.setMinWidth(WIDTH);
        window.setMinHeight(HEIGHT);
    }

    public void configurations() {
        this.configurations = new HBox();

        ComboBox<String> cb = new ComboBox<String>();
        cb.getItems().addAll("COVID19", "TestDisease"); // Ordering of options
        cb.setValue(START_DISEASE);

        Button play = new Button("Play");
        Button pause = new Button("Pause");
        Button restart = new Button("Restart");

        play.setOnAction(e -> animation.playScene());
        pause.setOnAction(e -> animation.pauseScene());
        restart.setOnAction(e -> {
            animation = restart(configurations);
            setStage();
        });

        Slider slider = new Slider(MIN_COUNT, MAX_COUNT, START_COUNT);
        slider.setMajorTickUnit(100.0);
        slider.setOrientation(Orientation.HORIZONTAL);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);

        configurations.getChildren().addAll(cb, play, pause, restart, slider);
        configurations.setSpacing(20);
    }

    public void setStage() {
        BorderPane pane = new BorderPane();
        pane.setTop(configurations);

        Pane p = new Pane();
        for (Group g : animation.group()) {
           p.getChildren().add(g);
        }
        pane.setCenter(p);
        window.setScene(new Scene(pane, WIDTH, HEIGHT));
    }

    public Animation restart(HBox hb) {
        animation.pauseScene();

        ComboBox<String> name = (ComboBox<String>) hb.getChildren().get(0);
        String nameValue = name.getValue();
        Slider count = (Slider) hb.getChildren().get(4);
        int countValue = (int) count.getValue();

        return animation.restartScene(countValue, WIDTH, HEIGHT, nameValue);
    }
}
