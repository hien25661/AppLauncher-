package app.hiennv.applauncher.network;

import android.util.Log;

import java.util.List;

import app.hiennv.applauncher.widgets.location.Country;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiService  {
    private static ApiService instance;
    public static AppApi appApi;
    public static final String APP_BASE_URL = "https://restcountries.eu/rest/v2/";

    public static AppApi getService(){
        if(appApi == null){
            appApi = getRetrofit().create(AppApi.class);
        }
        return appApi;
    }

    public static ApiService getInstance() {
        if(instance == null) {
            instance = new ApiService();
        }
        return instance;
    }

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(APP_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    public void getCountryInformation(String countryName, final loadCountryInforCallBack callBack ){
        getService().getCountryName(countryName).enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if(response!=null && response.body()!=null && response.body().size()>0){
                    Country country = response.body().get(0);
                    if(country!=null){
                        callBack.loadCountryInforSuccess(country);
                        return;
                    }
                }
                callBack.loadCountryInforFailed();
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.e("VVVV",""+t.getMessage());
            }
        });
    }

    public interface loadCountryInforCallBack {
        void loadCountryInforSuccess(Object... params);
        void loadCountryInforFailed();
    }
}
