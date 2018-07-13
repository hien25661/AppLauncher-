package app.hiennv.applauncher.widgets.location;

public class Translation {
    private String countryCode;
    private String countryNameTranslated;

    public Translation(String countryCode, String countryNameTranslated) {
        this.countryCode = countryCode;
        this.countryNameTranslated = countryNameTranslated;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryNameTranslated() {
        return countryNameTranslated;
    }

    public void setCountryNameTranslated(String countryNameTranslated) {
        this.countryNameTranslated = countryNameTranslated;
    }
}
