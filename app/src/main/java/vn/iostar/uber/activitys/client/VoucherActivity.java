package vn.iostar.uber.activitys.client;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import vn.iostar.uber.R;
import vn.iostar.uber.adapters.TypeVehicalAdapter;
import vn.iostar.uber.adapters.VoucherAdapter;
import vn.iostar.uber.controllers.LoaiXeController;
import vn.iostar.uber.controllers.UuDaiController;
import vn.iostar.uber.models.LoaiXe;
import vn.iostar.uber.models.UuDai;

public class VoucherActivity  extends AppCompatActivity {

    ListView lv_voucher;
    ArrayList<UuDai> listVoucher =new ArrayList<>();
    VoucherAdapter voucherAdapter;
    UuDaiController uuDaiController=new UuDaiController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voucher);
        getForm();

    }

    private void getForm() {
        lv_voucher=findViewById(R.id.lv_voucher);
        listVoucher.clear();
        ProgressDialog progressDialog = new ProgressDialog(VoucherActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        uuDaiController.getListUuDai(new UuDaiController.DataRetrievedCallback_UuDai() {
            @Override
            public void onDataRetrieved(ArrayList<UuDai> listUuDai) {
                listVoucher = listUuDai;
                voucherAdapter = new VoucherAdapter(VoucherActivity.this,R.layout.item_voucher, listVoucher);
                lv_voucher.setAdapter(voucherAdapter);
                progressDialog.dismiss();
            }
        } );





    }
}