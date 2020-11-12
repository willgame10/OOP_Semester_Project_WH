import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.sql.PreparedStatement;

import java.sql.*;
import java.util.Date;

/**
 * Author: William Howell
 * Brief: controller file that contains all program code and methods that will be used by the GUI.
 * contains private variables and initializes the program.
 */
public class Controller {

    private ObservableList<Product> productLine;

    private PreparedStatement ist;

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
    private TableView<Product> existingProd;

    @FXML
    private ListView<Product> list_View;

    @FXML
    private ChoiceBox<String> itemTypeCB;

    @FXML
    private TableColumn<?, ?> typeCol;

    @FXML
    private TableColumn<?, ?> manufacCol;

    @FXML
    private TableColumn<?, ?> productCol;

    @FXML
    private TextArea text_area;

    int serialId;
    int idAU;
    int idAM;
    int idVI;
    int idVM;

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

        setupProductLineTable();
        setupProduceListView();
    }

    public void recordProduction(ActionEvent actionEvent) {
        int numProd = Integer.parseInt(chQuantity.getValue());
        int select = list_View.getSelectionModel().getSelectedIndex();
        for (int i = 0; i < numProd; i++) {
            switch (productLine.get(select).getType()) {
                case AUDIO:
                    idAU++;
                    serialId = idAU;
                    break;
                case VISUAL:
                    idVI++;
                    serialId = idVI;
                    break;
                case AUDIO_MOBILE:
                    idAM++;
                    serialId = idAM;
                    break;
                case VISUAL_MOBILE:
                    idVM++;
                    serialId = idVM;
                    break;
            }
            ProductionRecord item = new ProductionRecord(productLine.get(select), serialId);
            text_area.appendText(String.valueOf(item));
/*            try {
            String insertSql = "INSERT INTO PRODUCTIONRECORD(PRODUCTION_NUM, PRODUCT_ID, SERIAL_NUM, DATE_PRODUCED) VALUES ( ? , ? , ? , ?)";
            ist = conn.prepareStatement(insertSql);
            ist.setInt(1, numProd);
            ist.setInt(2, );
            ist.setInt(3, serialId);
            ist.setDate(4, new Date);

        } catch(SQLException e){
            e.printStackTrace();
        }*/
        }

    }

    /**
     * brief:Initializes the program and populates combo box with numbers
     */
    public void initialize() {
        connectToDB();
        for (ItemType item : ItemType.values()) {
            itemTypeCB.getItems().add(String.valueOf(item));
        }
        for (int i = 1; i < 11; i++) {
            chQuantity.getItems().add(String.valueOf(i));
            chQuantity.setEditable(true);
            chQuantity.getSelectionModel().selectFirst();
        }
        showProduction();
        productLine = FXCollections.observableArrayList();

        try {
            String sql = "select * from PRODUCT";

            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getString(1));
                System.out.println(rs.getString(2));
                System.out.println(rs.getString(3));
                System.out.println(rs.getString(4));
            }
        } catch (SQLException e) {
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
            String sql = "SELECT * FROM PRODUCTIONRECORD";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                text_area.appendText(
                        "Production Number: " + rs.getString("PRODUCTION_NUMBER") + "\n"
                                + "Product Id: " + rs.getInt("PRODUCT_ID") + "\n"
                                + "Serial Number: " + rs.getString("SERIAL_NUM") + "\n"
                                + "Date Produced: " + rs.getTimestamp("DATE_PRODUCED") + "\n");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupProductLineTable() {
        String name = prName.getText();

        String manufacturer = prMan.getText();

        String type = itemTypeCB.getValue();


        Product product = new Widget(name, manufacturer, ItemType.valueOf(type));
        productLine.add(product);
        typeCol.setCellValueFactory(new PropertyValueFactory("Type"));
        manufacCol.setCellValueFactory(new PropertyValueFactory("Manufacturer"));
        productCol.setCellValueFactory(new PropertyValueFactory("Name"));
        existingProd.setItems(productLine);
        list_View.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void setupProduceListView() {
        list_View.setItems(productLine);
    }

}

