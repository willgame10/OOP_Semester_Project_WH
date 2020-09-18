import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    /**
     * brief: Main method that loads the sample.fxml file and opens a window for the program.
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        Scene scene = new Scene(root, 500, 400);                            //window of GUI when launched

        primaryStage.setTitle("OOP_Semester_Project");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
