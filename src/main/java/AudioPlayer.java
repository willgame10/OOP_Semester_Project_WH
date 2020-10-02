import java.lang.reflect.Type;

public class AudioPlayer extends Product implements MultiMediaControl{

    String supportedAudioFormats;
    String supportedPlaylistFormats;

    public AudioPlayer(String name, String manufacturer, String supportedAudioFormats, String supportedPlaylistFormats) {
        super(name, manufacturer, ItemType.AUDIO.code);

        this.supportedAudioFormats = supportedAudioFormats;
        this.supportedPlaylistFormats = supportedPlaylistFormats;
    }

    @Override
    public String Manufacturer() {
        return "Manufacturer";
    }

    @Override
    public void play() {
        System.out.println("Playing");
    }

    @Override
    public void stop() {
        System.out.println("Stopping");
    }

    @Override
    public void previous() {
        System.out.println("Previous");
    }

    @Override
    public void next() {
        System.out.println("Next");
    }

    public String toString() {
        return "Name: " + getName() + "\n" + "Manufacturer: " + getManufacturer() + "\n" + "Type: "
                + ItemType.AUDIO + "\n" +"supported audio formats: " + supportedAudioFormats + "\n"
                +"supported playlist formats: " +supportedPlaylistFormats;
    }
}