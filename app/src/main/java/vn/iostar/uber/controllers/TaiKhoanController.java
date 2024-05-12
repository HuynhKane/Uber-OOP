package vn.iostar.uber.controllers;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import vn.iostar.uber.models.KhachHang;
import vn.iostar.uber.models.TaiXe;

public class TaiKhoanController {
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    public void SaveAcc(String role) {

        FirebaseUser current= firebaseAuth.getCurrentUser();
        String url=current.getPhotoUrl().toString();
        if(url.isEmpty()){
            url="https://avatar.iran.liara.run/public/boy?username=Ash";
        }

        if(role.equals("client")){
            KhachHang kh=new KhachHang(current.getDisplayName(),"0",url);
            myRef.child(role).child(current.getUid()).setValue(kh);
        }
        else {
            TaiXe tx=new TaiXe(current.getDisplayName(),"0","0","0","0",url);
            myRef.child(role).child(current.getUid()).setValue(tx);
        }



    }
}
