package vn.iostar.uber.activitys.client;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import vn.iostar.uber.R;

public class FoundDriverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_driver);
        finishTrip();

    }

    private void finishTrip() {

        LinearLayout btn_confirm_driver = findViewById(R.id.btn_confirm_driver);
        btn_confirm_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FoundDriverActivity.this, FinishTripActivity.class));


            }
        });
    }
}