import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;

import java.sql.PreparedStatement;

import java.sql.*;

/**
 * Author: William Howell
 * Brief: controller file that contains all program code and methods that will be used by the GUI.
 * contains private variables and initializes the program.
 */
public class Controller {

    private static PreparedStatement ist;

    private static Connection conn;

    private static Statement stmt;

    private static ResultSet rs;

    @FXML
    private ComboBox<String> chQuantity;

    @FXML
    private TextField prName;

    @FXML
    private TextField prMan;

    @FXML
    private ChoiceBox<String> itemTypeCB;

    @FXML
    private TextArea text_area;

    /**
     * brief:Method that adds a product into the database using a prepared sql statement.
     */
    public void addProduct(ActionEvent actionEvent) {
        //prepared statement that inserts data into the database.
        try {
            String insertSql = "INSERT INTO Product(type, manufacturer, name) VALUES ( ? , ? , ? )";
            ist = conn.prepareStatement(insertSql);
            ist.setString(1, itemTypeCB.getValue());
            ist.setString(2, prMan.getText());
            ist.setString(3, prName.getText());

            ist.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void recordProduction(ActionEvent actionEvent) {
        System.out.println();
    }

    /**
     * brief:Initializes the program and populates combo box with numbers
     */
    public void initialize() {
        connectToDB();
        for (ItemType item : ItemType.values()) {
            itemTypeCB.getItems().add(item.getCode());
        }
        for (int i = 1; i < 11; i++) {
            chQuantity.getItems().add(String.valueOf(i));
            chQuantity.setEditable(true);
            chQuantity.getSelectionModel().selectFirst();
        }
        showProduction();

        try {
            String sql = "select * from PRODUCT";

            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
                System.out.println(rs.getString(4));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    /**
     * brief: Method that connects the database to the GUI with some
     */
    public static void connectToDB() {
        final String JDBC_DRIVER = "org.h2.Driver";
        final String DB_URL = "jdbc:h2:./res/HR";

        //  Database credentials
        final String USER = "";
        final String PASS = "";
        conn = null;
        stmt = null;

        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void showProduction() {
        text_area.setEditable(false);
        text_area.clear();
        try {
            stmt = conn.createStatement();
            String sql = "SELECT * FROM PRODUCT";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                text_area.appendText(
                        "Product Name: " + rs.getString("NAME") + "\n"
                                + "Product Id: " + rs.getInt("PRODUCT_ID") + "\n"
                                + "Serial Number: " + rs.getString("SERIAL_NUM") + "\n"
                                + "Date Produced: " + rs.getTimestamp("DATE_PRODUCED") + "\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
