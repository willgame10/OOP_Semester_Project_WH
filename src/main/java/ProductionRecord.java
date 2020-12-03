import java.util.Date;

/**
 * Author: William Howell
 * Brief: ProductionRecord class file that contains methods, to_strings, and fields.
 */
public class ProductionRecord {

    public int productionNumber;
    public int productID;
    public String serialNumber;
    public Date dateProduced;

    public int getProductionNum() {
        return productionNumber;
    }


    public int getProductID() {
        return productID;
    }

    public void setProductID(int prodID) {
        productID = prodID;
    }

    public String getSerialNum() {
        return serialNumber;
    }

    public Date getProdDate() {
        return dateProduced;
    }

    ProductionRecord(int productionNumber, int productID, String serialNumber, Date dateProduced) {
        this.productionNumber = productionNumber;
        this.productID = productID;
        this.serialNumber = serialNumber;
        this.dateProduced = dateProduced;
    }

    ProductionRecord(Product product, int prodCount) {
        String endSerialDigits = String.format("%05d", prodCount);
        setProductID(product.getId());
        serialNumber = product.getManufacturer().substring(0, 3) + product.getType().code + endSerialDigits;
        dateProduced = new Date();
        productionNumber++;
    }

    @Override
    public String toString() {
        return "Prod. Num: " + getProductionNum() +
                " Product Id: " + getProductID() +
                " Serial Num: " + getSerialNum() +
                " Date: " + getProdDate() + "\n";
    }

    //to string that replaces the product ID with the product name
    public String toString(String prodName) {
        return "Prod. Num: " + getProductionNum() +
                " Product Name: " + prodName +
                " Serial Num: " + getSerialNum() +
                " Date: " + getProdDate() + "\n";
    }


}
