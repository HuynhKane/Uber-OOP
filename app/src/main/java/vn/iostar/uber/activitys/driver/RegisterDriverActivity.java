package vn.iostar.uber.activitys.driver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import vn.iostar.uber.R;
import vn.iostar.uber.databinding.ActivityRegisterBinding;
import vn.iostar.uber.databinding.ActivityRegisterDriverBinding;

public class RegisterDriverActivity extends AppCompatActivity {

    private ActivityRegisterDriverBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getData();
    }

    private void getData() {
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}