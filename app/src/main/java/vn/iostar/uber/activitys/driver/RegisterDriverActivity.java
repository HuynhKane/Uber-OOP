package vn.iostar.uber.activitys.driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import vn.iostar.uber.R;
import vn.iostar.uber.activitys.HomeActivity;
import vn.iostar.uber.activitys.MainActivityClient;
import vn.iostar.uber.activitys.MainActivityDriver;
import vn.iostar.uber.activitys.client.Map_TypeVehical;
import vn.iostar.uber.activitys.client.RegisterClientActivity;
import vn.iostar.uber.adapters.TypeVehicalAdapter;
import vn.iostar.uber.controllers.LoaiXeController;
import vn.iostar.uber.controllers.TaiKhoanController;
import vn.iostar.uber.databinding.ActivityRegisterBinding;
import vn.iostar.uber.databinding.ActivityRegisterDriverBinding;
import vn.iostar.uber.models.LoaiXe;

public class RegisterDriverActivity extends AppCompatActivity {

    private ActivityRegisterDriverBinding binding;
    FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
    TaiKhoanController taiKhoanController=new TaiKhoanController();
    ArrayList<LoaiXe> listTypeVehical=new ArrayList<>();
    TypeVehicalAdapter typeVehicalAdapter;
    LoaiXeController loaiXeController=new LoaiXeController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();
    }

    private void getData() {
        FirebaseUser current= firebaseAuth.getCurrentUser();
        loaiXeController.getListLoaiXe(new LoaiXeController.DataRetrievedCallback_LoaiXe() {
            @Override
            public void onDataRetrieved(ArrayList<LoaiXe> listLoaiXe) {
                listTypeVehical=listLoaiXe;
                typeVehicalAdapter=new TypeVehicalAdapter(RegisterDriverActivity.this, com.karumi.dexter.R.layout.support_simple_spinner_dropdown_item,listTypeVehical);
                binding.spinnerDropdown.setAdapter(typeVehicalAdapter);
            }
        });
        binding.edtFirstName.setText(current.getDisplayName());

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.edtPhoneNumber.getText().toString().isEmpty()
                    ||binding.editCccd.getText().toString().isEmpty()
                    ||binding.editBienSo.getText().toString().isEmpty()){
                    Toast.makeText(RegisterDriverActivity.this,"Vui longf nhập số điện thoại",Toast.LENGTH_SHORT).show();
                }
                else {
                    LoaiXe selectedLoaiXe = (LoaiXe) binding.spinnerDropdown.getSelectedItem();
                    taiKhoanController.UpdateAcc_Driver(binding.edtFirstName.getText().toString(),
                                                        binding.edtPhoneNumber.getText().toString(),
                                                        binding.editCccd.getText().toString(),
                                                        binding.editBienSo.getText().toString(),
                                                        selectedLoaiXe.getIdLoaiXe().toString(),
                                                    RegisterDriverActivity.this);
                    startActivity(new Intent(RegisterDriverActivity.this, MainActivityDriver.class));
                }
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                AuthUI.getInstance()
                        .signOut(RegisterDriverActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(RegisterDriverActivity.this, HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });

            }
        });
    }
}