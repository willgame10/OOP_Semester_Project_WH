import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.*;
import java.util.ArrayList;


/**
 * Author: William Howell
 * Brief: controller file that contains all program code and methods that will be used by the GUI.
 * contains private variables and initializes the program.
 */
public class Controller {
    //Initialize Product productLine Observable list to store information from the database
    private final ObservableList<Product> productLine = FXCollections.observableArrayList();

    //Initialize ProductionRecord productionLog Observable list to store information from the database
    private final ObservableList<ProductionRecord> productionLog = FXCollections.observableArrayList();

    private final ObservableList<Employee> employees = FXCollections.observableArrayList();
    //box that a user will input their name(Employee Tab)
    @FXML
    private TextField empName;
    //box that a user will input a password into(Employee Tab)
    @FXML
    private TextField password;
    //Text area that will display every employee(Employee Tab)
    @FXML
    private TextArea Employee_Text_Area;

    //box that allows you to choose the quantity you want to produce(Produce Tab)
    @FXML
    private ComboBox<String> chQuantity;

    //ListView that displays the produce list(Produce Tab)
    @FXML
    private ListView<Product> list_View;

    //box that a user will input a product name(Product Line Tab)
    @FXML
    private TextField prName;

    //box that a user will input a Manufacturer name(Product Line Tab)
    @FXML
    private TextField prMan;

    //TableView that displays the product line(Product Line Tab)
    @FXML
    private TableView<Product> existingProd;

    //ChoiceBox that allows you to choose an item type(Product Line Tab)
    @FXML
    private ChoiceBox<String> itemTypeCB;

    //Column in existingProd table for Type(Product Line Tab)
    @FXML
    private TableColumn<Product, String> typeCol;

    //Column in existingProd table for ManufacturerName(Product Line Tab)
    @FXML
    private TableColumn<Product, String> manufacCol;

    //Column in existingProd table for ProductName(Product Line Tab)
    @FXML
    private TableColumn<Product, String> productCol;

    //Column in existingProd table for ProductId(Product Line Tab)
    @FXML
    private TableColumn<Product, Integer> idCol;

    //TextArea that displays the production record(Production Log)
    @FXML
    private TextArea text_area;

    //stores the last four digits of the serial number
    int serialId;

    //Variables to store Serial Number for specific item type.
    ArrayList<Integer> idAUList = new ArrayList<>();
    ArrayList<Integer> idAMList = new ArrayList<>();
    ArrayList<Integer> idVIList = new ArrayList<>();
    ArrayList<Integer> idVMList = new ArrayList<>();

    //variable that will hold the database password.
    String dBPassword = "";

    //initialization of the BufferedReader to be able to read from a file.
    BufferedReader reader;

    {
        try {
            //reader will read file from this path.
            reader = Files.newBufferedReader(Paths.get("src/main/resources/password.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //database drivers and location
    final static String JDBC_DRIVER = "org.h2.Driver";
    final static String DB_URL = "jdbc:h2:./res/HR";

    //Database Credentials
    final static String USER = "";
    final String PASS = reverseString(dBPassword);

    /**
     * brief:Initializes the program and populates the program with items from the database.
     */
    public void initialize() {
        //init the password from password.txt
        startPassword();
        connectToDB();
        //init array list for serial numbers
        setArraylist();
        //populate combo box with items of ItemType
        PopulateComboBox();
        //init the existing products table
        setupProductLineTable();
        //init the list view for the products that are to be produced
        setupProduceListView();
        //init the employee text area with employees
        setupEmployee_Text_Area();
        //loads employees into the observable list
        loadEmployees();
        //loads products into observable list
        loadProductList();
        //loads production record observable list
        loadProductionLog();
    }

    /**
     * brief: Method that connects the database to the GUI
     */
    public void connectToDB() {
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            Statement stmt = conn.createStatement();

            //close database connection
            conn.close();

            //close Query
            stmt.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Brief: sets the value that will populate the columns of the existing products in the Product Line tab
     * and populates the TableView with items from the productLine ObservableList.
     */
    private void setupProductLineTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        manufacCol.setCellValueFactory(new PropertyValueFactory<>("Manufacturer"));
        productCol.setCellValueFactory(new PropertyValueFactory<>("Name"));

        existingProd.setItems(productLine);
    }

    private void setupEmployee_Text_Area() {
        Employee_Text_Area.appendText(employees.toString());
    }

    /**
     * Brief: Populates the ListView with items from the productLine ObservableList.
     */
    private void setupProduceListView() {
        list_View.setItems(productLine);
    }

    /**
     * Brief: populates the ComboBox in the Product Line tab with strings from the ItemType Enum
     * It also populates the ChoiceBox in the Produce Tab with 10 digits and allows you to input any number.
     */
    private void PopulateComboBox() {

        //loops through all of the items in the ItemType Enum.
        for (ItemType item : ItemType.values()) {
            itemTypeCB.getItems().add(String.valueOf(item));
        }

        //fills the choice box with numbers 1-10 and allows you to edit the number.
        for (int i = 1; i < 11; i++) {
            chQuantity.getItems().add(String.valueOf(i));
            chQuantity.setEditable(true);
            chQuantity.getSelectionModel().selectFirst();
        }
    }

    /**
     * brief:Method that adds a product into the database using a prepared sql statement.
     */
    public void addProduct() {
        //prepared statement that inserts products into the database.
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            Statement stmt = conn.createStatement();

            //SQL statement that contains code to insert into the console to the database.
            String insertSql = "INSERT INTO Product(type, manufacturer, name) VALUES ( ? , ? , ? )";
            PreparedStatement ist = conn.prepareStatement(insertSql);

            //If the product Manufacturer name is shorter than 3 letters or name is null it will not be added to the database for error prevention.
            if (prMan.getLength() <= 2) {
                System.out.println("Error, Manufacturer name too short. Please use at least 3 letters");
            } else {

                //sets the product item type to what the user specifies.
                ist.setString(1, itemTypeCB.getValue());

                //sets the product manufacturer to what the user specifies.
                ist.setString(2, prMan.getText());

                //sets the product name to what the user specifies.
                ist.setString(3, prName.getText());
                ist.executeUpdate();
            }
            ist.close();

            //close Query
            stmt.close();

            //close database connection
            conn.close();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        prMan.clear();
        prName.clear();
        //re-loads the product list in the Product Line tab
        loadProductList();
    }

    /**
     * Brief: populates the existing product's Tableview with items from the product line ObservableList
     */
    private void loadProductList() {
        productLine.clear();
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            Statement stmt = conn.createStatement();

            //SQL statement that contains code select all Items from the Product Table in the database.
            String sql = "select * from PRODUCT";
            ResultSet rs = stmt.executeQuery(sql);

            //loops through every item in the database under the Product Table and populates the product line ObservableList.
            while (rs.next()) {                //retrieves the product id from the database.
                int id = rs.getInt(1);

                //retrieves the product name from the database.
                String name = rs.getString(2);

                //retrieves the product type from the database.
                String type = rs.getString(3);

                //retrieves the product manufacturer from the database.
                String manufacturer = rs.getString(4);

                //creates product items of class Product
                Product product = new Product(id, name, manufacturer, ItemType.valueOf(type)) {
                };

                //adds product items from the database into the productLine ObservableList.
                productLine.add(product);
            }
            //close database connection
            conn.close();

            //close Query
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Brief: Method that records the production of an item that is chosen in the Produce tab.
     */
    public void recordProduction() {

        //the product selected in the ListView by the user under the Produce Tab.
        Product product = list_View.getSelectionModel().getSelectedItem();

        //the quantity chosen in the ChoiceBox
        int numProd = Integer.parseInt(chQuantity.getValue());

        //depending on the products type they will receive a different set of digits at the end of the serial number.
        //it will loop however many times the user put in the ChoiceBox
        for (int i = 0; i < numProd; i++) {
            switch (product.getType()) {
                case AUDIO:
                    idAUList.add(1);
                    serialId = idAUList.size();
                    break;
                case VISUAL:
                    idVIList.add(1);
                    serialId = idVIList.size();
                    break;
                case AUDIO_MOBILE:
                    idAMList.add(1);
                    serialId = idAMList.size();
                    break;
                case VISUAL_MOBILE:
                    idVMList.add(1);
                    serialId = idVMList.size();
                    break;
            }
            //creates ProductionRecord items.
            ProductionRecord productionRun = new ProductionRecord(product, serialId);

            //Calls a method that adds the ProductionRecord item to the ProductionRecord table in the database.
            addToProductionDB(productionRun);
        }
        //loads the ProductionRecord TextArea
        loadProductionLog();
    }

    /**
     * Brief: populates the existing ProductionRecord TextArea with items from the productionLog ObservableList.
     */
    private void loadProductionLog() {

        //denies the user ability to edit what is in the Text Area(Production Log tab)
        text_area.setEditable(false);

        //cleans the TextArea before populating.
        text_area.clear();
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            Statement stmt = conn.createStatement();

            //SQL statement that contains code select all Items from the ProductionRecord Table in the database.
            String sql = "SELECT * FROM PRODUCTIONRECORD";
            ResultSet rs = stmt.executeQuery(sql);

            //loops through every item in the database under the ProductionRecord Table and populates the productionLog ObservableList.
            while (rs.next()) {

                //creates new productionRecord items from the Database.
                ProductionRecord productionRecord = new ProductionRecord(rs.getInt("PRODUCTION_NUM"), rs.getInt("PRODUCT_ID"),
                        rs.getString("SERIAL_NUM"), rs.getDate("DATE_PRODUCED"));


                //adds the new productionRecord items to the productionLog ObservableList.
                productionLog.add(productionRecord);

                text_area.appendText(productionRecord.toString(productIDtoName(productionRecord)));
            }
            //close database connection
            conn.close();

            //close Query
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Brief: Method that adds a production record to the database.
     *
     * @param productionRun - the item that is going to be added into the ProductionRecord.
     */
    private void addToProductionDB(ProductionRecord productionRun) {

        //The item selected in the ListView that the user wants to produce.
        Product product = list_View.getSelectionModel().getSelectedItem();

        //creates a timestamp for the birthday of the product you have produced.
        Timestamp date = new Timestamp(productionRun.getProdDate().getTime());
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            Statement stmt = conn.createStatement();

            //SQL statement that contains code to insert into the console to the database.
            String insertSql = "INSERT INTO PRODUCTIONRECORD( PRODUCT_ID, SERIAL_NUM, DATE_PRODUCED) VALUES ( ? , ? , ?)";
            PreparedStatement ist = conn.prepareStatement(insertSql);

            //sets the production record ID equal to what the product ID is.
            ist.setString(1, String.valueOf(product.getId()));

            //sets the serialnumber to what was sent from the ProductionRecord Parameter.
            ist.setString(2, productionRun.getSerialNum());

            //sets the Date to the current date of the items production from the Timestamp.
            ist.setString(3, String.valueOf(date));
            ist.executeUpdate();

            ist.close();

            //close database connection
            conn.close();

            //close Query
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Brief: initializes the array list that holds the amount of a certain item type already produced.
     * This is used for the serial number to have different ending digits per type.
     */
    public void setArraylist() {

        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            Statement stmt = conn.createStatement();

            //SQL statement that contains code to select serial number Items from the production record Table in the database.
            String sql = "SELECT SERIAL_NUM FROM PRODUCTIONRECORD";
            ResultSet rs = stmt.executeQuery(sql);

            //loops through the serial number column and adds to the arraylist holding the amount of a item type that has been produced.
            while (rs.next()) {
                if (rs.getString("SERIAL_NUM").substring(3, 5).contains("AU")) {
                    idAUList.add(1);
                }
                if (rs.getString("SERIAL_NUM").substring(3, 5).contains("AM")) {
                    idAMList.add(1);
                }
                if (rs.getString("SERIAL_NUM").substring(3, 5).contains("VI")) {
                    idVIList.add(1);
                }
                if (rs.getString("SERIAL_NUM").substring(3, 5).contains("VM")) {
                    idVMList.add(1);
                }

            }
            //close database connection
            conn.close();

            //close Query
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will select the products name where the ID is the same as the productionRecords ID.
     *
     * @param productionRecord - takes a production record item.
     * @return String "products name"
     */
    public String productIDtoName(ProductionRecord productionRecord) {
        String nameProd = "";
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            Statement stmt = conn.createStatement();

            //SQL statement that contains code select all Items from the Product Table in the database.
            String sql = "SELECT NAME FROM PRODUCT WHERE ID = '" + productionRecord.getProductID() + "' ";
            ResultSet rs = stmt.executeQuery(sql);
            //name of the product is equal to the result of the SQL statement.
            while (rs.next()) {
                nameProd = rs.getString(1);
            }
            //close database connection
            conn.close();

            //close Query
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return nameProd;
    }

    /**
     * Brief: This method will add an employee when the button is pressed.
     */
    @FXML
    public void addEmployee() {
        //prepared statement that inserts products into the database.
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            Statement stmt = conn.createStatement();

            //retrieves name from text field
            String name = empName.getText();

            //retrieves password from text field
            String passWord = password.getText();

            //creates a new employee object.
            Employee employee = new Employee(name, passWord);

            //SQL statement that contains code to insert into the console to the database.
            String insertSql = "INSERT INTO EMPLOYEES(name, username, password, email) VALUES ( ?, ?, ?, ? )";
            PreparedStatement ist = conn.prepareStatement(insertSql);
            //sets the product item type to what the user specifies.
            ist.setString(1, String.valueOf(employee.getName()));

            //sets the product manufacturer to what the user specifies.
            ist.setString(2, employee.getUsername());

            ist.setString(3, reverseString(employee.getPassword()));

            ist.setString(4, employee.getEmail());

            ist.executeUpdate();

            ist.close();
            //close database connection
            conn.close();

            //close Query
            stmt.close();


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        empName.clear();
        password.clear();
        loadEmployees();
    }

    /**
     * Brief: this method loads employees into the employee text area
     */
    public void loadEmployees() {
        //resets text area
        Employee_Text_Area.clear();
        //no one can edit the text area.
        Employee_Text_Area.setEditable(false);
        try {
            // STEP 1: Register JDBC driver
            Class.forName(JDBC_DRIVER);

            //STEP 2: Open a connection
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            //STEP 3: Execute a query
            Statement stmt = conn.createStatement();

            //SQL statement that contains code select all Items from the ProductionRecord Table in the database.
            String sql = "SELECT * FROM EMPLOYEES";
            ResultSet rs = stmt.executeQuery(sql);

            //loops through every item in the database under the ProductionRecord Table and populates the productionLog ObservableList.
            while (rs.next()) {
                //creates new employee object from database.
                Employee employee = new Employee(rs.getString(2), reverseString(rs.getString(4)));
                //adds new employee to array list.
                employees.add(employee);
                //adds the new employee to the text area.
                Employee_Text_Area.appendText(employee.toString());
            }
            //close database connection
            conn.close();

            //close Query
            stmt.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //reverses the string of anything passed into it.
    public String reverseString(String pw) {
        if (pw.length() <= 1) {
            return pw;
        }
        return reverseString(pw.substring(1)) + pw.charAt(0);
    }

    /**
     * Brief: reads password from reader and returns what was found.
     *
     * @param reader -   Buffered reader that will hold the path of the file we want to read.
     * @return - returns what was found in the file.
     * @throws IOException -
     */
    public String readPassword(BufferedReader reader) throws IOException {
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            content.append(line);
            content.append(System.lineSeparator());
        }

        return content.toString();
    }

    /**
     * Brief: init the password that was found in the file.
     */
    public void startPassword() {
        try {
            dBPassword = readPassword(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}