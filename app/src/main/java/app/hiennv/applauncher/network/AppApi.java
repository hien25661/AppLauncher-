package app.hiennv.applauncher.network;

import java.util.List;

import app.hiennv.applauncher.widgets.location.Country;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by nguyenvanhien on 5/4/18.
 */

public interface AppApi {

    @GET("name/{country_name}")
    Call<List<Country>> getCountryName(@Path("country_name") String country_name);
}
