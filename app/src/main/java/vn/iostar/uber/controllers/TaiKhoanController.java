package vn.iostar.uber.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    public void SaveAcc(String role) {
        FirebaseUser current= firebaseAuth.getCurrentUser();
        String url=current.getPhotoUrl().toString();
        if(url.isEmpty()){
            url="https://avatar.iran.liara.run/public/boy?username=Ash";
        }

        if(role.equals("client")){
            KhachHang kh=new KhachHang(current.getDisplayName(),current.getPhoneNumber() ,url);
            myRef.child(role).child(current.getUid()).setValue(kh);
        }
        else {
            TaiXe tx=new TaiXe(current.getDisplayName(),"0",current.getPhoneNumber(),"0",url);
            myRef.child(role).child(current.getUid()).setValue(tx);

        }

    }
    public boolean CheckNum(){
        FirebaseUser current= firebaseAuth.getCurrentUser();
         istrue=true;
        myRef.child("client").orderByKey().equalTo(current.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {

                    if(!dataSnapshot.child("sdt").getValue(String.class).equals("0")){
                        istrue=false;
                        break;
                    }
                }

            }
        });
        return istrue;
    }
    public boolean CheckDriverInf(){
        FirebaseUser current= firebaseAuth.getCurrentUser();
        istrue=true;
        myRef.child("driver").orderByKey().equalTo(current.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {

                    if(!dataSnapshot.child("sdt").getValue(String.class).equals("0")
                            && !dataSnapshot.child("cccd").getValue(String.class).equals("0")
                            && !dataSnapshot.child("idXe").getValue(String.class).equals("0")){
                        istrue=false;
                        break;
                    }
                }

            }
        });
        return istrue;
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
        myRef.child("driver").child(current.getUid()).setValue(tx).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Xe xe=new Xe(myRef.push().getKey(),bienSo,loaiXe);
                myRef.child("driver").child(current.getUid()).child("idXe").setValue(xe);
                progressDialog.dismiss();
            }
        });
    }
}
