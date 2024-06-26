package vn.iostar.uber.activitys.client;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vn.iostar.uber.R;
import vn.iostar.uber.activitys.HomeActivity;
import vn.iostar.uber.controllers.FinalBookingController;
import vn.iostar.uber.controllers.GeocodingHelper;
import vn.iostar.uber.controllers.YeuCauDatXeController;
import vn.iostar.uber.models.LoaiXe;
import vn.iostar.uber.models.TaiXe;
import vn.iostar.uber.models.UuDai;
import vn.iostar.uber.models.YeuCauDatXe;
import vn.iostar.uber.ui.home.home;

public class FinalBookingFormActivity extends AppCompatActivity {
    GeocodingHelper geocodingHelper=new GeocodingHelper();
    TextView diemDon,diemDen,tenXe,giaTien,txt_uuDai,tongTien,thoiGian;
    ImageView icon ,typePayment;
    LinearLayout btn_confirm_booking, btn_x;
    TableLayout table;

    FinalBookingController finalBookingController=new FinalBookingController();

    YeuCauDatXe yeuCauDatXe;
    private String posFrom;
    private String posTo;
    public static TaiXe taiXe=new TaiXe();
    private String typePay= ChooseTypePaymentActivity.typePayment;
    private LoaiXe typeCar= Map_TypeVehicalActivity.loaiXe;
    private UuDai voucher= VoucherActivity.uuDai;
    private String idDriverTemp;
    public static ArrayList<String> denyDriver=new ArrayList<>();

    private YeuCauDatXeController yeuCauDatXeController=new YeuCauDatXeController();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_booking_form);
        getForm();
        try {
            getInforBoooking(posFrom,posTo,typePay,typeCar,voucher);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        table=findViewById(R.id.table);
        btn_x=findViewById(R.id.x);


        posFrom=geocodingHelper.getAddressFromLatLng(FinalBookingFormActivity.this,home.from);
        posTo=geocodingHelper.getAddressFromLatLng(FinalBookingFormActivity.this,home.to);

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


        } //Hình ảnh loại xe
        tenXe.setText(typeCar.getTenLoaiXe());
        thoiGian.setText("15 min");
        txt_uuDai.setText(voucher.getUuDai());

        btn_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(FinalBookingFormActivity.this, FormWaiting.class);
                startActivity(intent);
            }
        });


        Geocoder geocoder = new Geocoder(FinalBookingFormActivity.this, Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(home.from.latitude,home.from.longitude, 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String cityName =addressList.get(0).getLocality();
        btn_confirm_booking.setOnClickListener(new View.OnClickListener() {   //*****
            @Override
            public void onClick(View v) {

                    yeuCauDatXeController.getIdClosestDriver(cityName, null,home.from,new YeuCauDatXeController.Retriver_Client() {
                        @Override
                        public void onSuccess(String idClient) {
                            idDriverTemp=idClient;
                                yeuCauDatXeController.consider_room(cityName, idClient,null, FirebaseAuth.getInstance().getCurrentUser().getUid(), home.from,new YeuCauDatXeController.Callback() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(FinalBookingFormActivity.this,"Chờ đi",Toast.LENGTH_SHORT).show();
                                        yeuCauDatXeController.addNewYeuCauDatXe(yeuCauDatXe, cityName, new YeuCauDatXeController.Callback_Bool() {
                                            @Override
                                            public void onSuccess() {
                                                Toast.makeText(FinalBookingFormActivity.this,"OK sắp có xe r ",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        try {
                                            yeuCauDatXeController.foundDriver(FinalBookingFormActivity.this, FirebaseAuth.getInstance().getUid(), new YeuCauDatXeController.Retriver_Client() {
                                                @Override
                                                public void onSuccess(String idClient) {
                                                    Intent intent = new Intent(FinalBookingFormActivity.this, FoundDriverActivity.class);
                                                    startActivity(intent);
                                                }

                                                @Override
                                                public void onFail() {
                                                    Toast.makeText(FinalBookingFormActivity.this,"Gía như cô ấy đừng xuất hiện",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }


//                                        ProgressDialog progressDialog = new ProgressDialog(FinalBookingFormActivity.this);
//                                        progressDialog.setMessage("Loading...");
//                                        progressDialog.show();
                                    }

                                    @Override
                                    public void onFail() {
                                        Toast.makeText(FinalBookingFormActivity.this,"kHÔNG CÓ TÀI XẾ TRONG KHU VỰC",Toast.LENGTH_SHORT).show();

                                    }
                                });
                        }
                        @Override
                        public void onFail() {

                        }
                    });



                finish();
            }
        });

    }
    private void getInforBoooking(String posFrom, String posTo, String typePay, LoaiXe typeCar, UuDai voucher) throws IOException {

        Float initalPrice =finalBookingController.getPriceOfTypeCar(typeCar,home.from,home.to);
        giaTien.setText(initalPrice.toString());


        ////////////caculate final price///////////////
        Double finalPrice =finalBookingController.getPriceUseVoucher(voucher,initalPrice);
        tongTien.setText(finalPrice.toString());

        finalBookingController.paymentInfor(typePay,finalPrice);
        //taiXe= finalBookingController.chooseDriver(home.from,home.to,typePay);

        yeuCauDatXe =new YeuCauDatXe( FirebaseAuth.getInstance().getCurrentUser().getUid(),typeCar.getIdLoaiXe(),voucher.getIdUuDai(),home.from.toString(),home.to.toString(),finalPrice,"wait");


    }






}