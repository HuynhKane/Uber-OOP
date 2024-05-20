package vn.iostar.uber.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import vn.iostar.uber.activitys.client.RegisterClientActivity;
import vn.iostar.uber.models.KhachHang;
import vn.iostar.uber.models.TaiXe;
import vn.iostar.uber.models.Xe;

public class TaiKhoanController {
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    boolean istrue;

    String idClient;
//    public void SaveAcc(String role) {
//        FirebaseUser current= firebaseAuth.getCurrentUser();
//        String url=current.getPhotoUrl().toString();
//        if(url.isEmpty()){
//            url="https://avatar.iran.liara.run/public/boy?username=Ash";
//        }
//
//        if(role.equals("client")){
//            myRef.child(role).child(current.getUid()).child("ten").setValue(current.getDisplayName() );
//            myRef.child(role).child(current.getUid()).child("urlAva").setValue(url );
//        }
//        else {
//            TaiXe tx=new TaiXe(current.getDisplayName(),url);
//            myRef.child(role).child(current.getUid()).setValue(tx);
//
//        }
//
//    }
    public interface DataRetrievedCallback_Bool {
        void onDataRetrieved(boolean num);
    }

    public void CheckNum(DataRetrievedCallback_Bool callbackBool){
        FirebaseUser current= firebaseAuth.getCurrentUser();
         istrue=true;
         myRef.child("client").orderByKey().equalTo(current.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebasesssss", "Error getting data", task.getException());
                }
                else {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        Iterator<DataSnapshot> iterator = task.getResult().getChildren().iterator();
                        if (iterator.hasNext()) {
                            DataSnapshot dataSnapshot = iterator.next();
                            String sdt = dataSnapshot.child("sdt").getValue(String.class);
                            String ten = dataSnapshot.child("ten").getValue(String.class);
                            if (sdt != null) {
                                istrue = false;
                            }
                        }
                    }
                }
                callbackBool.onDataRetrieved(istrue);
            }
        });

    }
    public void CheckDriverInf(DataRetrievedCallback_Bool callbackBool){
        FirebaseUser current= firebaseAuth.getCurrentUser();
        istrue=true;
        myRef.child("driver").orderByKey().equalTo(current.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult().getChildren().iterator().next();

                if(dataSnapshot.child("sdt").getValue(String.class)!=null
                            && dataSnapshot.child("cccd").getValue(String.class)!=null
                            && dataSnapshot.child("idXe").getValue()!=null){
                        istrue=false;

                    }
                callbackBool.onDataRetrieved(istrue);

            }
        });
    }
    public void UpdateAcc_Client( String newName,String newNum,Context context){
        ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseUser current= firebaseAuth.getCurrentUser();
        KhachHang kh=new KhachHang(newName,newNum,current.getPhotoUrl().toString());
        myRef.child("client").child(current.getUid()).setValue(kh).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
            }
        });
    }
    public void UpdateAcc_Driver( String newName,String newNum,String newCccd, String bienSo, String loaiXe,Context context){
        ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        FirebaseUser current= firebaseAuth.getCurrentUser();
        TaiXe tx=new TaiXe(current.getDisplayName(),newCccd,current.getPhoneNumber(),"0",current.getPhotoUrl().toString());


        Map<String, Object> updates = new HashMap<>();
        updates.put("driver/" + current.getUid() + "/ten",newName);
        updates.put("driver/" + current.getUid() + "/sdt", newNum);
        updates.put("driver/" + current.getUid() + "/cccd", newCccd);
        myRef.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Xe xe=new Xe(myRef.push().getKey(),bienSo,loaiXe);
                myRef.child("driver").child(current.getUid()).child("idXe").setValue(xe);
                progressDialog.dismiss();
            }
        });

    }
}
