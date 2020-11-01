public class AudioPlayer extends Product implements MultiMediaControl{

    String supportedAudioFormats;
    String supportedPlaylistFormats;

    public AudioPlayer(String name, String manufacturer, String supportedAudioFormats, String supportedPlaylistFormats) {
        super(name, manufacturer, ItemType.AUDIO);

        this.supportedAudioFormats = supportedAudioFormats;
        this.supportedPlaylistFormats = supportedPlaylistFormats;
    }

    @Override
    public String getManufacturer() {
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
                + ItemType.AUDIO + "\n" +"Supported Audio Formats: " + supportedAudioFormats + "\n"
                +"Supported Playlist Formats: " +supportedPlaylistFormats;
    }
}
