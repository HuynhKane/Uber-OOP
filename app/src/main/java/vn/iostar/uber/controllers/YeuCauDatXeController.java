package vn.iostar.uber.controllers;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

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

import vn.iostar.uber.activitys.client.FinalBookingFormActivity;
import vn.iostar.uber.activitys.client.FoundDriverActivity;
import vn.iostar.uber.models.UuDai;
import vn.iostar.uber.models.YeuCauDatXe;
import vn.iostar.uber.ui.home.home;

public class YeuCauDatXeController {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    public static ValueEventListener valueEventListener;
    ArrayList<YeuCauDatXe> list=new ArrayList<YeuCauDatXe>();
    FinalBookingController finalBookingController=new FinalBookingController();
    Double closest=-1.0; String keyClosest;
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
    public interface Retriver_Client_DatabaseReference {
        void onSuccess(String idClient,DatabaseReference myref);
        void onFail();
    }

    public void addNewYeuCauDatXe(YeuCauDatXe yeuCauDatXe, String cityName,Callback_Bool callback){
        FirebaseUser current= firebaseAuth.getCurrentUser();
        myRef.child("yeuCauDatXe").child(cityName).child(current.getUid()).child(String.valueOf(System.currentTimeMillis())).setValue(yeuCauDatXe).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                callback.onSuccess();
            }
        });
    }


    public void getIdClosestDriver(String cityName,String denyDriver,LatLng curPos,Retriver_Client callback) {

        DatabaseReference cityRef = myRef.child("driverLocation").child(cityName);
        cityRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String itsYouu="";
                    double closest=999999999999.0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String key=snapshot.getKey();

                        Double lat= snapshot.child("l").child("0").getValue(Double.class);
                        Double lon= snapshot.child("l").child("1").getValue(Double.class);
                        LatLng latLng= new LatLng(lat,lon);

                        double distance= finalBookingController.calculateDistanceInKm(latLng,curPos);
                       // Log.d("Loiiiiiii", String.valueOf(distance));
                        if(closest>distance && !key.equals(denyDriver)){
                            closest=distance;
                            itsYouu=key;Log.d("Loiiiiiii",key);
                        }

                    }

                    if(!itsYouu.isEmpty() ){
                        callback.onSuccess(itsYouu);
                    }
                    else {
                        callback.onFail();
                    }
                } else {
                    callback.onFail();
                }
            }
        });


    }


    public void consider_room(String cityName, String idDriver,String idClient,LatLng curPos, Callback callback)  {

        DatabaseReference cityRef = myRef.child("driverLocation").child(cityName);

        cityRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // driverLocation tồn tại
                    if(idDriver.isEmpty()){
                        callback.onFail();
                    }
                    else {
                        String time = String.valueOf(System.currentTimeMillis());
                        Map<String, Object> map = new HashMap<>();
                        map.put("client/" + time + "/idclient", idClient);
                        map.put("client/" + time + "/l/0", curPos.latitude);
                        map.put("client/" + time + "/l/1", curPos.longitude);
                        cityRef.child(idDriver).updateChildren(map);
                        callback.onSuccess();
                    }


                } else {
                    callback.onFail();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void listenClient(Context context,LatLng curPos, String idDriver, Retriver_Client_DatabaseReference callback) throws IOException {

        Geocoder geocoder = new Geocoder(context,Locale.getDefault());
        List<Address> addressList;
        addressList = geocoder.getFromLocation(curPos.latitude,curPos.longitude, 1);
        String cityName =addressList.get(0).getLocality();

        DatabaseReference cityRef = myRef.child("driverLocation").child(cityName).child(idDriver);

        valueEventListener= cityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    cityRef.child("client").orderByKey().limitToLast(1).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Lấy dữ liệu từ snapshot
                                String idClient = snapshot.child("idclient").getValue(String.class);
                                callback.onSuccess(idClient,cityRef);
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
        cityRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    cityRef.orderByKey().limitToLast(1).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Lấy dữ liệu từ snapshot
                                String id = snapshot.getKey();
                                Map<String, Object> map = new HashMap<>();
                                map.put("idDriver/", idDriver);
                                cityRef.child(id).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        callback.onSuccess();
                                    }
                                });

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
    public void denyThisClient(Context context,LatLng curPos,String idDriver, String idClient,Callback callback) {
        Geocoder geocoder = new Geocoder(context,Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation( curPos.latitude, curPos.longitude, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String cityName =addressList.get(0).getLocality();
        DatabaseReference cityRef =myRef.child("driverLocation").child(cityName).child(idDriver);
        cityRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.child("client").getChildren()) {
                        String key=snapshot.getKey();

                        String id = snapshot.child("idclient").getValue(String.class);
                        Double lat= snapshot.child("l").child("0").getValue(Double.class);
                        Double lon= snapshot.child("l").child("1").getValue(Double.class);
                        LatLng posClient= new LatLng(lat,lon);
                        if(id.equals(idClient)){
                            cityRef.child("client").child(key).removeValue();// Xóa khách hàng ra khỏi hàng chờ của tài xế này

                            getIdClosestDriver(cityName,idDriver, posClient,new Retriver_Client() { //Tìm khách tài xế khác cho nhỏ này
                                    @Override
                                    public void onSuccess(String idClient) {//Client nhưng mà thiệt ra là driver =)))
                                            consider_room(cityName, idClient, id,posClient, new Callback() {//Add nhỏ này vào phòng chờ tài xế khác
                                                @Override
                                                public void onSuccess() {

                                                }

                                                @Override
                                                public void onFail() {

                                                }
                                            });

                                    }

                                    @Override
                                    public void onFail() {

                                    }
                                });

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
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Lấy dữ liệu từ snapshot
                                String id = snapshot.getKey();
                                String idDriver = snapshot.child("idDriver").getValue(String.class);
                                if(idDriver!=null){
                                    callback.onSuccess(idDriver);
                                }
                                else {
                                    callback.onFail();
                                }
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
