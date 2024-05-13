package vn.iostar.uber.activitys.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import vn.iostar.uber.R;
import vn.iostar.uber.models.LoaiXe;
import vn.iostar.uber.models.UuDai;

public class FinalBookingFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_booking_form);
        getInforBoooking(Map_TypeVehicalActivity.loaiXe, VoucherActivity.uuDai, ChooseTypePayment.typePayment);
    }

    private void getInforBoooking(LoaiXe loaiXe, UuDai uuDai, String typePayment) {

        ////////get id /////////////////////////////////
        TextView diemDon = findViewById(R.id.txt_pick_up);
        TextView diemDen = findViewById(R.id.txt_destination);
        ImageView icon = findViewById(R.id.icon);
        TextView tenXe = findViewById(R.id.ten);
        TextView giaTien = findViewById(R.id.gia);
        TextView txt_uuDai = findViewById(R.id.txt_voucher);
        TextView tongTien = findViewById(R.id.total_price);
        TextView thoiGian = findViewById(R.id.txt_estimate_time);
        LinearLayout btn_confirm_booking = findViewById(R.id.btn_confirm_booking);
        ImageView typePay = findViewById(R.id.typePay);

        //////////SetText//////////////////
        diemDon.setText("Null");
        diemDen.setText("Null");
        String id= loaiXe.getIdLoaiXe();
        switch (id) {
            case "idbike": {
                icon.setImageResource(R.drawable.ic_bike);
                break;
            }
            case "idcar": {
                icon.setImageResource(R.drawable.ic_car);
                break;
            }
            case "idlimo": {
                icon.setImageResource(R.drawable.ic_limo);
                break;
            }

        }
        String type = typePayment;
        switch (type){
            case "cash":{
                typePay.setImageResource(R.drawable.ic_cash);
                break;
            }
            case "visa":{
                typePay.setImageResource(R.drawable.ic_visa);
                break;
            }
            case "master_card":{
                typePay.setImageResource(R.drawable.ic_mastercard);
                break;
            }
            case "paypal":{
                typePay.setImageResource(R.drawable.ic_paypal);
                break;
            }


        }
        tenXe.setText(loaiXe.getTenLoaiXe());
        thoiGian.setText("15 min");
        giaTien.setText(loaiXe.getGia().toString());
        txt_uuDai.setText(uuDai.getUuDai());


        ////////////caculate final price///////////////
        Float giaTien_float = loaiXe.getGia();
        String uuDai_str = uuDai.getUuDai();
        tongTien.setText(getFinalPrice(giaTien_float, uuDai_str).toString());

        btn_confirm_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDriver(diemDen, diemDon,tongTien,typePay);

            }
        });



    }

    private void chooseDriver(TextView diemDen, TextView diemDon, TextView tongTien, ImageView typePay) {

        startActivity(new Intent(FinalBookingFormActivity.this, FoundDriverActivity.class));

    }


    private Double getFinalPrice(Float giaTienFloat, String uuDaiStr) {
        uuDaiStr = uuDaiStr.replace("%", "");
        Double gia_tien = (1 - Double.parseDouble(uuDaiStr)/100) * (giaTienFloat);
        return gia_tien;
    }

}