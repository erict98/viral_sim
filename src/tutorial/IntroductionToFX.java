package tutorial;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Remember to edit configurations for the VM
 * --module-path "C:\Program Files\Java\javafx-sdk-14.0.2.1\lib" --add-modules javafx.controls,javafx.fxml
 */
public class IntroductionToFX extends Application{

    Stage window;
    Button button;
    Button alertBox;
    Button confirmBox;

    public static void main(String[] args) {
        launch(args); //setup as JavaFX application
    }

    /**
     * Starting: launch(args) is required to start the JavaFX application. A stage is the application and within a
     * stage are different scenes. Scenes can have layouts which contains different interactive features.
     */
    @Override
    public void start(Stage primaryStage) {
        this.window = primaryStage;

        primaryStage.setTitle("Simulator");

        button = new Button("Button");
        //button.setOnAction(this); // Can make another class to handle to this event and look for handle method
        //button.setOnAction(event -> { System.out.println("This is a lambda expression"); });

        alertBox = new Button("Alert box");
        alertBox.setOnAction(e -> AlertBox.display("Alert box window", "This is an alert box"));

        confirmBox = new Button("Confirm box");
        confirmBox.setOnAction(e -> {
            boolean result;
            result = ConfirmBox.display("Confirm box window", "Please choose an answer");
            System.out.println(result);
        });

        Button close = new Button("Close Program");
        close.setOnAction(e -> closeProgram());
        window.setOnCloseRequest(e -> { // is called when user tries to exit the program
            e.consume(); // Use this if the program closes anyway since the request went through
            closeProgram();
        });

        VBox leftMenu = new VBox(10);
        leftMenu.getChildren().addAll(button, alertBox, confirmBox, close);

        HBox topMenu = new HBox();
        Button a = new Button("File");
        Button b = new Button("Edit");
        Button c = new Button("view");
        topMenu.getChildren().addAll(a, b, c);

        // StackPane layout = new StackPane();
        BorderPane layout = new BorderPane();
        layout.setTop(topMenu);
        layout.setLeft(leftMenu);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene); // adds the scene
        primaryStage.show(); // shows the stage
    }


    /**
     * Allows the user to properly close the program
     */
    private void closeProgram() {
        Boolean answer = ConfirmBox.display("Confirm", "Are you sure you want to exit the program?");
        if (answer) {
            System.out.println("Progress has been saved!");
            window.close();
        }
    }
}
