package vn.iostar.uber.activitys.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import vn.iostar.uber.R;
import vn.iostar.uber.activitys.HomeActivity;

public class FinishTripActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private TextView text_rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_trip);
        ratingBar = findViewById(R.id.rate);
        text_rate = findViewById(R.id.txt_rate);

        getForm();
    }

    private void getForm() {
        LinearLayout btn_x=findViewById(R.id.x);

        btn_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FinishTripActivity.this, HomeActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                text_rate.setText(String.valueOf(rating));
            }
        });

    }
}