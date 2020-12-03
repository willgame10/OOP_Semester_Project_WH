/**
 * Author: William Howell
 * Brief: Product class file that contains methods, to_strings, and fields.
 */
abstract public class Product implements Item {

    private final int Id;
    private ItemType Type;
    private String Manufacturer;
    private String Name;

    //product constructor
    Product(int id, String name, String manufacturer, ItemType type) {
        Id = id;
        this.Name = name;
        this.Manufacturer = manufacturer;
        this.setType(type);
    }

    //to string constructor
    public String toString() {
        return "Name: " + Name + "\n" + "Manufacturer: " + Manufacturer + "\n" + "Type: "
                + getType();
    }

    public int getId() {
        return Id;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ItemType getType() {
        return Type;
    }

    public void setType(ItemType type) {
        Type = type;
    }
}