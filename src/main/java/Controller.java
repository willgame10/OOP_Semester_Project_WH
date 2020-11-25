import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;


import java.sql.PreparedStatement;

import java.sql.*;


/**
 * Author: William Howell
 * Brief: controller file that contains all program code and methods that will be used by the GUI.
 * contains private variables and initializes the program.
 */
public class Controller {

    private ObservableList<Product> productLine = FXCollections.observableArrayList();

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
            Product product = new Widget(prName.getText(), prMan.getText(), ItemType.valueOf(itemTypeCB.getValue()));
            productLine.add(product);
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
            Timestamp date = new Timestamp(item.getProdDate().getTime());

            try {
                String insertSql = "INSERT INTO PRODUCTIONRECORD( PRODUCT_ID, SERIAL_NUM, DATE_PRODUCED) VALUES ( ? , ? , ?)";
                ist = conn.prepareStatement(insertSql);
                ist.setString(1, String.valueOf(productLine.get(select).getId()));
                ist.setString(2, item.getSerialNum());
                ist.setString(3, String.valueOf(date));
                ist.executeUpdate();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
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
        setupProduceListView();
        setupProductLineTable();

        try {
            String sql = "select * from PRODUCT";

            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String name = rs.getString(2);

                String type = rs.getString(3);

                String manufacturer = rs.getString(4);

                Product product = new Widget(name, manufacturer, ItemType.valueOf(type));
                productLine.add(product);
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
                ProductionRecord productionRecord = new ProductionRecord(rs.getInt("PRODUCTION_NUM"), rs.getInt("PRODUCT_ID"),
                        rs.getString("SERIAL_NUM"), rs.getDate("DATE_PRODUCED"));
                text_area.appendText(productionRecord.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupProductLineTable() {

        typeCol.setCellValueFactory(new PropertyValueFactory("Type"));
        manufacCol.setCellValueFactory(new PropertyValueFactory("Manufacturer"));
        productCol.setCellValueFactory(new PropertyValueFactory("Name"));

        existingProd.setItems(productLine);
    }

    private void setupProduceListView() {
        list_View.setItems(productLine);
    }

}