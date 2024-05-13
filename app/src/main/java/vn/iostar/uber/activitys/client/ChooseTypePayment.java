package vn.iostar.uber.activitys.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import vn.iostar.uber.R;
import vn.iostar.uber.activitys.HomeActivity;
import vn.iostar.uber.activitys.MainActivityDriver;
import vn.iostar.uber.activitys.driver.RegisterDriverActivity;

public class ChooseTypePayment extends AppCompatActivity {
    public static String typePayment = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type_payment);
        choose_type();

    }

    private void choose_type() {
        RelativeLayout btn_cash = findViewById(R.id.btn_cash);
        RelativeLayout btn_credit = findViewById(R.id.btn_credit);
        LinearLayout btn_x=findViewById(R.id.x);


        btn_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typePayment = "cash";
                showFinalScreen();

            }
        });

        btn_credit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typePayment = "credit";
                showCreditScreen();
            }
        });

        btn_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChooseTypePayment.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showCreditScreen() {
        startActivity(new Intent(ChooseTypePayment.this, CreditPayment.class));
    }

    private void showFinalScreen() {
        startActivity(new Intent(ChooseTypePayment.this, FinalBookingFormActivity.class));
    }
}