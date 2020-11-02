public class Screen implements ScreenSpec {

    String resolution;
    int refreshRate;
    int responseTime;

    @Override
    public String getResolution() {
        return null;
    }

    @Override
    public int getRefreshRate() {
        return 0;
    }

    @Override
    public int getResponseTime() {
        return 0;
    }

    Screen(String resolution, int refreshRate, int responseTime) {
        this.resolution = resolution;
        this.refreshRate = refreshRate;
        this.responseTime = responseTime;
    }

    public String toString() {
        return "Resolution " + resolution + "\n" + "Refresh rate: " + refreshRate + "\n" + "Response time: " + responseTime;
    }

}
