package vn.iostar.uber.activitys.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import vn.iostar.uber.R;
import vn.iostar.uber.activitys.HomeActivity;
import vn.iostar.uber.controllers.GeocodingHelper;
import vn.iostar.uber.models.LoaiXe;
import vn.iostar.uber.models.UuDai;
import vn.iostar.uber.ui.home.home;

public class FinalBookingFormActivity extends AppCompatActivity {
    GeocodingHelper geocodingHelper=new GeocodingHelper();
    TextView diemDon,diemDen,tenXe,giaTien,txt_uuDai,tongTien,thoiGian;
    ImageView icon ,typePayment;
    LinearLayout btn_confirm_booking, btn_x;


    private String posFrom=geocodingHelper.getAddressFromLatLng(FinalBookingFormActivity.this,home.from);
    private String posTo=geocodingHelper.getAddressFromLatLng(FinalBookingFormActivity.this,home.to);
    private String typePay= ChooseTypePaymentActivity.typePayment;
    private LoaiXe typeCar= Map_TypeVehicalActivity.loaiXe;
    private UuDai voucher= VoucherActivity.uuDai;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_booking_form);
        getForm();
        getInforBoooking(posFrom,posTo,typePay,typeCar,voucher);
    }

    private void getForm() {
        diemDon = findViewById(R.id.txt_pick_up);
        diemDen = findViewById(R.id.txt_destination);
        icon = findViewById(R.id.icon);
        tenXe = findViewById(R.id.ten);
        giaTien = findViewById(R.id.gia);
        txt_uuDai = findViewById(R.id.txt_voucher);
        tongTien = findViewById(R.id.total_price);
        thoiGian = findViewById(R.id.txt_estimate_time);
        btn_confirm_booking = findViewById(R.id.btn_confirm_booking);
        typePayment = findViewById(R.id.typePay);
        btn_x=findViewById(R.id.x);
    }
    private void getInforBoooking(String posFrom, String posTo, String typePay, LoaiXe typeCar, UuDai voucher) {

        diemDon.setText(posFrom);
        diemDen.setText(posTo);
        String id= typeCar.getIdLoaiXe();
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
        String type = typePay;
        switch (type){
            case "cash":{
                typePayment.setImageResource(R.drawable.ic_cash);
                break;
            }
            case "visa":{
                typePayment.setImageResource(R.drawable.ic_visa);
                break;
            }
            case "master_card":{
                typePayment.setImageResource(R.drawable.ic_mastercard);
                break;
            }
            case "paypal":{
                typePayment.setImageResource(R.drawable.ic_paypal);
                break;
            }


        }
        tenXe.setText(typeCar.getTenLoaiXe());
        thoiGian.setText("15 min");
        giaTien.setText(typeCar.getGia().toString());
        txt_uuDai.setText(voucher.getUuDai());


        ////////////caculate final price///////////////
        Float giaTien_float = typeCar.getGia();
        String uuDai_str = voucher.getUuDai();
        tongTien.setText(getFinalPrice(giaTien_float, uuDai_str).toString());

        btn_confirm_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseDriver();
                //diemDen, diemDon,tongTien,typePay
            }
        });



        btn_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FinalBookingFormActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }


    private void chooseDriver() {

        startActivity(new Intent(FinalBookingFormActivity.this, FoundDriverActivity.class));

    }


    private Double getFinalPrice(Float giaTienFloat, String uuDaiStr) {
        uuDaiStr = uuDaiStr.replace("%", "");
        Double gia_tien = (1 - Double.parseDouble(uuDaiStr)/100) * (giaTienFloat);
        return gia_tien;
    }

}