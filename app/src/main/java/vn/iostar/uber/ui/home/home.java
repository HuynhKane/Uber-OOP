package vn.iostar.uber.ui.home;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import vn.iostar.uber.R;
import vn.iostar.uber.activitys.client.Map_TypeVehicalActivity;
import vn.iostar.uber.activitys.client.VoucherActivity;
import vn.iostar.uber.databinding.ActivityHomeBinding;

public class home extends Fragment {

    private HomeViewModel mViewModel;
    AutocompleteSupportFragment fromFragment ;
    AutocompleteSupportFragment toFragment ;

    LinearLayout btn_next;
    public static Location bookingInf;
    public static LatLng from;
    public static LatLng to;

    public static home newInstance() {
        return new home();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home_client, container, false);
        initializePlaces();
        setupAutocompleteFragment();
        handleNext(rootView);

        return rootView;

    }

    private void handleNext(View rootView) {
        btn_next = rootView.findViewById(R.id.next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Map_TypeVehicalActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializePlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), getString(R.string.api_key));
        }

    }

    private void setupAutocompleteFragment() {
        fromFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.from_fragment);
        fromFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME));
        fromFragment.setHint("From");
         fromFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Log.d("error", "Error occurred: " + status);
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if (place.getLatLng() != null) {
                    LatLng latLng = place.getLatLng();
                    from=latLng;
                    Log.d("latLngFrom", latLng.toString());

                }
            }
        });
        getCurrentLocation();

        toFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.to_fragment);
        toFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME));
        toFragment.setHint("To");
        toFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Log.d("error", "Error occurred: " + status);
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if (place.getLatLng() != null) {
                    LatLng latLng = place.getLatLng();
                    to=latLng;
                    Log.d("latLngTo", latLng.toString());
                }
            }
        });
    }


    // Lấy vị trí hiện tại của thiết bị
    private void getCurrentLocation() {

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnFailureListener(e -> Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show())
                .addOnSuccessListener(location -> {
                    from = new LatLng(location.getLatitude(), location.getLongitude());
                    fromFragment.setText(GeocodingHelper.getAddressFromLatLng(getActivity(),from));
                    Log.d("latLnini", from.toString());
                });

    }

    public class GeocodingHelper {

        public static String getAddressFromLatLng(android.content.Context context, LatLng latLng) {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            String addressText = "";

            try {
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                        addressText += address.getAddressLine(i);
                        if (i < address.getMaxAddressLineIndex()) {
                            addressText += ", ";
                        }
                    }
                }
            } catch (IOException e) {
                Log.e("GeocodingHelper", "Error getting address from location", e);
            }

            return addressText;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // TODO: Use the ViewModel
    }

}