import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.PreparedStatement;

import java.sql.*;

/**
 * class that houses all the code needed to run the program
 */
public class Controller {

    @FXML
    private ComboBox<String> chQuantity;

    @FXML
    private TextField prName;

    @FXML
    private TextField prMan;

    @FXML
    private ChoiceBox<String> itemType;

    /**
     * Method that adds a product into the database using a prepared sql statement.
     */
    public void addProduct(ActionEvent actionEvent) {
        connectToDB();
    }

    public void recordProduction(ActionEvent actionEvent) {
        System.out.println();
    }

    /**
     * Initializes the program and populates combo box with numbers
     */
    public void initialize() {
        itemType.getItems().add("AUDIO");
        for (int i = 1; i < 11; i++) {
            chQuantity.getItems().add(String.valueOf(i));
            chQuantity.setEditable(true);
            chQuantity.getSelectionModel().selectFirst();
        }
    }

    /**
     * Method that connects the database to the GUI with some
     */
    public void connectToDB() {
        final String JDBC_DRIVER = "org.h2.Driver";
        final String DB_URL = "jdbc:h2:./res/HR";

        //  Database credentials
        final String USER = "";
        final String PASS = "";
        Connection conn = null;
        Statement stmt = null;

        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            stmt = conn.createStatement();

            /**
             * prepared statement that inserts data into the database.
             */
            String insertSql = "INSERT INTO Product(type, manufacturer, name) VALUES ( ? , ? , ? )";
            PreparedStatement ist = conn.prepareStatement(insertSql);
            ist.setString(1, itemType.getValue());
            ist.setString(2, prMan.getText());
            ist.setString(3, prName.getText());

            ist.executeUpdate();

            /**
             * prints to console all the products in the database after button click.
             */
            String sql = "select id, name, type, manufacturer from PRODUCT";

            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
                System.out.println(rs.getString(4));
            }


            // STEP 4: Clean-up environment
            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
