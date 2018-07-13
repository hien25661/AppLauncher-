package app.hiennv.applauncher.widgets.location;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Country {
    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("capital")
    @Expose
    private String capital;

    @SerializedName("currencies")
    @Expose
    private List<Currency> currencies = null;

    @SerializedName("translations")
    @Expose
    private JsonObject translations;


    public String getName() {
        return name;
    }

    public String getCapital() {
        return capital;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public JsonObject getTranslations() {
        return translations;
    }
}
