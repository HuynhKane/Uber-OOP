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
        void onSuccess();
        void onFail();

    }
    public interface Retriver_Client {
        void onSuccess(String idClient);
        void onFail();

    }

    public void addNewYeuCauDatXe(YeuCauDatXe yeuCauDatXe, Context context,Callback_Bool callback) throws IOException {
        FirebaseUser current= firebaseAuth.getCurrentUser();
        Geocoder geocoder = new Geocoder(context);
        List<Address> addressList;
        addressList = geocoder.getFromLocation(home.from.latitude,home.from.longitude, 1);
        String cityName =addressList.get(0).getLocality();
        city=cityName;

        myRef.child("yeuCauDatXe").child(cityName).child(String.valueOf(System.currentTimeMillis())).setValue(yeuCauDatXe).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onSuccess();
            }
        });
    }

    public void getYeuCauDatXeXom_City(String cityName,Callback_YeuCauDatXe callbackYeuCauDatXe){
        myRef.child("yeuCauDatXe").child(cityName).orderByKey().limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Lấy dữ liệu từ snapshot
                    YeuCauDatXe yeuCauDatXe = snapshot.getValue(YeuCauDatXe.class);
                    if (yeuCauDatXe != null) {
                        callbackYeuCauDatXe.onSuccess(yeuCauDatXe);

                    }
                    else {
                        callbackYeuCauDatXe.onFail();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void distant(LatLng client,String cityName){
         closest=-1.0;

        myRef.child("driverLocation").child(cityName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        String key = childSnapshot.getKey();
                        Double  lat= childSnapshot.child("l").child("0").getValue(Double .class);
                        Double  lng= childSnapshot.child("l").child("1").getValue(Double .class);
                        Double  distance= finalBookingController.calculateDistanceInKm(client,new LatLng(lat,lng));
                        if(distance>closest){
                            closest=distance;
                            keyClosest=key;
                        }
                        ;}

                    } else {
                    Log.d("sssssss", "Không có dữ liệu cho thành phố này.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void deleteYeuCauDatXe(String cityName, String idYC,Callback_Bool callbackBool){
        myRef.child("yeuCauDatXe").child(cityName).child(idYC).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callbackBool.onSuccess();
            }
        });
    }

    public void consider_room(String cityName, String idDriver,String idClient, Callback callback) {
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
    public void listenClient(String cityName, String idDriver, Retriver_Client callback) {
        DatabaseReference cityRef = myRef.child("driverLocation").child(cityName);

        cityRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    cityRef.child(idDriver).child("client").orderByKey().limitToFirst(1).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
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

}
