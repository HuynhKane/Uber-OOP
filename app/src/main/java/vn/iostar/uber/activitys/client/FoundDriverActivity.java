package vn.iostar.uber.activitys.client;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import vn.iostar.uber.R;
import vn.iostar.uber.activitys.HomeActivity;
import vn.iostar.uber.databinding.ActivityFoundDriverBinding;
import vn.iostar.uber.databinding.ActivityMainClientBinding;

public class FoundDriverActivity extends AppCompatActivity {
    private ActivityFoundDriverBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoundDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        finishTrip();

    }

    private void finishTrip() {

        binding.btnConfirmDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FoundDriverActivity.this, FinishTripActivity.class));


            }
        });
        binding.x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FoundDriverActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        binding.txtTen.setText(FinalBookingFormActivity.taiXe.getTen());
        binding.txtSdt.setText(FinalBookingFormActivity.taiXe.getSdt());



    }
}