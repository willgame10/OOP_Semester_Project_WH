import java.util.Date;

public class ProductionRecord {

    protected static int productionNumber = 0;
    public int productID;
    public String serialNumber;
    public Date dateProduced;

    public int getProductionNum() {
        return productionNumber;
    }

    public void setProductionNum(int productionNum) {
        productionNumber = productionNum;
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

    public void setSerialNum(String serialNum) {
        serialNumber = serialNum;
    }

    public Date getProdDate() {
        return dateProduced;
    }

    public void setProdDate(Date dateProd) {
        dateProduced = dateProd;
    }

    ProductionRecord(int productID) {
        this.productID = productID;
        serialNumber = String.valueOf(0);
        dateProduced = new Date();
        productionNumber++;
    }

    ProductionRecord(int productionNumber, int productID, String serialNumber, Date dateProduced) {
        ProductionRecord.productionNumber = productionNumber;
        this.productID = productID;
        this.serialNumber = serialNumber;
        this.dateProduced = dateProduced;
    }

    ProductionRecord(Product product, int prodCount) {
        String endSerialDigits = String.format("%05d", prodCount);
        productID =getProductID();
        serialNumber = product.getManufacturer().substring(0, 3) + product.getType().code + endSerialDigits;
        dateProduced = new Date();
        productionNumber++;
    }

    @Override
    public String toString() {
        return  "Prod. Num: " + getProductionNum() +
                " Product ID: " + getProductID() +
                " Serial Num: " + getSerialNum() +
                " Date: " + getProdDate() + "\n";
    }

}
