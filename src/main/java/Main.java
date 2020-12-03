import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Author: William Howell
 * Brief: Main java class file that runs the program files.
 */

public class Main extends Application {

    @FXML
    private TextField usernameText;

    @FXML
    private TextField passwordText;

    @FXML
    private Button logInButton;

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

        Scene scene = new Scene(root, 550, 425);                            //window of GUI when launched

        primaryStage.setTitle("OOP_Semester_Project");
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
