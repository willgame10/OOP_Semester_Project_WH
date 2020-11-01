public class Product implements Item {

    private int Id;
    private ItemType Type;
    private String Manufacturer;
    private String Name;


    Product(String name, String manufacturer, ItemType type) {
        this.Name = name;
        this.Manufacturer = manufacturer;
        this.setType(type);
    }

    public String toString() {
        return "Name: " + Name + "\n" + "Manufacturer: " + Manufacturer + "\n" + "Type: "
                + getType();
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getId() {
        return Id;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.Manufacturer = manufacturer;
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

abstract class Widget extends Product {

    Widget(String name, String manufacturer, ItemType type) {

        super(name, manufacturer, type);
    }
}