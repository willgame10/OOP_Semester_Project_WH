/**
 * Author: William Howell
 * Brief: methods that must be implemented if a class extends Item.
 */
public interface Item {
    int getId();

    void setName(String name);

    String getName();

    void setManufacturer(String Manufacturer);

    String getManufacturer();
}
