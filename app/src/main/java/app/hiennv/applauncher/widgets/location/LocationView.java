package app.hiennv.applauncher.widgets.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import app.hiennv.applauncher.R;
import app.hiennv.applauncher.network.ApiService;

public class LocationView extends Fragment implements LocationListener, ApiService.loadCountryInforCallBack {
    private static final int REQUEST_CODE = 100;
    private Context mContext;
    private LocationManager mLocationManager;
    private String mProvider;
    private TextView tvCountry, tvCapital, tvCurrency;
    private String mCountryName, mCurrentLanguageCode;
    private Country mCountry;

    public static LocationView newInstance() {
        LocationView fragment = new LocationView();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_location, container, false);
        mContext = view.getContext();
        initView(view);
        init(mContext);
        return view;
    }

    private void initView(View v) {
        tvCountry = (TextView) v.findViewById(R.id.tvCountryName);
        tvCapital = (TextView) v.findViewById(R.id.tvCapital);
        tvCurrency = (TextView) v.findViewById(R.id.tvCurrency);
    }

    @SuppressLint("MissingPermission")
    private void init(Context context) {
        mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        mProvider = mLocationManager.getBestProvider(criteria, false);
        Location location = getLastKnownLocation();
        updateLocationInformation(location);

        if (isAllowedPermission()) {
            mLocationManager.requestLocationUpdates(mProvider, 400, 1, this);
        }

    }

    private void updateLocationInformation(Location location) {
        if (location == null) return;
        getCountryNameFromLocation(location.getLatitude(), location.getLongitude());
        requestApiGetInformationCountry(mCountryName);
    }

    private void requestApiGetInformationCountry(String countryName) {
        if (TextUtils.isEmpty(countryName)) return;
        ApiService.getInstance().getCountryInformation(countryName, this);
    }


    @Override
    public void onLocationChanged(Location location) {
        updateLocationInformation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    /**
     * @return location coordinate
     */
    private Location getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    /**
     * @param lat
     * @param lng
     */
    private void getCountryNameFromLocation(double lat, double lng) {
        Geocoder geocoder = new Geocoder(mContext, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            mCountryName = obj.getCountryName();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param params
     */
    @Override
    public void loadCountryInforSuccess(Object... params) {
        mCountry = (Country) params[0];
        setTranslateCountryName(mCountry);
        setCurrencyInfor(mCountry);
        setCapitalName(mCountry);
    }

    /**
     * loadCountryInforFailed
     */
    @Override
    public void loadCountryInforFailed() {

    }


    /**
     * @param mCountry
     */
    private void setTranslateCountryName(Country mCountry) {
        if (mCountry == null || mCountry.getTranslations() == null)
            return;
        mCountryName = mCountry.getName();

        JsonObject parentObject = mCountry.getTranslations();
        mCurrentLanguageCode = Locale.getDefault().getLanguage();

        if (parentObject.get(mCurrentLanguageCode) != null && !TextUtils.isEmpty(parentObject.get(mCurrentLanguageCode).getAsString())) {
            mCountryName = parentObject.get(mCurrentLanguageCode).getAsString();
        }
        tvCountry.setText(mCountryName);
    }

    /**
     * @param mCountry
     */
    private void setCurrencyInfor(Country mCountry) {
        if (mCountry == null || mCountry.getCurrencies() == null || mCountry.getCurrencies().size() == 0)
            return;
        Currency mCurrency = mCountry.getCurrencies().get(0);
        tvCurrency.setText(mCurrency.getCode());
    }

    /**
     * @param mCountry
     */
    private void setCapitalName(Country mCountry) {
        if (mCountry == null || mCountry.getCapital() == null) return;
        tvCapital.setText(mCountry.getCapital());
    }

    /**
     * @return true: PERMISSION_GRANTED, false: need to request permission
     */
    private boolean isAllowedPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
                return false;
            }
        }
        return true;
    }



    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted successfully
                    mLocationManager.requestLocationUpdates(mProvider, 400, 1, this);
                } else {
                    //permission denied
                }
                break;
        }
    }

}
