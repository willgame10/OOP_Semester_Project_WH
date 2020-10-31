public class MoviePlayer extends Product implements MultiMediaControl{

    Screen screen;
    MonitorType monitorType;

    public MoviePlayer(String name, String manufacturer, Screen screen, MonitorType monitorType) {
        super(name, manufacturer, monitorType.name());

        this.monitorType = monitorType;
        this.screen = screen;
    }

    @Override
    public String getManufacturer() {
        return "Manufacturer";
    }

    @Override
    public void play() {
        System.out.println("Playing movie");
    }

    @Override
    public void stop() {
        System.out.println("Stopping movie");
    }

    @Override
    public void previous() {
        System.out.println("Previous movie");
    }

    @Override
    public void next() {
        System.out.println("Next movie");
    }

    public String toString() {
        return "Name: " + getName() + "\n" + "Manufacturer: " + getManufacturer() + "\n" + "Type: "
                + ItemType.VISUAL + "\n" + "Screen: " + "\n" + screen + "\n" + "Monitor Type: " + monitorType;
    }
}
