package vn.iostar.uber.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
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
import com.google.maps.android.PolyUtil;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import vn.iostar.uber.ui.home.home;

public class MapFragment extends Fragment implements OnMapReadyCallback, IFirebaseDriverInforListener {

    private FragmentMapBinding binding;
    //Common
    public static Set<ViTri> driverFound = new HashSet<ViTri>();
    public static HashMap<String, Marker> markerList = new HashMap<>();


    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private DataSnapshot dataSnapshot;

    private boolean firstTime = true;


    IFirebaseDriverInforListener iFirebaseDriverInforListener;
    IFirebaseFailedListener iFirebaseFailedListener;

    private String role = HomeActivity.role;
    private DatabaseError databaseError;

    //online system
    DatabaseReference  currentUserRef, driverLocationRef;
    GeoFire geoFire;



    @Override
    public void onDestroy() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        geoFire.removeLocation(FirebaseAuth.getInstance().getCurrentUser().getUid());
 //       onlineRef.removeEventListener(onlineValueEnventListener);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

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

       // onlineRef = FirebaseDatabase.getInstance().getReference().child(".info/connected");
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
//                LatLng newPosition = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 18f));

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

                      //  registerOnlineSystem();

                    } catch (IOException e) {
                        Snackbar.make(getView(), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
//                    if (firstTime) {
//                        previousLoc = curLoc = locationResult.getLastLocation();
//                        firstTime = false;
//                    } else {
//                        previousLoc = curLoc;
//                        curLoc = locationResult.getLastLocation();
//                    }


                }

            }
        };

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        //addDriverMarker();
    }


//    @SuppressLint("CheckResult")
//    private void addDriverMarker() {
//        if(driverFound.size() > 0){
//            Observable.fromIterable(driverFound)
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(viTri -> {
//                      //  findDriverByKey(viTri);
//                    }, throwable -> {
//                        Snackbar.make(getView(),throwable.getMessage(), Snackbar.LENGTH_SHORT).show();
//                    },()->{});
//        }
//    }

//    private void findDriverByKey(ViTri viTri) {
//        FirebaseDatabase.getInstance()
//                .getReference("driver")
//                .child(viTri.getKey())
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(dataSnapshot.hasChildren()){
//                            viTri.setDriverInforModel(dataSnapshot.getValue(TaiXe.class));
//                            onDriverInforLoadSuccess(viTri);
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//
//                    }
//                });
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

       // mMap.getUiSettings().setZoomControlsEnabled(true);

        if(home.to!=null && home.from!=null){
            zoomout();
            drawRoute();
            mMap.clear();
            MarkerOptions options = new MarkerOptions();
            options.position(home.to);
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mMap.addMarker(options);
            options.position(home.from);
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(options);
        } //mark 2 điểm on nmap
    }

    private void zoomout(){
        // Tạo đối tượng LatLngBounds.Builder
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(home.to);
        builder.include(home.from);
        LatLngBounds bounds = builder.build();
        int padding = 100; // khoảng cách padding từ mép bản đồ
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }
    private void drawRoute(){
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + home.from.latitude + "," + home.from.longitude + "&destination=" + home.to.latitude + "," + home.to.longitude + "&key=" + getString(R.string.api_key);

        // Sử dụng thư viện Volley để gửi yêu cầu HTTP
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Phân tích dữ liệu phản hồi
                            JSONArray routes = response.getJSONArray("routes");
                            if (routes.length() == 0) {
                                Log.e("drawRoute", "Không tìm thấy tuyến đường nào");
                                return;
                            }
                            JSONObject route = routes.getJSONObject(0);
                            JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                            String points = overviewPolyline.getString("points");

                            // Vẽ tuyến đường trên bản đồ
                            List<LatLng> decodedPath = PolyUtil.decode(points);
                            mMap.addPolyline(new PolylineOptions().addAll(decodedPath).color(Color.BLUE).width(10));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("drawRoute", "Lỗi phân tích JSON: " + e.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("drawRoute", "Lỗi yêu cầu HTTP: " + error.getMessage());
            }
        });

        // Thêm yêu cầu vào hàng đợi
        requestQueue.add(jsonObjectRequest);
    }


    @Override
    public void onDriverInforLoadSuccess(ViTri driverGeoModel) {
//        if(!markerList.containsKey(driverGeoModel.getKey())){
//            markerList.put(driverGeoModel.getKey(), mMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(driverGeoModel.getGeoLocation().latitude,
//                            driverGeoModel.getGeoLocation().longitude))
//                    .flat(true)
//                    .title(buildName(driverGeoModel.getDriverInforModel().getTen(),
//                            driverGeoModel.getDriverInforModel().getIdXe()))
//                    .snippet(driverGeoModel.getDriverInforModel().getSdt())
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car))));
//        }

    }
    public void off(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        geoFire.removeLocation(FirebaseAuth.getInstance().getCurrentUser().getUid());
      //  onlineRef.removeEventListener(onlineValueEnventListener);
    }

    private String buildName(String ten, String idXe) {
        return new StringBuilder(ten).append("|").append(idXe).toString();
    }

}