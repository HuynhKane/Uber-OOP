package vn.iostar.uber.activitys.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import vn.iostar.uber.activitys.MainActivityClient;
import vn.iostar.uber.controllers.TaiKhoanController;
import vn.iostar.uber.databinding.ActivityRegisterBinding;

public class RegisterClientActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    TaiKhoanController taiKhoanController=new TaiKhoanController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();

    }
    private void getData(){
        FirebaseUser current= firebaseAuth.getCurrentUser();
        binding.edtFirstName.setText(current.getDisplayName());
        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.edtPhoneNumber.getText().toString().isEmpty()){
                    Toast.makeText(RegisterClientActivity.this,"Vui longf nhập số điện thoại",Toast.LENGTH_SHORT).show();
                }
                else {
                    taiKhoanController.UpdateAcc_Client(binding.edtFirstName.getText().toString(),binding.edtPhoneNumber.getText().toString(),RegisterClientActivity.this);
                    startActivity(new Intent(RegisterClientActivity.this, MainActivityClient.class));
                }
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}