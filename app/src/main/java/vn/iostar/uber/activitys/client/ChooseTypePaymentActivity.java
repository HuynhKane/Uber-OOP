package vn.iostar.uber.activitys.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

import vn.iostar.uber.R;
import vn.iostar.uber.activitys.HomeActivity;

public class ChooseTypePaymentActivity extends AppCompatActivity {
    public static String typePayment = "";

    RelativeLayout btn_cash ;
    RelativeLayout btn_credit;
    LinearLayout btn_x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type_payment);
        getForm();
        btnConfirmPayment_Click(); //***

    }
    private void btnConfirmPayment_Click(){
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
    }

    private void getForm() {
         btn_cash = findViewById(R.id.btn_cash);
         btn_credit = findViewById(R.id.btn_credit);
         btn_x=findViewById(R.id.x);




        btn_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ChooseTypePaymentActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showCreditScreen() {
        startActivity(new Intent(ChooseTypePaymentActivity.this, CreditPaymentActivity.class));
    }

    private void showFinalScreen() {
        startActivity(new Intent(ChooseTypePaymentActivity.this, FinalBookingFormActivity.class));
    }
}