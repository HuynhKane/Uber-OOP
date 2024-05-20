package vn.iostar.uber.controllers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import vn.iostar.uber.models.UuDai;
import vn.iostar.uber.models.YeuCauDatXe;
import vn.iostar.uber.ui.home.home;

public class YeuCauDatXeController {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    ArrayList<YeuCauDatXe> list=new ArrayList<YeuCauDatXe>();
    FinalBookingController finalBookingController=new FinalBookingController();
    Double closest=-1.0; String keyClosest;String city;
    public interface Callback_Bool {
        void onSuccess();

    }
    public interface Callback_YeuCauDatXe {
        void onSuccess(YeuCauDatXe first);
        void onFail();
    }
    public interface Callback {
        void onSuccess() ;
        void onFail();

    }
    public interface Retriver_Client {
        void onSuccess(String idClient);
        void onFail();

    }

    public void addNewYeuCauDatXe(YeuCauDatXe yeuCauDatXe, Context context,Callback_Bool callback){
        FirebaseUser current= firebaseAuth.getCurrentUser();
        Geocoder geocoder = new Geocoder(context,Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(home.from.latitude,home.from.longitude, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String cityName =addressList.get(0).getLocality();
        city=cityName;

        myRef.child("yeuCauDatXe").child(cityName).child(current.getUid()).child(String.valueOf(System.currentTimeMillis())).setValue(yeuCauDatXe).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onSuccess();
            }
        });
    }


    public void getIdFirstDriver(Context context,Retriver_Client callback) throws IOException {
        Geocoder geocoder = new Geocoder(context,Locale.getDefault());
        List<Address> addressList;
        addressList = geocoder.getFromLocation(home.from.latitude,home.from.longitude, 1);
        String cityName =addressList.get(0).getLocality();
        DatabaseReference cityRef = myRef.child("driverLocation").child(cityName);

        cityRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    cityRef.orderByKey().limitToLast(1).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Lấy dữ liệu từ snapshot
                                String idClient = snapshot.getKey();
                                callback.onSuccess(idClient);
                            }
                        }
                    });

                } else {
                    callback.onFail();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




    public void consider_room(Context context, String idDriver,String idClient, Callback callback) throws IOException {
        Geocoder geocoder = new Geocoder(context,Locale.getDefault());
        List<Address> addressList;
        addressList = geocoder.getFromLocation(home.from.latitude,home.from.longitude, 1);
        String cityName =addressList.get(0).getLocality();
        DatabaseReference cityRef = myRef.child("driverLocation").child(cityName);

        cityRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // driverLocation tồn tại
                    String time = String.valueOf(System.currentTimeMillis());
                    Map<String, Object> map = new HashMap<>();
                    map.put("client/" + time + "/idclient", idClient);
                    cityRef.child(idDriver).updateChildren(map);
                    callback.onSuccess();

                } else {
                    callback.onFail();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void listenClient(Context context,LatLng curPos, String idDriver, Retriver_Client callback) throws IOException {

        Geocoder geocoder = new Geocoder(context,Locale.getDefault());
        List<Address> addressList;
        addressList = geocoder.getFromLocation(curPos.latitude,curPos.longitude, 1);
        String cityName =addressList.get(0).getLocality();
        city=cityName;
        DatabaseReference cityRef = myRef.child("driverLocation").child(cityName);

        cityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    cityRef.child(idDriver).child("client").orderByKey().limitToLast(1).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Lấy dữ liệu từ snapshot
                                String idClient = snapshot.child("idclient").getValue(String.class);
                                callback.onSuccess(idClient);
                            }
                        }
                    });

                } else {
                    callback.onFail();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void acceptThisClient(Context context,LatLng curPos,String idDriver, String idClient,Callback callback) throws IOException {
        Geocoder geocoder = new Geocoder(context,Locale.getDefault());
        List<Address> addressList;
        addressList = geocoder.getFromLocation( curPos.latitude, curPos.longitude, 1);
        String cityName =addressList.get(0).getLocality();
        DatabaseReference cityRef =myRef.child("yeuCauDatXe").child(cityName).child(idClient);

        Map<String, Object> map = new HashMap<>();
        map.put("/idDriver", idDriver);
        cityRef.child(String.valueOf(System.currentTimeMillis())).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onSuccess();
            }
        });


    }
    public void denyThisClient(Context context,String idDriver, String idClient,Callback callback) {
        Geocoder geocoder = new Geocoder(context,Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(home.from.latitude,home.from.longitude, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String cityName =addressList.get(0).getLocality();
        DatabaseReference cityRef =myRef.child("driverLocation").child(cityName).child(idDriver).child("client");
        cityRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key=snapshot.getKey();
                        String id = snapshot.child("idclient").getValue(String.class);
                        if(id.equals(idClient)){
                            cityRef.child(key).removeValue();
                            break;
                        }
                        callback.onSuccess();
                    }

                } else {
                    callback.onFail();
                }

            }
        });

    }

    public void foundDriver(Context context, String idClient,Retriver_Client callback) throws IOException {
        Geocoder geocoder = new Geocoder(context,Locale.getDefault());
        List<Address> addressList;
        addressList = geocoder.getFromLocation( home.from.latitude, home.from.longitude, 1);
        String cityName =addressList.get(0).getLocality();
        DatabaseReference cityRef =myRef.child("yeuCauDatXe").child(cityName).child(idClient);
        cityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    cityRef.orderByKey().limitToLast(1).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            String idDriver = snapshot.child("idDriver").getValue(String.class);
                            callback.onSuccess(idDriver);
                        }
                    });

                } else {
                    callback.onFail();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
