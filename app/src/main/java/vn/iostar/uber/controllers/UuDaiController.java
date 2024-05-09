package vn.iostar.uber.controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import vn.iostar.uber.models.LoaiXe;
import vn.iostar.uber.models.UuDai;

public class UuDaiController {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    StorageReference myRefStora= FirebaseStorage.getInstance().getReference();

    ArrayList<UuDai> list=new ArrayList<UuDai>();
    public interface DataRetrievedCallback_UuDai {
        void onDataRetrieved(ArrayList<UuDai> listUuDai);
    }

    public void getListUuDai(UuDaiController.DataRetrievedCallback_UuDai callbackUuDai){
        myRef.child("uuDai").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("UuDai", "Error getting data", task.getException());
                }
                else {
                    Log.d("UuDai", String.valueOf(task.getResult().getValue()));
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        String idUudai = dataSnapshot.getKey();
                        String uuDai = dataSnapshot.child("uuDai").getValue(String.class);
                        String moTa = dataSnapshot.child("moTa").getValue(String.class);
                        String thoiGian = dataSnapshot.child("thoiGian").getValue(String.class);
                        String soLuong = dataSnapshot.child("soLuong").getValue(String.class);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
                        Date parsedDate = null;
                        try {
                            parsedDate = dateFormat.parse(thoiGian);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                        assert uuDai != null;
                        UuDai uuDai1 = new UuDai(idUudai,timestamp,uuDai,moTa,soLuong);
                        list.add(uuDai1);

                    }
                    // Gọi callback với danh sách dữ liệu đã lấy được
                    callbackUuDai.onDataRetrieved(list);

                }

            }
        });

    }
}
