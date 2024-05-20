package vn.iostar.uber.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import vn.iostar.uber.R;
import vn.iostar.uber.activitys.HomeActivity;
import vn.iostar.uber.activitys.MainActivityDriver;
import vn.iostar.uber.callback.IFirebaseDriverInforListener;
import vn.iostar.uber.callback.IFirebaseFailedListener;
import vn.iostar.uber.databinding.FragmentMapBinding;
import vn.iostar.uber.models.GeoQueryModel;
import vn.iostar.uber.models.TaiXe;
import vn.iostar.uber.models.ViTri;

public class MapFragment extends Fragment implements OnMapReadyCallback, IFirebaseDriverInforListener, IFirebaseFailedListener {

    private FragmentMapBinding binding;
    //Common
    public static Set<ViTri> driverFound = new HashSet<ViTri>();
    public static HashMap<String, Marker> markerList = new HashMap<>();


    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    //load driver
    private double distance = 1.0;
    private static final double LIMIT_RANGE = 100000000000000.0;//km

    private Location previousLoc, curLoc;
    private DataSnapshot dataSnapshot;

    private boolean firstTime = true;
    private String cityName;


    IFirebaseDriverInforListener iFirebaseDriverInforListener;
    IFirebaseFailedListener iFirebaseFailedListener;

    private String role = HomeActivity.role;
    private DatabaseError databaseError;

    //online system
    DatabaseReference onlineRef, currentUserRef, driverLocationRef;
    GeoFire geoFire;
    ValueEventListener onlineValueEnventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists() && currentUserRef != null) {
                currentUserRef.onDisconnect().removeValue();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Snackbar.make(mapFragment.getView(), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();

        }
    };


    @Override
    public void onDestroy() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        geoFire.removeLocation(FirebaseAuth.getInstance().getCurrentUser().getUid());
        onlineRef.removeEventListener(onlineValueEnventListener);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        registerOnlineSystem();
    }

    private void registerOnlineSystem() {
       if(role.equals("driver")){
           onlineRef.addValueEventListener(onlineValueEnventListener);
       }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MapViewModel mapViewModel =
                new ViewModelProvider(this).get(MapViewModel.class);
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        init();
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);


        return root;
    }

    private void init() {

        iFirebaseDriverInforListener = this;
        iFirebaseFailedListener = this;


        onlineRef = FirebaseDatabase.getInstance().getReference().child(".info/connected");
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }





        locationRequest = new LocationRequest();
        locationRequest.setSmallestDisplacement(10f);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LatLng newPosition = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 18f));

                if(role.equals("driver")){
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    List<Address> addressList;
                    try{
                        addressList = geocoder.getFromLocation(locationResult.getLastLocation().getLatitude(),
                                locationResult.getLastLocation().getLongitude(), 1);
                        String cityName = addressList.get(0).getLocality();
                        driverLocationRef = FirebaseDatabase.getInstance().getReference("driverLocation")
                                .child(cityName);
                        currentUserRef = driverLocationRef.child(FirebaseAuth.getInstance().getUid());
                        geoFire = new GeoFire(driverLocationRef);


                        //udpate location
                        geoFire.setLocation(FirebaseAuth.getInstance().getCurrentUser().getUid(), new GeoLocation(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude()),
                                (key, error) -> {
                                    if (error != null)
                                        Snackbar.make(mapFragment.getView(), databaseError.getMessage(), Snackbar.LENGTH_LONG).show();
                                    else
                                    {
                                        Snackbar snackbar = Snackbar.make(mapFragment.getView(), "You're online", Snackbar.LENGTH_LONG);
                                        snackbar.show();

                                    }
                                });

                        registerOnlineSystem();

                    } catch (IOException e) {
                        Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                    if (firstTime) {
                        previousLoc = curLoc = locationResult.getLastLocation();
                        firstTime = false;
                    } else {
                        previousLoc = curLoc;
                        curLoc = locationResult.getLastLocation();
                    }

                    if (previousLoc.distanceTo(curLoc) / 1000 <= LIMIT_RANGE) {
                        loadAvailableDrivers();
                    } else {

                    }
                }
                //here, after set location, get address city name



            }
        };

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());


        //loadAvailableDrivers();

    }

    private void loadAvailableDrivers() {

        if(getContext()==null){
            Log.d("CONTEX","nullllll");
        }

        if (role.equals("client") || ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(getView(), getString(R.string.permission_require), Snackbar.LENGTH_SHORT).show();
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnFailureListener(e -> Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show())
                .addOnSuccessListener(location -> {
                    //Load all driver
                    Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                    List<Address> addressList;

                    try {
                        addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                        cityName = addressList.get(0).getLocality();

                        //Query

                        DatabaseReference driver_location_ref = FirebaseDatabase.getInstance()
                                .getReference("DriverLocations")
                                .child(cityName);

                        GeoFire gf = new GeoFire(driver_location_ref);
                        GeoQuery geoQuery = gf.queryAtLocation(new GeoLocation(location.getLatitude(),location.getLongitude()),distance);

                        geoQuery.removeAllListeners();

                        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                            @Override
                            public void onKeyEntered(String key, GeoLocation location) {
                                driverFound.add(new ViTri(key, location));

                            }

                            @Override
                            public void onKeyExited(String key) {

                            }

                            @Override
                            public void onKeyMoved(String key, GeoLocation location) {

                            }

                            @Override
                            public void onGeoQueryReady() {
                                if(distance <= LIMIT_RANGE){
                                    distance++;
                                    loadAvailableDrivers();
                                }
                                else{
                                    distance = 1.0;
                                    addDriverMarker();
                                }

                            }

                            @Override
                            public void onGeoQueryError(DatabaseError error) {
                                Snackbar.make(getView(),error.getMessage(), Snackbar.LENGTH_SHORT).show();

                            }
                        });

                        driver_location_ref.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                GeoQueryModel geoQueryModel = dataSnapshot.getValue(GeoQueryModel.class);
                                GeoLocation geoLocation = new GeoLocation(geoQueryModel.getL().get(0),
                                        geoQueryModel.getL().get(1));
                                ViTri viTri = new ViTri(dataSnapshot.getKey(), geoLocation);
                                Location newDriverLocation = new Location("");
                                newDriverLocation.setLatitude(geoLocation.latitude);
                                newDriverLocation.setLongitude(geoLocation.longitude);

                                float newDistance = location.distanceTo(newDriverLocation)/1000;

                                if(newDistance <= LIMIT_RANGE){
                                    findDriverByKey(viTri);
                                }

                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                    } catch (IOException e) {
                        e.printStackTrace();
                        Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }

                });
    }

    @SuppressLint("CheckResult")
    private void addDriverMarker() {
        if(driverFound.size() > 0){
            Observable.fromIterable(driverFound)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(viTri -> {

                        findDriverByKey(viTri);
                    }, throwable -> {
                        Snackbar.make(getView(),throwable.getMessage(), Snackbar.LENGTH_SHORT).show();
                    },()->{});
        }
        else {
            Snackbar.make(getView(),getString(R.string.driver_not_found), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void findDriverByKey(ViTri viTri) {
        FirebaseDatabase.getInstance()
                .getReference("driver")
                .child(viTri.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(dataSnapshot.hasChildren()){
                            viTri.setDriverInforModel(dataSnapshot.getValue(TaiXe.class));
                            iFirebaseDriverInforListener.onDriverInforLoadSuccess(viTri);
                        }
                        else
                            iFirebaseFailedListener.onFirebaseFailed(getString(R.string.not_found_key) + viTri.getKey());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iFirebaseFailedListener.onFirebaseFailed(databaseError.getMessage());

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        Dexter.withContext(getContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            Snackbar.make(getView(),getString(R.string.permission_require), Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                            @Override
                            public boolean onMyLocationButtonClick() {
                                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    return false;
                                }
                                fusedLocationProviderClient.getLastLocation()
                                        .addOnFailureListener(e -> Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show())
                                        .addOnSuccessListener(location -> {
                                            LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 18f));

                                        });
                                return false;
                            }
                        });

                        View locationButton = ((View)mapFragment.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP,0);
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                        params.setMargins(0,0,0,250);



                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Snackbar.make(getView(), permissionDeniedResponse.getPermissionName() + " need enale", Snackbar.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();


        mMap.getUiSettings().setZoomControlsEnabled(true);
        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.uber_maps_style));
            if(!success)
                Snackbar.make(getView(), "Load map style failed", Snackbar.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDriverInforLoadSuccess(ViTri driverGeoModel) {
        if(!markerList.containsKey(driverGeoModel.getKey())){
            markerList.put(driverGeoModel.getKey(), mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(driverGeoModel.getGeoLocation().latitude,
                            driverGeoModel.getGeoLocation().longitude))
                    .flat(true)
                    .title(buildName(driverGeoModel.getDriverInforModel().getTen(),
                            driverGeoModel.getDriverInforModel().getIdXe()))
                    .snippet(driverGeoModel.getDriverInforModel().getSdt())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car))));
        }
        if(!TextUtils.isEmpty(cityName)){
            DatabaseReference driverLocation = FirebaseDatabase.getInstance()
                    .getReference("DriverLocations")
                    .child(cityName)
                    .child(driverGeoModel.getKey());
            driverLocation.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!dataSnapshot.hasChildren()){
                        if(markerList.get(driverGeoModel.getKey()) != null){
                            markerList.get(driverGeoModel.getKey()).remove();
                        }
                        markerList.remove(driverGeoModel.getKey());
                        driverLocation.removeEventListener(this);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Snackbar.make(getView(), databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();

                }
            });
        }


    }

    private String buildName(String ten, String idXe) {
        return new StringBuilder(ten).append("|").append(idXe).toString();
    }

    @Override
    public void onFirebaseFailed(String message) {
        Snackbar.make(getView(),message, Snackbar.LENGTH_SHORT).show();

    }
}