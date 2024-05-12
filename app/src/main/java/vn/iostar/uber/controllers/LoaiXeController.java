package vn.iostar.uber.controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import vn.iostar.uber.models.LoaiXe;

public class LoaiXeController {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    StorageReference myRefStora= FirebaseStorage.getInstance().getReference();

    ArrayList<LoaiXe> list=new ArrayList<LoaiXe>();
    public interface DataRetrievedCallback_LoaiXe {
        void onDataRetrieved(ArrayList<LoaiXe> listLoaiXe);
    }

    public void getListLoaiXe(DataRetrievedCallback_LoaiXe callbackLopHoc){
        myRef.child("loaiXe").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("LoaiXe", "Error getting data", task.getException());
                }
                else {
                    Log.d("LoaiXe", String.valueOf(task.getResult().getValue()));
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        String idLoaiXe = dataSnapshot.getKey();
                        String tenLoaiXe = dataSnapshot.child("tenLoaiXe").getValue(String.class);
                        String gia = dataSnapshot.child("gia").getValue(String.class);

                        assert gia != null;
                        LoaiXe loaiXe=new LoaiXe(idLoaiXe,tenLoaiXe,Float.valueOf(gia));
                        list.add(loaiXe);

                    }
                    // Gọi callback với danh sách dữ liệu đã lấy được
                    callbackLopHoc.onDataRetrieved(list);

                }

//                return false;
            }
        });

    }
}
