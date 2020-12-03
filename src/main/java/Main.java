import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Author: William Howell
 * Brief: Main java driver class file that runs the program files.
 */
public class Main extends Application {
    /**
     * brief: Main method that loads the sample.fxml file and opens a window for the program.
     */
    public static void main(String[] args) {
        Controller controller = new Controller();
        controller.connectToDB();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Scene scene = new Scene(root, 593, 400);                            //window of GUI when launched

        primaryStage.setTitle("Production Database");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
